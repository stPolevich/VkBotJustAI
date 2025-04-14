package com.example.VkBotJustAI.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class VkConfig {
    @Value("${vk.access-token}")
    private String accessToken;

    @Value("${vk.group-id}")
    private Integer groupId;

    @Value("${vk.confirmation-code}")
    private String confirmationCode;

    @Value("${vk.secret-key}")
    private String secretKey;
}