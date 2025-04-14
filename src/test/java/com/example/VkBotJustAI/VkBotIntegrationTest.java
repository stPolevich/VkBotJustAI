package com.example.VkBotJustAI;

import com.example.VkBotJustAI.config.VkConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VkBotIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private VkConfig vkConfig;

    @Test
    void callbackEndpoint_ShouldReturnConfirmationCode() {
        // Arrange
        String url = "http://localhost:" + port + "/api/vk/callback";
        String requestJson = """
        {
            "type": "confirmation",
            "secret": "%s"
        }
        """.formatted(vkConfig.getSecretKey());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(vkConfig.getConfirmationCode(), response.getBody());
    }
}