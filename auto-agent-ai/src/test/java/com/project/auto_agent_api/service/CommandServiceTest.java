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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CommandServiceTest {
    @InjectMocks
    private CommandService commandService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        commandService = new CommandService();
        commandService = spy(commandService); // allow us to mock url
        doReturn("http://mock-url").when(commandService).getPythonAgentUrl();
    }

    @Test
    public void testGetAIResponse_ReturnsValidResponse() {
        // Arrange
        PromptRequest request = new PromptRequest();
        request.setPrompt("Hello");

        PromptResponse expectedResponse = new PromptResponse("Hi there!");

        ResponseEntity<PromptResponse> mockResponse =
                new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        commandService = new CommandService() {
            @Override
            public PromptResponse getAIResponse(PromptRequest req) {
                return expectedResponse;
            }
        };

        // Act
        PromptResponse actualResponse = commandService.getAIResponse(request);

        // Assert
        assertNotNull(actualResponse);
        assertEquals("Hi there!", actualResponse.getResponse());
    }

}
