package com.example.VkBotJustAI.controller;

import com.example.VkBotJustAI.config.VkConfig;
import com.example.VkBotJustAI.dto.VkCallback;
import com.example.VkBotJustAI.dto.VkMessageResponse;
import com.example.VkBotJustAI.service.VkBotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VkBotControllerTest {

    private VkBotService vkBotService;
    private VkConfig vkConfig;
    private VkBotController controller;

    @BeforeEach
    void setup() {
        vkBotService = mock(VkBotService.class);
        vkConfig = mock(VkConfig.class);
        controller = new VkBotController(vkBotService, vkConfig);
    }

    @Test
    void testInvalidSecretKey() {
        VkCallback callback = new VkCallback();
        callback.setSecret("wrongSecret");

        when(vkConfig.getSecretKey()).thenReturn("expectedSecret");

        ResponseEntity<String> response = controller.handleCallback(callback);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("invalid secret code", response.getBody());
    }

    @Test
    void testConfirmationType() {
        VkCallback callback = new VkCallback();
        callback.setSecret("expectedSecret");
        callback.setType("confirmation");

        when(vkConfig.getSecretKey()).thenReturn("expectedSecret");
        when(vkConfig.getConfirmationCode()).thenReturn("confirmation_code");

        ResponseEntity<String> response = controller.handleCallback(callback);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("confirmation_code", response.getBody());
    }

    @Test
    void testMessageProcessing() {
        VkCallback callback = new VkCallback();
        callback.setSecret("expectedSecret");
        callback.setType("message_new");

        VkMessageResponse.Message message = new VkMessageResponse.Message();
        message.setText("привет");
        message.setFrom_id(12345);

        VkMessageResponse object = new VkMessageResponse();
        object.setMessage(message);
        callback.setObject(object);

        when(vkConfig.getSecretKey()).thenReturn("expectedSecret");
        when(vkBotService.processMessage(any())).thenReturn("ok");

        ResponseEntity<String> response = controller.handleCallback(callback);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ok", response.getBody());
    }

}
