package com.project.auto_agent_api.api.dto;

import com.project.auto_agent_api.core.domain.CommandType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class CommandRequest {
    @NotBlank(message = "Prompt cannot be empty")
    private String prompt;
    
    private CommandType type = CommandType.GENERAL;
    
    private Map<String, Object> context;
    
    private Map<String, Object> metadata;
} 