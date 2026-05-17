package edu.escuelaing.arsw.medigo.handler;

import edu.escuelaing.arsw.medigo.handler.dto.PlaceBidRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuctionWebSocketHandlerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuctionWebSocketHandler auctionWebSocketHandler;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(auctionWebSocketHandler, "backendUrl", "http://localhost:8080");
    }

    @Test
    void handleBid_ShouldForwardBidToBackend() {
        // Arrange
        Long auctionId = 1L;
        PlaceBidRequest request = new PlaceBidRequest(10L, "Test User", new java.math.BigDecimal("100.0"));

        // Act
        auctionWebSocketHandler.handleBid(auctionId, request);

        // Assert
        String expectedUrl = "http://localhost:8080/api/auctions/{auctionId}/bids";
        verify(restTemplate, times(1)).postForEntity(eq(expectedUrl), eq(request), eq(Object.class), eq(auctionId));
    }
}
