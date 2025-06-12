# BICS Semantic Kernel Agent

A Java-based Semantic Kernel agent that provides AI-powered access to various BICS APIs including Connect, MyNumbers, and SMS services.

## Features

- 🚀 **Spring Boot** based architecture for dependency injection and configuration management
- 🔌 **Plugin Architecture** for modular API integrations
- 🐳 **Docker Support** with automated container builds
- 🧪 **Comprehensive Testing** with JUnit 5 and Mockito
- 🔒 **Security First** with built-in error handling and validation
- 📊 **CI/CD Pipeline** with GitHub Actions
- 🔄 **Dependency Management** with Dependabot integration

## API Plugins

The agent includes plugins for the following BICS APIs:

1. **Connect API** - Authentication and connectivity services
2. **MyNumbers API** - Number management and provisioning
3. **MyNumbers Address Management API** - Address-related operations
4. **MyNumbers CDR API** - Call detail records
5. **MyNumbers Disconnection API** - Number disconnection services
6. **MyNumbers Emergency Services API** - Emergency service configuration
7. **MyNumbers Number Porting API** - Number porting operations
8. **SMS API** - SMS messaging services

## Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker (optional, for container deployment)

### Local Development Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/kvaes/test4.git
   cd test4/agent
   ```

2. **Configure the application:**
   ```bash
   # Copy the configuration template
   cp src/main/resources/application.yml.example src/main/resources/application.yml
   
   # Edit the configuration file
   nano src/main/resources/application.yml
   ```

3. **Set required environment variables:**
   ```bash
   export OPENAI_API_KEY=your-openai-api-key
   export OPENAI_MODEL=gpt-3.5-turbo
   ```

4. **Build and run the application:**
   ```bash
   # Run tests
   mvn clean test
   
   # Build the application
   mvn clean compile
   
   # Run the application
   mvn spring-boot:run
   ```

5. **Verify the application is running:**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

### Docker Deployment

1. **Build the Docker image:**
   ```bash
   mvn jib:dockerBuild
   ```

2. **Run the container:**
   ```bash
   docker run -p 8080:8080 \
     -e OPENAI_API_KEY=your-api-key \
     -e OPENAI_MODEL=gpt-3.5-turbo \
     bics-semantic-kernel-agent:latest
   ```

## Configuration

The application uses Spring Boot's configuration system. Key configuration options:

```yaml
agent:
  apiUrls:
    connect: "https://connect-api.bics.com"
    mynumbers: "https://mynumbers-api.bics.com"
    # ... other API URLs
  openAiApiKey: ${OPENAI_API_KEY:your-api-key-here}
  openAiModel: ${OPENAI_MODEL:gpt-3.5-turbo}

server:
  port: 8080

logging:
  level:
    com.bics.agent: DEBUG
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `OPENAI_API_KEY` | OpenAI API key for AI services | `your-api-key-here` |
| `OPENAI_MODEL` | OpenAI model to use | `gpt-3.5-turbo` |
| `SERVER_PORT` | Server port | `8080` |

## Project Structure

```
agent/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/bics/agent/
│   │   │       ├── Application.java              # Main application class
│   │   │       ├── BicsSemanticAgent.java        # Core agent service
│   │   │       ├── config/                       # Configuration classes
│   │   │       ├── exceptions/                   # Exception classes
│   │   │       ├── functions/                    # API plugin functions
│   │   │       └── models/                       # Data models
│   │   └── resources/
│   │       └── application.yml                   # Configuration file
│   └── test/
│       └── java/                                # Unit tests
├── pom.xml                                      # Maven configuration
└── Dockerfile                                   # Docker configuration
```

## Development

### Running Tests

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=ConnectPluginTest

# Run tests with coverage
mvn clean test jacoco:report
```

### Code Style

This project follows standard Java conventions:
- Use meaningful variable and method names
- Write comprehensive Javadoc for public APIs
- Keep methods focused and small
- Use dependency injection where appropriate

### Adding New API Plugins

1. Create a new plugin class in `src/main/java/com/bics/agent/functions/`
2. Implement the plugin methods with proper error handling
3. Add the plugin to `BicsSemanticAgent.java`
4. Update configuration in `application.yml`
5. Write unit tests for the new plugin

Example plugin structure:
```java
@Component
public class NewApiPlugin {
    private final String baseUrl;
    private final OkHttpClient httpClient;
    
    public NewApiPlugin(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = new OkHttpClient();
    }
    
    public String someMethod(String param) {
        // Implementation
    }
}
```

## CI/CD Pipeline

The project includes a comprehensive CI/CD pipeline that:

- **Runs on every push/PR** to main branches
- **Executes all tests** with JUnit 5
- **Builds Docker images** using Jib
- **Publishes containers** to GitHub Container Registry
- **Performs security scans** with Trivy
- **Updates dependencies** automatically with Dependabot

### Workflow Triggers

- Push to `main` or `develop` branches
- Pull requests to `main` branch
- Changes in the `agent/` directory

## Monitoring and Observability

The application includes:

- **Health checks** at `/actuator/health`
- **Metrics** at `/actuator/metrics`
- **Structured logging** with Logback
- **Error tracking** with custom exception handling

## Security

- All API communications use HTTPS
- Credentials are managed through environment variables
- Dependencies are regularly updated via Dependabot
- Security scanning is performed on every build
- See [SECURITY.md](.github/SECURITY.md) for vulnerability reporting

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE) file for details.

## Support

For support and questions:
- Create an issue in this repository
- Contact the development team at: dev@bics.com
- Check the [documentation](docs/) for detailed guides

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for a list of changes and version history.