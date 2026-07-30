package com.beelive.ollama.configuration;

import com.beelive.ollama.advisor.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class HelpDeskChatClientConfig {

    @Bean("helpDeskChatClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory){
       Advisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        Advisor tokenUsageAuditAdvisor =new TokenUsageAuditAdvisor();
       Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return chatClientBuilder
            .defaultTools().
            defaultAdvisors(List.of(simpleLoggerAdvisor,memoryAdvisor,tokenUsageAuditAdvisor))
            .build();
    }
}
