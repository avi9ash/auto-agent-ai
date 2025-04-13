package com.project.auto_agent_api.controller;

import com.project.auto_agent_api.model.PromptRequest;
import com.project.auto_agent_api.model.PromptResponse;
import com.project.auto_agent_api.service.CommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommandController.class)
public class CommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandService commandService;

    @BeforeEach
    void setup() {
        reset(commandService);  // reset mock between tests
    }

    @Test
    void shouldReturnAIResponse() throws Exception {
        // Given
        PromptResponse mockResponse = new PromptResponse("This is a mocked AI response.");
        when(commandService.getAIResponse(any(PromptRequest.class))).thenReturn(mockResponse);

        // When/Then
        mockMvc.perform(post("/api/command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"prompt\": \"Hello!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").exists())
                .andExpect(jsonPath("$.response").isString());
    }

    @Test
    void shouldHandleEmptyPrompt() throws Exception {
        // Given
        PromptResponse mockResponse = new PromptResponse("Please provide a prompt.");
        when(commandService.getAIResponse(any(PromptRequest.class))).thenReturn(mockResponse);

        // When/Then
        mockMvc.perform(post("/api/command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"prompt\": \"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").exists())
                .andExpect(jsonPath("$.response").isString());
    }

    @Test
    void shouldHandleInvalidJson() throws Exception {
        // When/Then
        mockMvc.perform(post("/api/command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest());
    }
}
