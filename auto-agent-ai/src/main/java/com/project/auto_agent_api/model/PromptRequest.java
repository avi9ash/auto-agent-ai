package com.project.auto_agent_api.model;


import jakarta.validation.constraints.NotBlank;

public class PromptRequest {

    @NotBlank(message = "Prompt cannot be blank")
    String prompt;

    public PromptRequest() {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
