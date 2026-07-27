package com.beelive.ollama.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ChatMemoryController {

    private final ChatClient chatClient;

    public ChatMemoryController(@Qualifier("chat-memory-chatClient") ChatClient chatClient){
        this.chatClient = chatClient;
    }

    @GetMapping("/chatMemory")
    public ResponseEntity<String> chat(
            @RequestParam("message") String message,
            @RequestParam(value = "conversationId", defaultValue = "default-chat") String conversationId) {

        String response = chatClient.prompt()
                .user(message)
                .advisors(a -> a.param("chat_memory_conversation_id", conversationId))
                .call()
                .content();

        return ResponseEntity.ok(response);
    }
}