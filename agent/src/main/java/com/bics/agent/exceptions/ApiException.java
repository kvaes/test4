package com.bics.agent.exceptions;

/**
 * Exception thrown when API calls fail.
 */
public class ApiException extends AgentException {
    private final int statusCode;
    private final String response;
    
    public ApiException(String message, int statusCode, String response) {
        super(message);
        this.statusCode = statusCode;
        this.response = response;
    }
    
    public ApiException(String message, int statusCode, String response, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.response = response;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getResponse() {
        return response;
    }
}