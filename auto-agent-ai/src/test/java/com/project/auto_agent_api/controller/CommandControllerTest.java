package com.project.auto_agent_api.controller;

import com.project.auto_agent_api.model.PromptRequest;
import com.project.auto_agent_api.model.PromptResponse;
import com.project.auto_agent_api.service.CommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommandController.class)
@Import(CommandControllerTest.MockConfig.class)
public class CommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommandService commandService;

    @BeforeEach
    void setup() {
        reset(commandService);  // reset mock between tests
    }

    @Test
    void shouldReturnAIResponse() throws Exception {
        PromptResponse mockResponse = new PromptResponse("This is a mocked AI response.");
        when(commandService.getAIResponse(any(PromptRequest.class))).thenReturn(mockResponse);

        String json = "{\"prompt\": \"Hello!\"}";

        mockMvc.perform(post("/api/command")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("This is a mocked AI response."));
    }

    @Configuration
    static class MockConfig {
        @Bean
        public CommandService commandService() {
            return Mockito.mock(CommandService.class);
        }
    }
}
