package com.beelive.ollama.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient chatClient;
    @Mock
    private ChatClient.CallResponseSpec responseSpec;


    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ChatController chatController = new ChatController(chatClient);
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
    }



    @Test
    void chat_ShouldReturnResponse() throws Exception {
        String expectedResponse = "Dear customer, thank you for reaching out...";

        // Step-by-step stubbing (handles varargs and method chaining cleanly)
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.advisors(any(Advisor[].class))).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/chat").param("message", "Help with refund"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }
}
