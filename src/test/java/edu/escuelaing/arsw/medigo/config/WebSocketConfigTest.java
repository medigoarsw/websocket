package edu.escuelaing.arsw.medigo.config;

import edu.escuelaing.arsw.medigo.security.AuthChannelInterceptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketConfigTest {

    @Mock
    private AuthChannelInterceptor authChannelInterceptor;

    @Mock
    private StompEndpointRegistry endpointRegistry;

    @Mock
    private MessageBrokerRegistry brokerRegistry;

    @Mock
    private ChannelRegistration channelRegistration;

    @InjectMocks
    private WebSocketConfig webSocketConfig;

    @Test
    void registerStompEndpoints_ShouldRegisterWsEndpoint() {
        StompWebSocketEndpointRegistration registration = mock(StompWebSocketEndpointRegistration.class);
        when(endpointRegistry.addEndpoint("/ws")).thenReturn(registration);
        when(registration.setAllowedOriginPatterns("*")).thenReturn(registration);

        webSocketConfig.registerStompEndpoints(endpointRegistry);

        verify(endpointRegistry).addEndpoint("/ws");
    }

    @Test
    void configureMessageBroker_ShouldEnableSimpleBroker() {
        webSocketConfig.configureMessageBroker(brokerRegistry);

        verify(brokerRegistry).enableSimpleBroker("/topic", "/queue");
        verify(brokerRegistry).setApplicationDestinationPrefixes("/app");
    }

    @Test
    void configureClientInboundChannel_ShouldAddInterceptor() {
        webSocketConfig.configureClientInboundChannel(channelRegistration);

        verify(channelRegistration).interceptors(authChannelInterceptor);
    }
}
