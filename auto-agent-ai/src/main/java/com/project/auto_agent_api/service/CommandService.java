package com.project.auto_agent_api.service;

import com.project.auto_agent_api.model.PromptRequest;
import com.project.auto_agent_api.model.PromptResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class CommandService {

    private final RestTemplate  restTemplate= new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(CommandService.class);

    @Value("${ai.agent.url}")
    private String pythonAgentUrl;

    public PromptResponse getAIResponse(PromptRequest promptRequest){
        logger.info("Sending prompt: {}", promptRequest.getPrompt());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PromptRequest> entity = new HttpEntity<>(promptRequest, headers);

        ResponseEntity<PromptResponse> response = restTemplate.postForEntity(
                pythonAgentUrl,
                entity,
                PromptResponse.class
        );
        logger.info("Received response: {}", response.getBody().getResponse());
        return response.getBody();
    }

    public void getPythonAgentUrl() {
    }
}
