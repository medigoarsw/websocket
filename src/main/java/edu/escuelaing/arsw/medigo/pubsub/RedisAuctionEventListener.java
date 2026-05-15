package edu.escuelaing.arsw.medigo.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.escuelaing.arsw.medigo.pubsub.dto.AuctionEvent;
import edu.escuelaing.arsw.medigo.pubsub.dto.AuctionPriceUpdateMessage;
import edu.escuelaing.arsw.medigo.pubsub.dto.BidPlacedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisAuctionEventListener implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            AuctionEvent event = objectMapper.readValue(message.getBody(), AuctionEvent.class);
            log.info("Redis Event received for auction {}: {}", event.getAuctionId(), event.getType());

            AuctionPriceUpdateMessage priceUpdate = AuctionPriceUpdateMessage.from(event);

            // Broadcast to STOMP topics
            String auctionTopic = "/topic/auction/" + event.getAuctionId();
            messagingTemplate.convertAndSend(auctionTopic, priceUpdate);
            
            // Global topic for lists
            messagingTemplate.convertAndSend("/topic/auctions", priceUpdate);

            // Bids detail topic
            if (event.getType() == AuctionEvent.EventType.BID_PLACED) {
                messagingTemplate.convertAndSend(
                        auctionTopic + "/bids", 
                        BidPlacedMessage.from(event)
                );
            }

        } catch (Exception e) {
            log.error("Failed to process Redis message: {}", e.getMessage());
        }
    }
}
