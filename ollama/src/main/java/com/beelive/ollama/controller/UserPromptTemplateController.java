package com.beelive.ollama.controller;

import org.springframework.core.io.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserPromptTemplateController {
    private final ChatClient chatClient;

    @Value("classpath:/templates/userPromptTemplate.st")
    Resource userPromptTemplate;

    public UserPromptTemplateController(ChatClient chatClient){
        this.chatClient= chatClient;
    }


    @GetMapping("/email")
    public String emailResponse(@RequestParam("customerName") String customerName, @RequestParam("customerName") String customerMessage ) {
        String response = chatClient.
                prompt()
                .system(userPromptTemplate)
                .user(promptTemplateSpec ->
                                promptTemplateSpec.text(userPromptTemplate)
                                        .param("customerName", customerName)
                                        .param("customerMessage", customerMessage)
                        )
                .call().content();
        return response;

    }

}
