package edu.escuelaing.arsw.medigo.pubsub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionEvent {
    public enum EventType {
        BID_PLACED, AUCTION_CLOSED, AUCTION_STARTED, WINNER_ADJUDICATED
    }
    private EventType type;
    private Long auctionId;
    private BigDecimal currentAmount;
    private String leaderName;
    private Long leaderId;
    private LocalDateTime timestamp;
    private String message;
}
