# ai-core/app.py

from flask import Flask, request, jsonify
from agent import ask_agent

app = Flask(__name__)

@app.route("/prompt", methods=["POST"])
def handle_prompt():
    data = request.json
    user_prompt = data.get("prompt", "")
    if not user_prompt:
        return jsonify({"error": "No prompt provided"}), 400
    
    try:
        result = ask_agent(user_prompt)
        return jsonify({"response": result})
    except Exception as e:
        print("🔥 Error in ask_agent:", str(e))  # Add this
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="127.0.0.1", port=5050, debug=True)
