package com.beelive.ollama.controller;

import com.beelive.ollama.tools.HelpDeskTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api")
public class HelpDeskController {

    private final HelpDeskTools helpDeskTools;
    private final ChatClient chatClient;

    HelpDeskController(@Qualifier("helpDeskChatClient") ChatClient helpDeskChatClient, HelpDeskTools tools){
        this.chatClient = helpDeskChatClient;
        this.helpDeskTools = tools;
    }


    @GetMapping("/helpdesk")
    public ResponseEntity<String> helpDesk(@RequestHeader("username") String username, @RequestParam("issue") String issue){
      String ans =   chatClient.prompt()
                .advisors( advisorSpec ->  advisorSpec.param( CONVERSATION_ID,username))
                .user(issue)
                .tools(helpDeskTools)
                .toolContext(Map.of("username",username))
                .call().content();
        return ResponseEntity.ok(ans);
    }
}
