# Auto Agent - AI-Powered Action Router

A sophisticated AI agent system that intelligently routes user requests to appropriate actions using Zapier integration. This project demonstrates advanced natural language processing, API integration, and action routing capabilities.

## 🚀 Features

- **Intelligent Action Routing**: Automatically identifies user intents and routes them to appropriate Zapier actions
- **Zapier Integration**: Seamless integration with Zapier for executing various actions
- **Natural Language Processing**: Uses GPT-3.5 to understand and process user requests
- **Standardized Data Handling**: Robust data validation and standardization
- **Error Handling**: Comprehensive error handling and logging
- **API Request Tracking**: Detailed API request/response logging
- **Flask Web Interface**: Clean and intuitive web interface for interaction

## 🛠️ Technical Stack

- **Backend**: Python 3.8+
- **AI Model**: OpenAI GPT-3.5-turbo
- **Web Framework**: Flask
- **API Integration**: Zapier
- **Logging**: Python logging module
- **Environment Management**: python-dotenv

## 📦 Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/auto-agent.git
cd auto-agent
```

2. Set up Python environment:
```bash
cd ai-core
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install -r requirements.txt
```

## 🔑 Required Environment Variables

- `OPENAI_API_KEY`: Your OpenAI API key
- `ZAPIER_NLA_API_KEY`: Your Zapier NLA API key

## 🏗️ Project Structure

```
auto-agent/
├── ai-core/                # Python Flask Backend
│   ├── agent.py           # Main agent logic
│   ├── actions/           # Action handlers
│   │   ├── zapier.py      # Zapier integration
│   │   └── email_summarizer.py
│   ├── app.py            # Flask application
│   └── webhook_handler.py # Webhook handling
├── requirements.txt       # Python dependencies
└── README.md             # Project documentation
```

## 🤖 How It Works

1. **User Input Processing**:
   - Receives natural language input from user
   - Processes through GPT-3.5 to identify intent
   - Extracts relevant parameters

2. **Action Routing**:
   - Identifies appropriate Zapier action
   - Validates required fields
   - Standardizes data structure

3. **Execution**:
   - Sends request to Zapier
   - Handles response
   - Returns standardized output

## 📝 API Documentation

### Endpoints

- `POST /ask`: Process user queries
  - Request body: `{"prompt": "your query"}`
  - Response: Standardized action response

### Response Format

```json
{
  "success": true,
  "response": {
    "action": "action_name",
    "data": {
      "message": "original prompt",
      // other action-specific fields
    }
  },
  "api_request": {
    "url": "zapier_webhook_url",
    "method": "POST",
    "headers": {...},
    "payload": {...}
  }
}
```

## 🧪 Testing

Run the test suite:
```bash
cd ai-core
python -m pytest tests/
```

## 📈 Future Enhancements

- [ ] Add more action types
- [ ] Implement action chaining
- [ ] Add user authentication
- [ ] Implement rate limiting
- [ ] Add action history tracking
- [ ] Add Java Spring Boot API Gateway
- [ ] Implement database persistence
- [ ] Add caching layer
- [ ] Implement monitoring and metrics

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

Avinash - [GitHub](https://github.com/avi9ash)
Cursor - vIbE CoDiNg :)

## 🙏 Acknowledgments

- OpenAI for the GPT-3.5 API
- Zapier for the NLA API
- Flask team for the web framework
