# ai-core/agent.py

import os
import openai
from dotenv import load_dotenv

load_dotenv()
api_key = os.getenv("OPENAI_API_KEY")
if not api_key:
    raise ValueError("OPENAI_API_KEY not found in environment!")

client = openai.OpenAI(api_key=api_key)

def ask_agent(prompt):
    print("🔍 Prompt received:", prompt) 
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",  # or "gpt-4" if available
        messages=[
            {"role": "user", "content": prompt}
        ]
    )
    return response.choices[0].message.content

if __name__ == "__main__":
    while True:
        user_input = input("You: ")
        print("AI:", ask_agent(user_input))


