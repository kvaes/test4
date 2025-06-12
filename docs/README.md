# BICS Semantic Kernel Agent Documentation

Welcome to the BICS Semantic Kernel Agent documentation. This Java-based application provides AI-powered access to various BICS APIs through a modern, plugin-based architecture.

## Table of Contents

- [Quick Start Guide](quickstart.md)
- [Local Development Setup](development.md)
- [GitHub Codespaces Setup](codespaces.md)
- [API Reference](api-reference.md)
- [Plugin Development](plugin-development.md)
- [Configuration Guide](configuration.md)
- [Deployment Guide](deployment.md)
- [Troubleshooting](troubleshooting.md)

## Overview

The BICS Semantic Kernel Agent is built with:

- **Spring Boot 3.2** for enterprise-grade application framework
- **Java 17** for modern language features and performance
- **Maven** for dependency management and build automation
- **Docker** for containerization and deployment
- **OkHttp** for robust HTTP client functionality
- **JUnit 5** for comprehensive testing

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│                 │    │                 │    │                 │
│   Application   │───▶│ BicsSemanticAgent│───▶│  Plugin Layer   │
│     (Main)      │    │    (Service)    │    │   (Functions)   │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │                 │    │                 │
                       │  Configuration  │    │   BICS APIs     │
                       │   Management    │    │   (External)    │
                       │                 │    │                 │
                       └─────────────────┘    └─────────────────┘
```

## Available Plugins

| Plugin | Description | Status |
|--------|-------------|---------|
| **ConnectPlugin** | Authentication and connectivity | ✅ Implemented |
| **MyNumbersPlugin** | Number management and provisioning | ✅ Implemented |
| **MyNumbersAddressManagementPlugin** | Address-related operations | 🚧 Basic structure |
| **MyNumbersCDRPlugin** | Call detail records | 🚧 Basic structure |
| **MyNumbersDisconnectionPlugin** | Number disconnection services | 🚧 Basic structure |
| **MyNumbersEmergencyServicesPlugin** | Emergency service configuration | 🚧 Basic structure |
| **MyNumbersNumberPortingPlugin** | Number porting operations | 🚧 Basic structure |
| **SmsPlugin** | SMS messaging services | ✅ Implemented |

## Getting Started

### For Developers

If you're new to the project, start with:

1. [Quick Start Guide](quickstart.md) - Get up and running in 5 minutes
2. [Local Development Setup](development.md) - Set up your development environment
3. [Plugin Development](plugin-development.md) - Learn how to create new plugins

### For DevOps/Deployment

If you're deploying the application:

1. [Configuration Guide](configuration.md) - Understand all configuration options
2. [Deployment Guide](deployment.md) - Deploy using Docker or Kubernetes
3. [Troubleshooting](troubleshooting.md) - Common issues and solutions

### For GitHub Codespaces Users

If you want to develop in the cloud:

1. [GitHub Codespaces Setup](codespaces.md) - Complete cloud development environment

## Core Concepts

### Plugin Architecture

The agent uses a plugin-based architecture where each BICS API is represented by a dedicated plugin. This provides:

- **Modularity**: Each API can be developed and maintained independently
- **Testability**: Plugins can be unit tested in isolation
- **Extensibility**: New APIs can be added without changing core code
- **Configuration**: Each plugin can have its own configuration

### Error Handling

The application implements a comprehensive error handling strategy:

```java
try {
    // API call
    String result = plugin.callApi(parameters);
    return result;
} catch (ApiException e) {
    logger.error("API error: {}", e.getMessage(), e);
    throw new RuntimeException(e.getMessage(), e);
} catch (IOException e) {
    logger.error("Network error: {}", e.getMessage(), e);
    throw new RuntimeException("Network request failed: " + e.getMessage(), e);
}
```

### Configuration Management

Configuration is managed through Spring Boot's configuration system:

- **YAML files** for structured configuration
- **Environment variables** for runtime configuration
- **Profiles** for environment-specific settings
- **Validation** for required configuration values

## Best Practices

### Development

- Write unit tests for all new functionality
- Use meaningful commit messages
- Follow Java naming conventions
- Document public APIs with Javadoc
- Use dependency injection where appropriate

### Security

- Never commit secrets to version control
- Use environment variables for sensitive data
- Validate all input parameters
- Log security-relevant events
- Keep dependencies up to date

### Performance

- Use connection pooling for HTTP clients
- Implement proper caching where appropriate
- Monitor application metrics
- Use async processing for long-running operations

## Support

- **Documentation**: Check this documentation for detailed guides
- **Issues**: Create GitHub issues for bugs or feature requests
- **Discussions**: Use GitHub Discussions for questions and ideas
- **Email**: Contact dev@bics.com for direct support

## Contributing

We welcome contributions! Please see our [Contributing Guide](../CONTRIBUTING.md) for details on:

- Code of conduct
- Development process
- Pull request guidelines
- Testing requirements