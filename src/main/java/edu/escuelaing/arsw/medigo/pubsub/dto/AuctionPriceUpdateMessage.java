package edu.escuelaing.arsw.medigo.pubsub.dto;

import java.math.BigDecimal;

public record AuctionPriceUpdateMessage(
        String     eventType,
        Long       auctionId,
        BigDecimal currentPrice,
        String     leaderName,
        Long       leaderId,
        String     timestamp,
        String     message
) {
    public static AuctionPriceUpdateMessage from(AuctionEvent event) {
        return new AuctionPriceUpdateMessage(
                event.getType().name(),
                event.getAuctionId(),
                event.getCurrentAmount(),
                event.getLeaderName(),
                event.getLeaderId(),
                event.getTimestamp() != null ? event.getTimestamp().toString() : null,
                event.getMessage()
        );
    }
}
