package com.beelive.ollama.config;

import com.beelive.ollama.advisor.TokenUsageAuditAdvisor;
import com.beelive.ollama.configuration.ChatClientConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;

import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatClientConfigTest {

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private ChatClient mockChatClient;

    @Captor
    private ArgumentCaptor<List<Advisor>> advisorsCaptor;

    private ChatClientConfig chatClientConfig;
    private Resource mockSystemPromptResource;

    @BeforeEach
    void setUp() {
        mockSystemPromptResource = new ByteArrayResource(
                "You are an internal assistant.".getBytes(StandardCharsets.UTF_8)
        );
        chatClientConfig = new ChatClientConfig(mockSystemPromptResource);
    }

    @Test
    @DisplayName("Should successfully construct ChatClient bean with default advisors and system prompt resource")
    void chatClient_ShouldConfigureDefaultAdvisorsAndSystemPrompt() {
        // Given
        when(chatClientBuilder.defaultAdvisors(anyList())).thenReturn(chatClientBuilder);
        when(chatClientBuilder.defaultSystem(any(Resource.class))).thenReturn(chatClientBuilder);
        when(chatClientBuilder.build()).thenReturn(mockChatClient);

        // When
        ChatClient actualChatClient = chatClientConfig.chatClient(chatClientBuilder);

        // Then
        assertThat(actualChatClient)
                .as("Configured ChatClient bean should match the builder result")
                .isNotNull()
                .isSameAs(mockChatClient);

        // Verify exact builder method chain and parameter values
        verify(chatClientBuilder).defaultAdvisors(advisorsCaptor.capture());
        verify(chatClientBuilder).defaultSystem(mockSystemPromptResource);
        verify(chatClientBuilder).build();
        verifyNoMoreInteractions(chatClientBuilder);

        // Verify registered Advisors match expected types and count
        List<Advisor> capturedAdvisors = advisorsCaptor.getValue();
        assertThat(capturedAdvisors)
                .hasSize(2)
                .hasAtLeastOneElementOfType(SimpleLoggerAdvisor.class)
                .hasAtLeastOneElementOfType(TokenUsageAuditAdvisor.class);
    }
}