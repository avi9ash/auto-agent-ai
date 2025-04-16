# ai-core/app.py

from flask import Flask, request, jsonify
from agent import Agent
import logging
import json

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = Flask(__name__)
agent = Agent()

@app.route('/ask', methods=['POST', 'GET'])
def ask_agent():
    """
    Endpoint to ask the agent a question
    Supports both POST (JSON body) and GET (query parameters)
    
    POST Example:
    {
        "prompt": "What's in my latest email?"
    }
    
    GET Example:
    /ask?prompt=What%27s%20in%20my%20latest%20email%3F
    """
    try:
        # Get prompt from either JSON body or query parameters
        if request.method == 'POST':
            data = request.get_json()
            if not data or 'prompt' not in data:
                return jsonify({"error": "Missing 'prompt' in request body"}), 400
            prompt = data['prompt']
        else:  # GET
            prompt = request.args.get('prompt')
            if not prompt:
                return jsonify({"error": "Missing 'prompt' query parameter"}), 400
        
        logger.info(f"Received prompt: {prompt}")
        
        # Process the prompt through the agent
        response = agent.process_prompt(prompt)
        
        # Log the response
        logger.info(f"Agent response: {json.dumps(response, indent=2)}")
        
        return jsonify(response)
    
    except Exception as e:
        logger.error(f"Error processing request: {str(e)}")
        return jsonify({"error": str(e)}), 500

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({"status": "healthy"})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5050, debug=True)
