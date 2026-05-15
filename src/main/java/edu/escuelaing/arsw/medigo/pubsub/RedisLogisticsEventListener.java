package edu.escuelaing.arsw.medigo.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLogisticsEventListener implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            Map<String, Object> payload = objectMapper.readValue(message.getBody(), Map.class);
            Object orderId = payload.get("orderId");
            
            if (orderId != null) {
                log.info("Redis Logistics Event received for order {}: {}", orderId, payload.get("status"));
                String destination = "/topic/order/" + orderId + "/status";
                messagingTemplate.convertAndSend(destination, payload);
            }
        } catch (Exception e) {
            log.error("Failed to process Redis Logistics message", e);
        }
    }
}
