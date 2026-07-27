package com.beelive.ollama.controller;

import com.beelive.ollama.advisor.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class ChatController {


    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient){
        this.chatClient= chatClient;
    }


    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message ){
        String response  = chatClient.
                prompt()
                .options(OllamaChatOptions.builder().minP(0.8))
                .system(
                        """
You are a professional customer service assistant which helps drafting email responses to improve the productivity of the customer support team
"""
                )
                .call().content();
        return  response;

    }
}
