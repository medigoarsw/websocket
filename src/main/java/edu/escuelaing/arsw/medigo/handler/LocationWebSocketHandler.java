package edu.escuelaing.arsw.medigo.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LocationWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;
    
    @Value("${app.backend-rest.url:http://localhost:8080}")
    private String backendUrl;

    private final RestTemplate restTemplate;

    /**
     * Recibe actualización de ubicación del repartidor y la retransmite al tópico del afiliado.
     * También la notifica al backend REST para persistencia si es necesario.
     */
    @MessageMapping("/location/{deliveryId}")
    public void handleLocationUpdate(
            @DestinationVariable String deliveryId,
            LocationPayload payload) {
        
        log.debug("GPS Update received for delivery {}: {}", deliveryId, payload);

        // 1. Broadcast inmediato al afiliado suscrito
        messagingTemplate.convertAndSend(
                "/topic/delivery/" + deliveryId + "/location",
                payload
        );

        // 2. Notificar al backend REST (opcional, para persistencia o lógica de negocio)
        try {
            String url = backendUrl + "/api/logistics/deliveries/{deliveryId}/location";
            restTemplate.postForEntity(url, payload, Object.class, deliveryId);
        } catch (Exception e) {
            log.error("Failed to forward location update to backend: {}", e.getMessage());
        }
    }

    /**
     * Recibe actualización de ubicación del USUARIO (afiliado) y la retransmite al repartidor.
     */
    @MessageMapping("/user-location/{orderId}")
    public void handleUserLocationUpdate(
            @DestinationVariable Long orderId,
            LocationPayload payload) {
        
        log.debug("User GPS Update received for order {}: {}", orderId, payload);

        // Broadcast al repartidor suscrito
        messagingTemplate.convertAndSend(
                "/topic/order/" + orderId + "/user-location",
                payload
        );
    }

    public record LocationPayload(double lat, double lng, Long ts, Long deliveryPersonId) {}
}
