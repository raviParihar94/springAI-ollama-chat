package com.beelive.ollama.configuration;

import com.beelive.ollama.advisor.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClientBuilderCustomizer;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientBuilderCustomizerConfig {

    @Bean
    ChatClientBuilderCustomizer loggerAdvisor(){
        return builder -> builder.defaultAdvisors(new SimpleLoggerAdvisor());
    }
    @Bean
    @ConditionalOnProperty(name = "audit.token-usage.enabled", havingValue = "true")
    public ChatClientBuilderCustomizer tokenUsageAuditAdvisor() {
        return builder -> builder.defaultAdvisors(new TokenUsageAuditAdvisor());
    }
}
