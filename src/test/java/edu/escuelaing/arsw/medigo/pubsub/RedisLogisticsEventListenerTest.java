package edu.escuelaing.arsw.medigo.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisLogisticsEventListenerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private RedisLogisticsEventListener redisLogisticsEventListener;

    @Test
    void onMessage_ShouldBroadcastLogisticsStatus() throws Exception {
        // Arrange
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", 123);
        payload.put("status", "IN_TRANSIT");

        byte[] body = objectMapper.writeValueAsBytes(payload);
        Message redisMessage = new DefaultMessage("logistics-channel".getBytes(), body);

        // Act
        redisLogisticsEventListener.onMessage(redisMessage, null);

        // Assert
        verify(messagingTemplate, times(1)).convertAndSend(
                eq("/topic/order/123/status"),
                any(Object.class)
        );
    }

    @Test
    void onMessage_ShouldNotBroadcastIfOrderIdIsMissing() throws Exception {
        // Arrange
        Map<String, Object> payload = new HashMap<>();
        payload.put("status", "IN_TRANSIT");

        byte[] body = objectMapper.writeValueAsBytes(payload);
        Message redisMessage = new DefaultMessage("logistics-channel".getBytes(), body);

        // Act
        redisLogisticsEventListener.onMessage(redisMessage, null);

        // Assert
        verify(messagingTemplate, never()).convertAndSend(anyString(), any(Object.class));
    }
}
