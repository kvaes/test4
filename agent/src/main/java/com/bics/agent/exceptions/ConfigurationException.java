package com.bics.agent.exceptions;

/**
 * Exception thrown when API configuration is invalid or missing.
 */
public class ConfigurationException extends AgentException {
    
    public ConfigurationException(String message) {
        super(message);
    }
    
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}