package com.bics.agent.config;

import com.bics.agent.exceptions.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Map;

/**
 * Configuration management for the BICS Semantic Kernel Agent.
 */
@Component
@ConfigurationProperties(prefix = "agent")
public class AgentConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(AgentConfiguration.class);
    
    private Map<String, String> apiUrls;
    private String openAiApiKey;
    private String openAiModel;
    
    @PostConstruct
    public void validate() throws ConfigurationException {
        if (apiUrls == null || apiUrls.isEmpty()) {
            logger.warn("API URLs configuration is missing or empty");
        }
        
        logger.info("Configuration loaded successfully");
    }
    
    // Getters and setters
    public Map<String, String> getApiUrls() {
        return apiUrls;
    }
    
    public void setApiUrls(Map<String, String> apiUrls) {
        this.apiUrls = apiUrls;
    }
    
    public String getOpenAiApiKey() {
        return openAiApiKey;
    }
    
    public void setOpenAiApiKey(String openAiApiKey) {
        this.openAiApiKey = openAiApiKey;
    }
    
    public String getOpenAiModel() {
        return openAiModel;
    }
    
    public void setOpenAiModel(String openAiModel) {
        this.openAiModel = openAiModel;
    }
    
    public String getApiUrl(String apiName) {
        return apiUrls != null ? apiUrls.get(apiName) : null;
    }
}