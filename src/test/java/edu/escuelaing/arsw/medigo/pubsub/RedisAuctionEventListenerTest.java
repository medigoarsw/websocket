package edu.escuelaing.arsw.medigo.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.escuelaing.arsw.medigo.pubsub.dto.AuctionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisAuctionEventListenerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @InjectMocks
    private RedisAuctionEventListener redisAuctionEventListener;

    @Test
    void onMessage_ShouldBroadcastToStompTopics() throws Exception {
        // Arrange
        AuctionEvent event = AuctionEvent.builder()
                .auctionId(1L)
                .type(AuctionEvent.EventType.BID_PLACED)
                .currentAmount(new BigDecimal("150.0"))
                .leaderName("John Doe")
                .leaderId(10L)
                .timestamp(LocalDateTime.now())
                .build();

        byte[] body = objectMapper.writeValueAsBytes(event);
        Message redisMessage = new DefaultMessage("auction-channel".getBytes(), body);

        // Act
        redisAuctionEventListener.onMessage(redisMessage, null);

        // Assert
        // Verify broadcast to specific auction topic
        verify(messagingTemplate, atLeastOnce()).convertAndSend(eq("/topic/auction/1"), any(Object.class));
        
        // Verify broadcast to global auctions topic
        verify(messagingTemplate, atLeastOnce()).convertAndSend(eq("/topic/auctions"), any(Object.class));

        // Verify broadcast to bids detail topic (since it's BID_PLACED)
        verify(messagingTemplate, atLeastOnce()).convertAndSend(eq("/topic/auction/1/bids"), any(Object.class));
    }

    @Test
    void onMessage_ShouldNotBroadcastBidsIfTypeIsNotBidPlaced() throws Exception {
        // Arrange
        AuctionEvent event = AuctionEvent.builder()
                .auctionId(1L)
                .type(AuctionEvent.EventType.AUCTION_STARTED)
                .currentAmount(new BigDecimal("100.0"))
                .timestamp(LocalDateTime.now())
                .build();

        byte[] body = objectMapper.writeValueAsBytes(event);
        Message redisMessage = new DefaultMessage("auction-channel".getBytes(), body);

        // Act
        redisAuctionEventListener.onMessage(redisMessage, null);

        // Assert
        verify(messagingTemplate, atLeastOnce()).convertAndSend(eq("/topic/auction/1"), any(Object.class));
        verify(messagingTemplate, never()).convertAndSend(eq("/topic/auction/1/bids"), any(Object.class));
    }
}
