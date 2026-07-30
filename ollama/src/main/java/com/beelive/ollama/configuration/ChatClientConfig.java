package com.beelive.ollama.configuration;

import com.beelive.ollama.advisor.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
public class ChatClientConfig {

    private final Resource defaultSystemPromptTemplate;

    // Spring automatically injects @Value parameters in constructors
    public ChatClientConfig(@Value("${spring.ai.system.prompt:classpath:/promptTemplates/systemPromptTemplate.st}") Resource defaultSystemPromptTemplate) {
        this.defaultSystemPromptTemplate = defaultSystemPromptTemplate;
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor()))
                .defaultSystem( defaultSystemPromptTemplate)
                .build();
    }

}
