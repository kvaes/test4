# Quick Start Guide

Get the BICS Semantic Kernel Agent up and running in under 5 minutes.

## Prerequisites

- ☕ **Java 17+** installed
- 📦 **Maven 3.6+** installed
- 🚀 **5 minutes** of your time

## 🚀 Option 1: Local Development

### 1. Clone and Navigate
```bash
git clone https://github.com/kvaes/test4.git
cd test4/agent
```

### 2. Set Environment Variables
```bash
export OPENAI_API_KEY=your-openai-api-key
export OPENAI_MODEL=gpt-3.5-turbo
```

### 3. Run the Application
```bash
# Quick test
mvn clean test

# Start the application
mvn spring-boot:run
```

### 4. Verify It's Working
```bash
curl http://localhost:8080/actuator/health
# Should return: {"status":"UP"}
```

**🎉 That's it! Your agent is running on http://localhost:8080**

## 🐳 Option 2: Docker (Even Faster)

### 1. Pull and Run
```bash
docker run -p 8080:8080 \
  -e OPENAI_API_KEY=your-api-key \
  ghcr.io/kvaes/test4/agent:latest
```

### 2. Test
```bash
curl http://localhost:8080/actuator/health
```

**🎉 Done! Zero local setup required.**

## ☁️ Option 3: GitHub Codespaces (Zero Install)

### 1. Open in Codespaces
1. Go to https://github.com/kvaes/test4
2. Click **Code** → **Codespaces** → **Create codespace**
3. Wait 2-3 minutes for setup

### 2. Run in Codespaces
```bash
cd agent
mvn spring-boot:run
```

**🎉 Complete cloud development environment ready!**

## 🧪 Test the API Plugins

Once running, test the different API plugins:

### Connect API (Authentication)
```bash
curl -X POST http://localhost:8080/api/connect/status
```

### SMS API
```bash
curl -X POST http://localhost:8080/api/sms/send \
  -H "Content-Type: application/json" \
  -d '{"to": "+1234567890", "message": "Hello from BICS Agent!"}'
```

### MyNumbers API
```bash
curl -X GET "http://localhost:8080/api/mynumbers?country=US" \
  -H "Authorization: Bearer your-token"
```

## 🔧 Configuration

### Environment Variables
| Variable | Description | Default |
|----------|-------------|---------|
| `OPENAI_API_KEY` | Your OpenAI API key | `your-api-key-here` |
| `OPENAI_MODEL` | OpenAI model to use | `gpt-3.5-turbo` |
| `SERVER_PORT` | Application port | `8080` |

### API Endpoints Available
The agent provides plugins for 8 BICS APIs:

- **Connect API** - Authentication & connectivity
- **MyNumbers API** - Number management  
- **SMS API** - Send SMS messages
- **Address Management** - Address operations
- **CDR** - Call detail records
- **Disconnection** - Number disconnection
- **Emergency Services** - Emergency configuration
- **Number Porting** - Port numbers

## 📚 Next Steps

### For Developers
- 📖 Read the [Development Guide](docs/development.md)
- 🧪 Run the test suite: `mvn test`
- 🔧 Add new plugins following the [Plugin Development Guide](docs/plugin-development.md)

### For DevOps
- 🐳 Check [Deployment Guide](docs/deployment.md)
- 🔒 Review [Security Policy](.github/SECURITY.md)
- 📊 Set up monitoring with `/actuator` endpoints

### For Cloud Development
- ☁️ Follow [GitHub Codespaces Setup](docs/codespaces.md)
- 🔄 Use the CI/CD pipeline for automated deployments

## 🆘 Troubleshooting

### Common Issues

**Port 8080 already in use?**
```bash
export SERVER_PORT=8081
mvn spring-boot:run
```

**Build fails?**
```bash
mvn clean
mvn dependency:resolve
mvn compile
```

**Tests fail?**
```bash
# Check Java version
java -version  # Should be 17+

# Clear and rebuild
mvn clean test
```

**Docker issues?**
```bash
# Rebuild the image
mvn jib:dockerBuild

# Check if image exists
docker images | grep bics-semantic-kernel-agent
```

## 🔗 Useful Links

- 📖 [Full Documentation](docs/README.md)
- 🐛 [Report Issues](https://github.com/kvaes/test4/issues)
- 💬 [Discussions](https://github.com/kvaes/test4/discussions)
- 🔒 [Security Policy](.github/SECURITY.md)

---

**Need help?** Create an issue or check the documentation for detailed guides!