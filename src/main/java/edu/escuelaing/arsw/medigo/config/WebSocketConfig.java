package edu.escuelaing.arsw.medigo.config;

import edu.escuelaing.arsw.medigo.security.AuthChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthChannelInterceptor authChannelInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Los orígenes deben coincidir exactamente con los del SecurityConfig
        registry.addEndpoint("/ws")
                .setAllowedOrigins(
                    "https://frontmedigo.vercel.app",
                    "http://localhost:5173",
                    "http://localhost:3000"
                )
                .withSockJS(); // CRÍTICO: Necesario si el frontend usa SockJS (ej. para endpoints /ws/info)
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Tópicos para broadcast
        registry.enableSimpleBroker("/topic", "/queue");
        // Prefijo para mensajes que van a controladores (@MessageMapping)
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Registro del interceptor de seguridad para validar el JWT en STOMP CONNECT
        registration.interceptors(authChannelInterceptor);
    }
}
