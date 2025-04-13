package com.project.auto_agent_api.service;

import com.project.auto_agent_api.model.PromptRequest;
import com.project.auto_agent_api.model.PromptResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CommandServiceTest {
    @InjectMocks
    private CommandService commandService;

    @Mock
    private RestTemplate restTemplate;

    private static final String MOCK_URL = "http://localhost:5050/prompt";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Use reflection to set the private field
        try {
            java.lang.reflect.Field field = CommandService.class.getDeclaredField("pythonAgentUrl");
            field.setAccessible(true);
            field.set(commandService, MOCK_URL);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set pythonAgentUrl", e);
        }
    }

    @Test
    public void testGetAIResponse_ReturnsValidResponse() {
        // Arrange
        PromptRequest request = new PromptRequest();
        request.setPrompt("Hello");

        PromptResponse expectedResponse = new PromptResponse("Hi there!");
        ResponseEntity<PromptResponse> mockResponse = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(
            eq(MOCK_URL),
            any(),
            eq(PromptResponse.class)
        )).thenReturn(mockResponse);

        // Act
        PromptResponse actualResponse = commandService.getAIResponse(request);

        // Assert
        assertNotNull(actualResponse, "Response should not be null");
        assertNotNull(actualResponse.getResponse(), "Response text should not be null");
    }
}
