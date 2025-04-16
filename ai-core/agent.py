# ai-core/agent.py

import os
import json
import requests
import openai
import logging
from abc import ABC, abstractmethod
from typing import Dict, Any, Optional, List
from dotenv import load_dotenv
import re

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# Load and validate environment
load_dotenv()

# Validate required environment variables
required_env_vars = [
    "OPENAI_API_KEY",
    "ZAPIER_CALENDAR_WEBHOOK",
    "ZAPIER_WHATSAPP_WEBHOOK",
    "ZAPIER_EMAIL_WEBHOOK"
]

missing_vars = [var for var in required_env_vars if not os.getenv(var)]
if missing_vars:
    error_msg = f"Missing required environment variables: {', '.join(missing_vars)}"
    logger.error(error_msg)
    raise ValueError(error_msg)

# Initialize OpenAI client
client = openai.OpenAI(api_key=os.getenv("OPENAI_API_KEY"))

# Define available Zapier webhook routes with validation
ZAP_ROUTES = {
    "create_meeting": {
        "url": os.getenv("ZAPIER_CALENDAR_WEBHOOK"),
        "required_fields": ["title", "start_time", "end_time", "attendees"]
    },
    "send_whatsapp_message": {
        "url": os.getenv("ZAPIER_WHATSAPP_WEBHOOK"),
        "required_fields": ["message", "recipient"]
    },
    "get_email": {
        "url": os.getenv("ZAPIER_EMAIL_WEBHOOK"),
        "required_fields": []
    }
}

class ActionHandler(ABC):
    """Base class for all action handlers."""
    @abstractmethod
    def can_handle(self, prompt: str) -> bool:
        pass
    
    @abstractmethod
    def handle(self, prompt: str) -> Dict[str, Any]:
        pass

class OpenAIActionHandler(ActionHandler):
    def __init__(self, client: openai.OpenAI):
        self.client = client
        self.system_prompt = """
        You are an action router that identifies user intents and routes them to appropriate Zapier actions.
        Available actions:
        - create_meeting: schedule a meeting in Google Calendar
        - send_whatsapp_message: send a message to Whatsapp
        - get_email: get a summary of unread Gmail emails

        For each action, respond ONLY with a JSON object in this exact format:
        {
          "action": "action_name",
          "data": {
            "key1": "value1",
            "key2": "value2"
          }
        }

        Required fields for each action:
        - create_meeting: subject, date, time, attendees
        - send_whatsapp_message: message, recipient
        - get_email: (no required fields)

        Do not include any additional text or explanations. Only return the JSON object.
        """
    
    def can_handle(self, prompt: str) -> bool:
        return True  # Primary handler
    
    def handle(self, prompt: str) -> Dict[str, Any]:
        try:
            logger.info(f"Processing prompt: {prompt}")
            completion = self.client.chat.completions.create(
                model="gpt-3.5-turbo",
                messages=[
                    {"role": "system", "content": self.system_prompt},
                    {"role": "user", "content": prompt}
                ],
                temperature=0.2
            )
            
            # Extract the JSON response
            response_text = completion.choices[0].message.content
            try:
                action_data = json.loads(response_text)
                return self._execute_action(action_data, prompt)
            except json.JSONDecodeError:
                logger.error(f"Failed to parse AI response as JSON: {response_text}")
                return {"success": False, "error": "Invalid response format"}
                
        except Exception as e:
            logger.error(f"Error in handler: {str(e)}")
            return {"success": False, "error": str(e)}
    
    def _execute_action(self, data: Dict[str, Any], original_prompt: str) -> Dict[str, Any]:
        """Execute the identified action with Zapier."""
        try:
            action = data.get("action")
            if not action or action not in ZAP_ROUTES:
                return {"success": False, "error": f"Invalid action: {action}"}
            
            # Validate required fields
            required_fields = ZAP_ROUTES[action]["required_fields"]
            missing_fields = [field for field in required_fields if not data.get("data", {}).get(field)]
            if missing_fields:
                return {"success": False, "error": f"Missing required fields: {missing_fields}"}
            
            # Standardize the data structure and always include original prompt in message
            standardized_data = {
                "action": action,
                "data": {
                    "message": original_prompt,  # Always include original prompt
                    "recipient": data.get("data", {}).get("recipient", "Not Available"),
                    "subject": data.get("data", {}).get("subject", "Not Available"),
                    "body": data.get("data", {}).get("body", "Not Available"),
                    "date": data.get("data", {}).get("date", "Not Available"),
                    "time": data.get("data", {}).get("time", "Not Available"),
                    "duration": data.get("data", {}).get("duration", "Not Available"),
                    "attendees": data.get("data", {}).get("attendees", "Not Available"),
                    "original_prompt": original_prompt  # Also store original prompt separately
                }
            }
            
            # Execute the action
            zap_url = ZAP_ROUTES[action]["url"]
            logger.info(f"Executing {action} with data: {json.dumps(standardized_data, indent=2)}")
            
            # Make the API request and get response
            response = call_zapier_webhook(zap_url, standardized_data)
            
            # Always include API request details in the response
            response["api_request"] = {
                "url": zap_url,
                "method": "POST",
                "headers": {
                    "Content-Type": "application/json",
                    "User-Agent": "AutoAgent/1.0"
                },
                "payload": standardized_data
            }
            
            return response
            
        except Exception as e:
            logger.error(f"Error executing action: {str(e)}")
            return {
                "success": False, 
                "error": str(e),
                "api_request": {
                    "url": ZAP_ROUTES.get(action, {}).get("url", "Unknown"),
                    "method": "POST",
                    "headers": {
                        "Content-Type": "application/json",
                        "User-Agent": "AutoAgent/1.0"
                    },
                    "payload": standardized_data if 'standardized_data' in locals() else None
                }
            }

class Agent:
    def __init__(self):
        self.client = openai.OpenAI(api_key=os.getenv("OPENAI_API_KEY"))
        self.handlers: List[ActionHandler] = [
            OpenAIActionHandler(self.client)
        ]
        logger.info("Agent initialized with handlers")
    
    def process_prompt(self, prompt: str) -> Dict[str, Any]:
        logger.info(f"Processing prompt: {prompt}")
        for handler in self.handlers:
            if handler.can_handle(prompt):
                logger.info(f"Using handler: {handler.__class__.__name__}")
                return handler.handle(prompt)
        logger.error("No handler found for prompt")
        return {"success": False, "error": "No handler found for prompt"}

def call_zapier_webhook(zap_url: str, data: Dict[str, Any]) -> Dict[str, Any]:
    try:
        logger.info(f"Calling Zapier webhook: {zap_url}")
        logger.info(f"Webhook payload: {json.dumps(data, indent=2)}")
        
        response = requests.post(
            zap_url,
            json=data,
            timeout=30,
            headers={"Content-Type": "application/json"}
        )
        response.raise_for_status()
        
        logger.info(f"Webhook response status: {response.status_code}")
        logger.info(f"Webhook response: {response.text}")
        
        return {
            "success": True,
            "status_code": response.status_code,
            "response": response.json() if response.text else None
        }
    except requests.exceptions.RequestException as e:
        logger.error(f"Webhook error: {str(e)}")
        return {"success": False, "error": str(e)}

def main():
    agent = Agent()
    
    while True:
        try:
            user_input = input("You: ")
            response = agent.process_prompt(user_input)
            if response.get("success"):
                print("Action executed successfully")
            else:
                print(f"Error: {response.get('error')}")
        except KeyboardInterrupt:
            print("\nGoodbye!")
            break
        except Exception as e:
            logger.error(f"Error in main loop: {str(e)}")
            print(f"An error occurred: {str(e)}")

if __name__ == "__main__":
    main()


