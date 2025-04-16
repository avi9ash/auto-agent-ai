import os
import openai
import requests
from dotenv import load_dotenv

load_dotenv()

# 🔐 OpenAI key
openai.api_key = os.getenv("OPENAI_API_KEY")

# 🔗 Zapier Webhooks (add more as you grow)
ZAP_ROUTES = {
    "create_meeting": "https://hooks.zapier.com/hooks/catch/123456/abcde",
    "send_slack_message": "https://hooks.zapier.com/hooks/catch/123456/defgh",
    "email_summary": "https://hooks.zapier.com/hooks/catch/123456/xyzabc"
}
