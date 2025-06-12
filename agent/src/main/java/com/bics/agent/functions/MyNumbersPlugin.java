package com.bics.agent.functions;

import com.bics.agent.exceptions.ApiException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Plugin for the BICS MyNumbers API.
 * Provides number management and provisioning services.
 */
@Component
public class MyNumbersPlugin {
    private static final Logger logger = LoggerFactory.getLogger(MyNumbersPlugin.class);
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    private final String baseUrl;
    private final OkHttpClient httpClient;
    
    public MyNumbersPlugin() {
        this.baseUrl = "https://mynumbers-api.bics.com"; // Default, will be configurable
        this.httpClient = new OkHttpClient();
    }
    
    public MyNumbersPlugin(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = new OkHttpClient();
    }
    
    /**
     * Get list of available numbers
     */
    public String getNumbers(String accessToken, String countryCode) {
        
        try {
            logger.info("Getting available numbers for country: {}", countryCode);
            
            HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/v1/numbers").newBuilder();
            if (countryCode != null && !countryCode.isEmpty()) {
                urlBuilder.addQueryParameter("country_code", countryCode);
            }
            
            Request request = new Request.Builder()
                    .url(urlBuilder.build())
                    .get()
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                
                if (!response.isSuccessful()) {
                    throw new ApiException("Failed to get numbers", response.code(), responseBody);
                }
                
                logger.info("Successfully retrieved numbers for country: {}", countryCode);
                return responseBody;
            }
            
        } catch (IOException e) {
            logger.error("Error getting numbers: {}", e.getMessage(), e);
            throw new RuntimeException("Get numbers request failed: " + e.getMessage(), e);
        } catch (ApiException e) {
            logger.error("API error getting numbers: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * Reserve a specific number
     */
    public String reserveNumber(String accessToken, String phoneNumber) {
        
        try {
            logger.info("Reserving number: {}", phoneNumber);
            
            String requestBody = String.format("{\"phone_number\":\"%s\"}", phoneNumber);
            
            Request request = new Request.Builder()
                    .url(baseUrl + "/v1/numbers/reserve")
                    .post(RequestBody.create(requestBody, JSON))
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/json")
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                
                if (!response.isSuccessful()) {
                    throw new ApiException("Failed to reserve number", response.code(), responseBody);
                }
                
                logger.info("Successfully reserved number: {}", phoneNumber);
                return responseBody;
            }
            
        } catch (IOException e) {
            logger.error("Error reserving number: {}", e.getMessage(), e);
            throw new RuntimeException("Reserve number request failed: " + e.getMessage(), e);
        } catch (ApiException e) {
            logger.error("API error reserving number: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}