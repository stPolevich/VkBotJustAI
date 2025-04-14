package com.example.VkBotJustAI.dto;

import lombok.Data;

@Data
public class VkCallback {
    private String type;
    private VkMessageResponse object;
    private Integer group_id;
    private String secret;
}
