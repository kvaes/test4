package com.bics.agent.functions;

import com.bics.agent.exceptions.ApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Plugin for the BICS Connect API.
 * Provides connectivity and authentication services.
 */
@Component
public class ConnectPlugin {
    private static final Logger logger = LoggerFactory.getLogger(ConnectPlugin.class);
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    private final String baseUrl;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public ConnectPlugin() {
        this.baseUrl = "https://connect-api.bics.com"; // Default, will be configurable
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }
    
    public ConnectPlugin(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Authenticate with the BICS Connect API
     */
    public String authenticate(String clientId, String clientSecret) {
        
        try {
            logger.info("Authenticating with Connect API for client: {}", clientId);
            
            String requestBody = String.format(
                    "{\"client_id\":\"%s\",\"client_secret\":\"%s\",\"grant_type\":\"client_credentials\"}", 
                    clientId, clientSecret);
            
            Request request = new Request.Builder()
                    .url(baseUrl + "/oauth/token")
                    .post(RequestBody.create(requestBody, JSON))
                    .addHeader("Content-Type", "application/json")
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                
                if (!response.isSuccessful()) {
                    throw new ApiException("Authentication failed", response.code(), responseBody);
                }
                
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String accessToken = jsonNode.get("access_token").asText();
                
                logger.info("Authentication successful for client: {}", clientId);
                return accessToken;
            }
            
        } catch (IOException e) {
            logger.error("Error during authentication: {}", e.getMessage(), e);
            throw new RuntimeException("Authentication request failed: " + e.getMessage(), e);
        } catch (ApiException e) {
            logger.error("API error during authentication: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * Get the status of the Connect API
     */
    public String getStatus() {
        try {
            logger.info("Getting Connect API status");
            
            Request request = new Request.Builder()
                    .url(baseUrl + "/status")
                    .get()
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                
                if (!response.isSuccessful()) {
                    throw new ApiException("Failed to get API status", response.code(), responseBody);
                }
                
                logger.info("Connect API status retrieved successfully");
                return responseBody;
            }
            
        } catch (IOException e) {
            logger.error("Error getting API status: {}", e.getMessage(), e);
            throw new RuntimeException("Status request failed: " + e.getMessage(), e);
        } catch (ApiException e) {
            logger.error("API error getting status: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * Validate an access token
     */
    public String validateToken(String accessToken) {
        
        try {
            logger.info("Validating access token");
            
            Request request = new Request.Builder()
                    .url(baseUrl + "/oauth/validate")
                    .get()
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                
                if (!response.isSuccessful()) {
                    throw new ApiException("Token validation failed", response.code(), responseBody);
                }
                
                logger.info("Token validation successful");
                return responseBody;
            }
            
        } catch (IOException e) {
            logger.error("Error during token validation: {}", e.getMessage(), e);
            throw new RuntimeException("Token validation request failed: " + e.getMessage(), e);
        } catch (ApiException e) {
            logger.error("API error during token validation: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}