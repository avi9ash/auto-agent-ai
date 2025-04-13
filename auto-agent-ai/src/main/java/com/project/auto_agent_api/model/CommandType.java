package com.project.auto_agent_api.model;

public enum CommandType {
    SUMMARIZE("Summarize the given text or content"),
    SCHEDULE("Schedule an event or task"),
    GENERAL("General conversation or query"),
    ANALYZE("Analyze data or information"),
    REMINDER("Set or manage reminders"),
    SEARCH("Search for information");

    private final String description;

    CommandType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 