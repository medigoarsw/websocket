package edu.escuelaing.arsw.medigo.handler;

import edu.escuelaing.arsw.medigo.handler.dto.PlaceBidRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuctionWebSocketHandler {

    @Value("${app.backend-rest.url:http://localhost:8080}")
    private String backendUrl;

    private final RestTemplate restTemplate;

    @MessageMapping("/auction/{auctionId}/bid")
    public void handleBid(@DestinationVariable Long auctionId,
                          PlaceBidRequest request) {
        log.debug("WS Bid received for auction {}: {}", auctionId, request);

        try {
            // Forward the command to the main REST backend
            String url = backendUrl + "/api/auctions/" + auctionId + "/bids";
            restTemplate.postForEntity(url, request, Object.class);
            log.info("Bid forwarded to backend for auction {}", auctionId);
        } catch (Exception e) {
            log.error("Failed to forward bid to backend: {}", e.getMessage());
        }
    }
}
