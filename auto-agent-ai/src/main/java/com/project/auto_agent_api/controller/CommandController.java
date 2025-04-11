package com.project.auto_agent_api.controller;

import com.project.auto_agent_api.model.PromptRequest;
import com.project.auto_agent_api.model.PromptResponse;
import com.project.auto_agent_api.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CommandController {

    @Autowired
    CommandService commandService;

    @PostMapping("/command")
    public PromptResponse handleCommand(@RequestBody PromptRequest request) {
        return commandService.getAIResponse(request);
    }

}
