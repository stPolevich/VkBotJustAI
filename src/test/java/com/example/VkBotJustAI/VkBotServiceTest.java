package com.example.VkBotJustAI.service;

import com.example.VkBotJustAI.config.VkConfig;
import com.example.VkBotJustAI.dto.VkMessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VkBotServiceTest {

    private VkBotService vkBotService;
    private VkConfig vkConfigMock;
    private RestTemplate restTemplateMock;

    @BeforeEach
    void setUp() {
        vkConfigMock = mock(VkConfig.class);
        vkBotService = new VkBotService(vkConfigMock);

        // Создаём мок и подставляем его в приватное поле
        restTemplateMock = mock(RestTemplate.class);
        ReflectionTestUtils.setField(vkBotService, "restTemplate", restTemplateMock);
    }

    @Test
    void testProcessMessageSendsCorrectRequest() {
        VkMessageResponse.Message message = new VkMessageResponse.Message();
        message.setFrom_id(12345);
        message.setText("Привет");

        when(vkConfigMock.getAccessToken()).thenReturn("dummyToken");
        when(restTemplateMock.getForObject(anyString(), eq(String.class))).thenReturn("ok");

        String result = vkBotService.processMessage(message);

        verify(restTemplateMock, times(1)).getForObject(contains("dummyToken"), eq(String.class));
        assertEquals("ok", result);
    }

    @Test
    void processMessage_ShouldHandleEmptyMessage() {
        VkMessageResponse.Message message = new VkMessageResponse.Message();
        message.setFrom_id(11111);

        when(vkConfigMock.getAccessToken()).thenReturn("accessToken");
        when(restTemplateMock.getForObject(anyString(), eq(String.class))).thenReturn("ok");

        String result = vkBotService.processMessage(message);

        verify(restTemplateMock).getForObject(argThat((String url) ->
                url.contains("Вы сказали: ") &&
                        url.contains("user_id=11111")
        ), eq(String.class));

        assertEquals("ok", result);
    }

    @Test
    void processMessage_ShouldSendMessageToCorrectUser() {
        VkMessageResponse.Message message = new VkMessageResponse.Message();
        message.setFrom_id(99999);
        message.setText("Привет");

        when(vkConfigMock.getAccessToken()).thenReturn("accessToken");
        when(restTemplateMock.getForObject(anyString(), eq(String.class))).thenReturn("ok");

        vkBotService.processMessage(message);

        verify(restTemplateMock).getForObject(argThat((String url) ->
                url.contains("user_id=99999")
        ), eq(String.class));
    }

    @Test
    void processMessage_ShouldHandleRestTemplateException() {
        VkMessageResponse.Message message = new VkMessageResponse.Message();
        message.setFrom_id(44444);
        message.setText("fail me");

        when(vkConfigMock.getAccessToken()).thenReturn("access");
        when(restTemplateMock.getForObject(anyString(), eq(String.class)))
                .thenThrow(new RuntimeException("VK API error"));

        assertThrows(RuntimeException.class, () -> vkBotService.processMessage(message));
    }
}
