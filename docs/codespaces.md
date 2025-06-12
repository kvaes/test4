# GitHub Codespaces Development Setup

This guide will help you set up a complete development environment using GitHub Codespaces, allowing you to develop and test the BICS Semantic Kernel Agent entirely in the cloud.

## Overview

GitHub Codespaces provides a complete development environment in the cloud, including:

- Pre-configured development container
- All necessary tools and dependencies
- Full IDE experience with Visual Studio Code
- Integrated terminal and debugging capabilities
- Port forwarding for testing web applications

## Quick Start

### 1. Create a Codespace

1. Navigate to the [repository on GitHub](https://github.com/kvaes/test4)
2. Click the **Code** button
3. Select the **Codespaces** tab
4. Click **Create codespace on main**

The codespace will take a few minutes to set up with all dependencies.

### 2. Verify the Environment

Once the codespace is ready, verify your environment:

```bash
# Check Java version
java -version
# Should show Java 17

# Check Maven version
mvn -version
# Should show Maven 3.6+

# Check Docker availability
docker --version
# Should show Docker version
```

### 3. Build and Run the Application

```bash
# Navigate to the agent directory
cd agent

# Build the application
mvn clean compile

# Run tests
mvn test

# Start the application
mvn spring-boot:run
```

### 4. Test the Application

The application will start on port 8080. Codespaces will automatically forward this port and show a notification with a link to access your application.

```bash
# In a new terminal, test the health endpoint
curl http://localhost:8080/actuator/health
```

## Development Workflow

### Setting Up Environment Variables

1. Create a `.env` file in the root directory:
   ```bash
   touch .env
   ```

2. Add your environment variables:
   ```bash
   echo "OPENAI_API_KEY=your-api-key-here" >> .env
   echo "OPENAI_MODEL=gpt-3.5-turbo" >> .env
   ```

3. Load the environment variables:
   ```bash
   source .env
   ```

### Working with the Code

1. **Open the integrated terminal**: `Ctrl + `` (backtick) or `Cmd + `` on Mac

2. **Use the file explorer** on the left to navigate the codebase

3. **Edit files** directly in the VS Code interface

4. **Run commands** in the integrated terminal

### Live Development

The Spring Boot application supports live reloading:

1. **Start the application with dev profile**:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

2. **Make changes** to your Java files

3. **The application will automatically restart** when you save changes

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ConnectPluginTest

# Run tests with coverage
mvn clean test jacoco:report

# View coverage report
# The report will be generated in target/site/jacoco/index.html
```

### Building Docker Images

```bash
# Build Docker image
mvn jib:dockerBuild

# Run the Docker container
docker run -p 8080:8080 \
  -e OPENAI_API_KEY=$OPENAI_API_KEY \
  -e OPENAI_MODEL=$OPENAI_MODEL \
  bics-semantic-kernel-agent:latest
```

## End-to-End Testing Workflow

### 1. Set Up Multiple Services

In separate terminals within your codespace:

**Terminal 1 - Start the Agent:**
```bash
cd agent
mvn spring-boot:run
```

**Terminal 2 - Mock External APIs (if needed):**
```bash
# You can use tools like json-server or create simple mock servers
npm install -g json-server
echo '{"status": "ok", "message": "Mock API running"}' > db.json
json-server --watch db.json --port 3001
```

**Terminal 3 - Run Integration Tests:**
```bash
cd agent
# Run integration tests against the running application
mvn test -Dtest=**/*IntegrationTest
```

### 2. Test API Interactions

Use the integrated terminal to test API interactions:

```bash
# Test Connect API plugin
curl -X POST http://localhost:8080/api/connect/authenticate \
  -H "Content-Type: application/json" \
  -d '{"clientId": "test", "clientSecret": "test"}'

# Test SMS plugin
curl -X POST http://localhost:8080/api/sms/send \
  -H "Content-Type: application/json" \
  -d '{"to": "+1234567890", "message": "Test message"}'
```

### 3. Debug Issues

1. **Use VS Code debugger**:
   - Set breakpoints in your Java code
   - Press `F5` to start debugging
   - Use the Debug Console to inspect variables

2. **View application logs**:
   ```bash
   # Tail the application logs
   tail -f logs/application.log
   ```

3. **Monitor application metrics**:
   ```bash
   # Check health endpoint
   curl http://localhost:8080/actuator/health

   # Check metrics
   curl http://localhost:8080/actuator/metrics
   ```

## Configuration for Codespaces

### Recommended VS Code Extensions

The following extensions will be automatically installed in your codespace:

- **Extension Pack for Java** - Java development tools
- **Spring Boot Extension Pack** - Spring Boot support
- **Docker** - Docker integration
- **REST Client** - For testing APIs
- **Thunder Client** - Alternative REST client
- **YAML** - YAML syntax support

### Codespace Configuration Files

Create `.devcontainer/devcontainer.json` for custom codespace configuration:

```json
{
  "name": "BICS Semantic Kernel Agent",
  "image": "mcr.microsoft.com/devcontainers/java:17",
  "features": {
    "ghcr.io/devcontainers/features/docker-in-docker:2": {},
    "ghcr.io/devcontainers/features/node:1": {
      "version": "18"
    }
  },
  "forwardPorts": [8080, 3001],
  "postCreateCommand": "cd agent && mvn dependency:resolve",
  "customizations": {
    "vscode": {
      "extensions": [
        "vscjava.vscode-java-pack",
        "pivotal.vscode-spring-boot",
        "ms-vscode.vscode-docker",
        "humao.rest-client"
      ]
    }
  }
}
```

## Working with External APIs

### Testing Against Real BICS APIs

If you have access to BICS API credentials:

1. **Set up your credentials**:
   ```bash
   export BICS_CLIENT_ID=your-client-id
   export BICS_CLIENT_SECRET=your-client-secret
   ```

2. **Update configuration** to point to real endpoints:
   ```yaml
   agent:
     apiUrls:
       connect: "https://api.bics.com/connect"
       mynumbers: "https://api.bics.com/mynumbers"
   ```

3. **Test end-to-end functionality**:
   ```bash
   # Test authentication
   mvn test -Dtest=ConnectPluginIntegrationTest
   ```

### Using Mock Services

For development without real API access:

1. **Create mock responses**:
   ```bash
   mkdir -p mocks
   echo '{"access_token": "mock-token", "expires_in": 3600}' > mocks/auth-response.json
   ```

2. **Start mock server**:
   ```bash
   # Using a simple HTTP server
   cd mocks
   python3 -m http.server 8081
   ```

3. **Update configuration** to use mock endpoints:
   ```yaml
   agent:
     apiUrls:
       connect: "http://localhost:8081"
   ```

## Performance Testing

### Load Testing with Artillery

```bash
# Install Artillery
npm install -g artillery

# Create load test configuration
cat > load-test.yml << EOF
config:
  target: 'http://localhost:8080'
  phases:
    - duration: 60
      arrivalRate: 10
scenarios:
  - name: "Health check"
    requests:
      - get:
          url: "/actuator/health"
EOF

# Run load test
artillery run load-test.yml
```

### Memory and CPU Monitoring

```bash
# Monitor Java process
jps -v

# Get memory usage
jcmd <PID> VM.memory_usage

# Get thread dump if needed
jcmd <PID> Thread.print
```

## Troubleshooting Common Issues

### Port Already in Use

```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### Out of Memory

```bash
# Increase JVM memory
export MAVEN_OPTS="-Xmx2g -Xms1g"
mvn spring-boot:run
```

### Docker Issues

```bash
# Restart Docker service
sudo service docker restart

# Clean up Docker containers
docker system prune -a
```

## Best Practices for Codespaces Development

1. **Commit frequently** - Codespaces are ephemeral, commit your changes often

2. **Use .gitignore** - Exclude temporary files and local configurations

3. **Document setup steps** - Help other developers get started quickly

4. **Use environment variables** - Keep sensitive data out of the codebase

5. **Test in isolation** - Ensure your changes work independently

## Advanced Configurations

### Custom JVM Options

```bash
# Set custom JVM options for development
export JAVA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
mvn spring-boot:run
```

### Database Integration (if needed)

```bash
# Start PostgreSQL in Docker
docker run --name postgres-dev \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=bics_agent \
  -p 5432:5432 \
  -d postgres:13

# Update application.yml for database connection
```

### Redis Integration (for caching)

```bash
# Start Redis in Docker
docker run --name redis-dev \
  -p 6379:6379 \
  -d redis:6-alpine

# Test Redis connection
redis-cli -h localhost ping
```

This setup provides a complete cloud-based development environment where you can develop, test, and debug the BICS Semantic Kernel Agent without any local setup requirements.