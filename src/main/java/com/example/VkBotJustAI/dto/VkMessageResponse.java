package com.example.VkBotJustAI.dto;

import lombok.Data;

@Data
public class VkMessageResponse {
    private Message message;

    @Data
    public static class Message {
        private int id;
        private int date;
        private int from_id;
        private String text;
        private int peer_id;
    }
}
