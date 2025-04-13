package com.project.auto_agent_api.api.dto;

import com.project.auto_agent_api.core.domain.CommandType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CommandResponse {
    private String response;
    private CommandType type;
    private Map<String, Object> metadata;
    private List<ActionItem> actionItems;
    
    @Data
    public static class ActionItem {
        private String description;
        private String status;
        private Map<String, Object> details;
    }
} 