# Development Guide

This guide provides comprehensive information for developers working on the BICS Semantic Kernel Agent.

## Prerequisites

Before you begin development, ensure you have the following installed:

### Required Software

- **Java 17 or higher**
  ```bash
  # Check Java version
  java -version
  
  # If using SDKMAN (recommended)
  sdk install java 17.0.8-tem
  sdk use java 17.0.8-tem
  ```

- **Maven 3.6 or higher**
  ```bash
  # Check Maven version
  mvn -version
  
  # If using SDKMAN
  sdk install maven 3.9.5
  ```

- **Git**
  ```bash
  git --version
  ```

- **Docker** (optional, for containerization)
  ```bash
  docker --version
  ```

### Recommended Tools

- **IntelliJ IDEA** or **VS Code** with Java extensions
- **Postman** or **Thunder Client** for API testing
- **Docker Desktop** for container management

## Project Setup

### 1. Clone the Repository

```bash
git clone https://github.com/kvaes/test4.git
cd test4/agent
```

### 2. Environment Configuration

Create a local environment configuration:

```bash
# Create local environment file
cp src/main/resources/application.yml src/main/resources/application-local.yml

# Edit the local configuration
nano src/main/resources/application-local.yml
```

Update the configuration with your settings:

```yaml
agent:
  apiUrls:
    connect: "https://connect-api-dev.bics.com"
    mynumbers: "https://mynumbers-api-dev.bics.com"
    # Use development endpoints for local testing
  openAiApiKey: ${OPENAI_API_KEY:your-dev-api-key}
  openAiModel: ${OPENAI_MODEL:gpt-3.5-turbo}

logging:
  level:
    com.bics.agent: DEBUG
    org.springframework: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
```

### 3. Set Environment Variables

```bash
# Create a .env file for local development
cat > .env << EOF
OPENAI_API_KEY=your-openai-api-key-here
OPENAI_MODEL=gpt-3.5-turbo
SPRING_PROFILES_ACTIVE=local
EOF

# Load environment variables
source .env
```

### 4. Build and Verify Setup

```bash
# Clean and compile
mvn clean compile

# Run tests to verify setup
mvn test

# Start the application
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## Development Workflow

### Code Organization

The project follows a standard Spring Boot structure:

```
agent/src/main/java/com/bics/agent/
├── Application.java                 # Main application entry point
├── BicsSemanticAgent.java           # Core service orchestrator
├── config/
│   └── AgentConfiguration.java     # Configuration management
├── exceptions/
│   ├── AgentException.java         # Base exception
│   ├── ApiException.java           # API-specific exceptions
│   └── ConfigurationException.java # Configuration exceptions
├── functions/                       # API plugin implementations
│   ├── ConnectPlugin.java
│   ├── MyNumbersPlugin.java
│   ├── SmsPlugin.java
│   └── ...
└── models/                         # Data transfer objects
    └── AuthenticationResponse.java
```

### Creating New API Plugins

Follow these steps to add a new API plugin:

#### 1. Create the Plugin Class

```java
package com.bics.agent.functions;

import com.bics.agent.exceptions.ApiException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NewApiPlugin {
    private static final Logger logger = LoggerFactory.getLogger(NewApiPlugin.class);
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    private final String baseUrl;
    private final OkHttpClient httpClient;
    
    public NewApiPlugin() {
        this.baseUrl = "https://new-api.bics.com"; // Default URL
        this.httpClient = new OkHttpClient();
    }
    
    public NewApiPlugin(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = new OkHttpClient();
    }
    
    /**
     * Example API method with proper error handling
     */
    public String getSomething(String accessToken, String param) {
        try {
            logger.info("Getting something with param: {}", param);
            
            Request request = new Request.Builder()
                    .url(baseUrl + "/v1/something")
                    .get()
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/json")
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                
                if (!response.isSuccessful()) {
                    throw new ApiException("Failed to get something", response.code(), responseBody);
                }
                
                logger.info("Successfully retrieved something");
                return responseBody;
            }
            
        } catch (IOException e) {
            logger.error("Error getting something: {}", e.getMessage(), e);
            throw new RuntimeException("Request failed: " + e.getMessage(), e);
        } catch (ApiException e) {
            logger.error("API error getting something: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
```

#### 2. Add Configuration

Update `application.yml`:

```yaml
agent:
  apiUrls:
    # ... existing URLs
    new-api: "https://new-api.bics.com"
```

#### 3. Register the Plugin

Update `BicsSemanticAgent.java`:

```java
@Autowired
private NewApiPlugin newApiPlugin;

@PostConstruct
public void initialize() {
    logger.info("BICS Semantic Agent initialized with {} API plugins", 9); // Update count
}
```

#### 4. Create Unit Tests

```java
package com.bics.agent.functions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NewApiPlugin Tests")
class NewApiPluginTest {
    
    private NewApiPlugin plugin;
    
    @BeforeEach
    void setUp() {
        plugin = new NewApiPlugin("https://test-api.example.com");
    }
    
    @Test
    @DisplayName("Should create plugin with default constructor")
    void testDefaultConstructor() {
        NewApiPlugin defaultPlugin = new NewApiPlugin();
        assertNotNull(defaultPlugin);
    }
    
    @Test
    @DisplayName("Should handle API errors gracefully")
    void testApiErrorHandling() {
        // Test error handling
        assertThrows(RuntimeException.class, () -> {
            plugin.getSomething("invalid-token", "test-param");
        });
    }
}
```

### Testing Strategy

#### Unit Tests

- **Location**: `src/test/java/com/bics/agent/`
- **Framework**: JUnit 5 with Mockito
- **Naming**: `ClassNameTest.java`

```bash
# Run all unit tests
mvn test

# Run specific test class
mvn test -Dtest=ConnectPluginTest

# Run tests with specific method
mvn test -Dtest=ConnectPluginTest#testAuthentication
```

#### Integration Tests

Create integration tests for end-to-end scenarios:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class BicsSemanticAgentIntegrationTest {
    
    @Autowired
    private BicsSemanticAgent agent;
    
    @Test
    void testAgentInitialization() {
        assertNotNull(agent);
    }
}
```

#### Test Configuration

Create `src/test/resources/application-test.yml`:

```yaml
agent:
  apiUrls:
    connect: "http://localhost:8089/mock/connect"
    mynumbers: "http://localhost:8089/mock/mynumbers"
  openAiApiKey: "test-key"
  openAiModel: "gpt-3.5-turbo"

logging:
  level:
    com.bics.agent: DEBUG
```

### Code Quality

#### Code Formatting

Use Google Java Style:

```xml
<!-- Add to pom.xml -->
<plugin>
    <groupId>com.spotify.fmt</groupId>
    <artifactId>fmt-maven-plugin</artifactId>
    <version>2.21</version>
    <executions>
        <execution>
            <goals>
                <goal>format</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

```bash
# Format code
mvn fmt:format

# Check formatting
mvn fmt:check
```

#### Static Analysis

Use SpotBugs for static analysis:

```xml
<!-- Add to pom.xml -->
<plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
    <version>4.7.3.0</version>
</plugin>
```

```bash
# Run static analysis
mvn spotbugs:check
```

#### Test Coverage

Use JaCoCo for test coverage:

```xml
<!-- Add to pom.xml -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

```bash
# Generate coverage report
mvn clean test jacoco:report

# View report
open target/site/jacoco/index.html
```

### Debugging

#### Local Debugging

1. **Start with remote debugging enabled**:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
   ```

2. **Connect your IDE** to `localhost:5005`

3. **Set breakpoints** and debug as usual

#### Docker Debugging

```bash
# Build debug image
docker build -t bics-agent-debug --target debug .

# Run with debug port exposed
docker run -p 8080:8080 -p 5005:5005 \
  -e JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" \
  bics-agent-debug
```

### Performance Optimization

#### JVM Tuning

For development:
```bash
export MAVEN_OPTS="-Xmx2g -Xms1g -XX:+UseG1GC"
```

For production:
```bash
export JAVA_OPTS="-Xmx4g -Xms2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

#### HTTP Client Optimization

Configure OkHttp for better performance:

```java
private OkHttpClient createHttpClient() {
    return new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
            .build();
}
```

### Monitoring and Observability

#### Application Metrics

Add actuator endpoints:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Enable metrics:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,info,env
  endpoint:
    health:
      show-details: always
```

#### Custom Metrics

```java
@Component
public class PluginMetrics {
    private final MeterRegistry meterRegistry;
    private final Counter apiCallCounter;
    
    public PluginMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.apiCallCounter = Counter.builder("bics.api.calls")
                .description("Number of API calls")
                .register(meterRegistry);
    }
    
    public void incrementApiCall(String plugin, String endpoint) {
        apiCallCounter.increment(
            Tags.of("plugin", plugin, "endpoint", endpoint)
        );
    }
}
```

### Documentation

#### Javadoc

Write comprehensive Javadoc for public APIs:

```java
/**
 * Authenticates with the BICS Connect API using client credentials.
 * 
 * @param clientId the client identifier for authentication
 * @param clientSecret the client secret for authentication
 * @return the access token for subsequent API calls
 * @throws RuntimeException if authentication fails
 */
public String authenticate(String clientId, String clientSecret) {
    // Implementation
}
```

Generate documentation:
```bash
mvn javadoc:javadoc
```

#### API Documentation

Use OpenAPI/Swagger for REST endpoints:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

Access at: `http://localhost:8080/swagger-ui.html`

### Best Practices

#### Error Handling

1. **Use custom exceptions** for different error types
2. **Log errors** with context information
3. **Provide meaningful error messages** to users
4. **Don't expose internal details** in error responses

#### Security

1. **Never log sensitive data** like API keys or passwords
2. **Validate all input parameters**
3. **Use HTTPS** for all external communications
4. **Keep dependencies up to date**

#### Performance

1. **Use connection pooling** for HTTP clients
2. **Implement caching** where appropriate
3. **Use async processing** for long-running operations
4. **Monitor resource usage**

#### Code Organization

1. **Follow Single Responsibility Principle**
2. **Use dependency injection** instead of static dependencies
3. **Write tests first** (TDD approach)
4. **Keep methods small and focused**

This development guide provides the foundation for productive development on the BICS Semantic Kernel Agent. Follow these practices to ensure code quality, maintainability, and reliability.