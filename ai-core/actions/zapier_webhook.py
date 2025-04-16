# ai-core/actions/zapier_webhook.py

import requests

# Replace this with your real webhook URL
ZAPIER_WEBHOOK_URL = "https://hooks.zapier.com/hooks/catch/22503724/20pbi21/"

def trigger_zapier_webhook(action: str, data: dict):
    payload = {
        "action": action,
        "data": data
    }

    try:
        response = requests.post(ZAPIER_WEBHOOK_URL, json=payload)
        response.raise_for_status()
        return {"success": True, "status_code": response.status_code}
    except requests.exceptions.RequestException as e:
        return {"success": False, "error": str(e)}
