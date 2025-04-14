package com.example.VkBotJustAI.controller;

import com.example.VkBotJustAI.config.VkConfig;
import com.example.VkBotJustAI.dto.VkCallback;
import com.example.VkBotJustAI.service.VkBotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/vk")
public class VkBotController {

    private final VkBotService vkBotService;
    private final VkConfig vkConfig;

    public VkBotController(VkBotService vkBotService, VkConfig vkConfig) {
        this.vkBotService = vkBotService;
        this.vkConfig = vkConfig;
    }

    @PostMapping(value = "/callback", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> handleCallback(@RequestBody VkCallback callback) {
        // Проверка секретного ключа
        if (!vkConfig.getSecretKey().equals(callback.getSecret())) {
            return ResponseEntity.badRequest().body("invalid secret code");
        }

        // Подтверждение сервера
        if ("confirmation".equals(callback.getType())) {
            return ResponseEntity.ok(vkConfig.getConfirmationCode());
        }

        // Обработка сообщений
        return ResponseEntity.ok(vkBotService.processMessage(callback.getObject().getMessage()));
    }
}
