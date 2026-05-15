package edu.escuelaing.arsw.medigo.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class LocationWebSocketHandlerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LocationWebSocketHandler locationWebSocketHandler;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(locationWebSocketHandler, "backendUrl", "http://localhost:8080");
    }

    @Test
    void handleLocationUpdate_ShouldBroadcastAndForwardToBackend() {
        // Arrange
        String deliveryId = "DEL-123";
        LocationWebSocketHandler.LocationPayload payload = new LocationWebSocketHandler.LocationPayload(
                4.6097, -74.0817, System.currentTimeMillis(), 100L
        );

        // Act
        locationWebSocketHandler.handleLocationUpdate(deliveryId, payload);

        // Assert
        // Check broadcast
        verify(messagingTemplate, times(1)).convertAndSend(
                eq("/topic/delivery/DEL-123/location"),
                eq(payload)
        );

        // Check backend notification
        String expectedUrl = "http://localhost:8080/api/logistics/deliveries/DEL-123/location";
        verify(restTemplate, times(1)).postForEntity(eq(expectedUrl), eq(payload), eq(Object.class));
    }

    @Test
    void handleUserLocationUpdate_ShouldBroadcastToDriver() {
        // Arrange
        Long orderId = 456L;
        LocationWebSocketHandler.LocationPayload payload = new LocationWebSocketHandler.LocationPayload(
                4.6100, -74.0820, System.currentTimeMillis(), null
        );

        // Act
        locationWebSocketHandler.handleUserLocationUpdate(orderId, payload);

        // Assert
        verify(messagingTemplate, times(1)).convertAndSend(
                eq("/topic/order/456/user-location"),
                eq(payload)
        );
    }
}
