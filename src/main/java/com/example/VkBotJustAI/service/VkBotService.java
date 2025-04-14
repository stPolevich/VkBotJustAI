package com.example.VkBotJustAI.service;

import com.example.VkBotJustAI.config.VkConfig;
import com.example.VkBotJustAI.dto.VkMessageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VkBotService {

    private final VkConfig vkConfig;
    private final RestTemplate restTemplate;

    public VkBotService(VkConfig vkConfig) {
        this.vkConfig = vkConfig;
        this.restTemplate = new RestTemplate();
    }

    public String processMessage(VkMessageResponse.Message message) {

        String responseText = "Вы сказали: " + message.getText();
        sendReply(message.getFrom_id(), responseText);
        return "ok";
    }

    private void sendReply(int userId, String text) {
        String url = String.format(
                "https://api.vk.com/method/messages.send?access_token=%s&user_id=%d&message=%s&v=5.199&random_id=0",
                vkConfig.getAccessToken(),
                userId,
                text
        );
        restTemplate.getForObject(url, String.class);
    }
}
