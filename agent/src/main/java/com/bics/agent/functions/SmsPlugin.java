package com.bics.agent.functions;

import org.springframework.stereotype.Component;
import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Plugin for the BICS SMS API.
 */
@Component
public class SmsPlugin {
    private static final Logger logger = LoggerFactory.getLogger(SmsPlugin.class);
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    private final String baseUrl;
    private final OkHttpClient httpClient;
    
    public SmsPlugin() {
        this.baseUrl = "https://sms-api.bics.com"; // Default
        this.httpClient = new OkHttpClient();
    }
    
    public SmsPlugin(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = new OkHttpClient();
    }
    
    /**
     * Send an SMS message
     */
    public String sendSms(String accessToken, String to, String message) {
        
        try {
            logger.info("Sending SMS to: {}", to);
            
            String requestBody = String.format(
                    "{\"to\":\"%s\",\"message\":\"%s\"}", 
                    to, message);
            
            Request request = new Request.Builder()
                    .url(baseUrl + "/v1/sms/send")
                    .post(RequestBody.create(requestBody, JSON))
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/json")
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                
                if (!response.isSuccessful()) {
                    logger.error("Failed to send SMS: {} {}", response.code(), responseBody);
                    return String.format("{\"error\":\"Failed to send SMS\",\"status\":%d}", response.code());
                }
                
                logger.info("SMS sent successfully to: {}", to);
                return responseBody;
            }
            
        } catch (IOException e) {
            logger.error("Error sending SMS: {}", e.getMessage(), e);
            return String.format("{\"error\":\"SMS send failed: %s\"}", e.getMessage());
        }
    }
}