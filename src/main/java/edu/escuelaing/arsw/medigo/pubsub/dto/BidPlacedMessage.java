package edu.escuelaing.arsw.medigo.pubsub.dto;

import java.math.BigDecimal;

public record BidPlacedMessage(
        Long       auctionId,
        BigDecimal amount,
        String     bidderName,
        Long       bidderId,
        String     placedAt
) {
    public static BidPlacedMessage from(AuctionEvent event) {
        return new BidPlacedMessage(
                event.getAuctionId(),
                event.getCurrentAmount(),
                event.getLeaderName(),
                event.getLeaderId(),
                event.getTimestamp() != null ? event.getTimestamp().toString() : null
        );
    }
}
