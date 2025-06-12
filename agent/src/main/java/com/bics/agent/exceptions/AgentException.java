package com.bics.agent.exceptions;

/**
 * Base exception class for all agent-related errors.
 */
public class AgentException extends Exception {
    
    public AgentException(String message) {
        super(message);
    }
    
    public AgentException(String message, Throwable cause) {
        super(message, cause);
    }
}