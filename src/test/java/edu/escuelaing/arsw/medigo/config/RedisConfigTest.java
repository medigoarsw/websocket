package edu.escuelaing.arsw.medigo.config;

import edu.escuelaing.arsw.medigo.pubsub.RedisAuctionEventListener;
import edu.escuelaing.arsw.medigo.pubsub.RedisLogisticsEventListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class RedisConfigTest {

    @Mock
    private RedisConnectionFactory connectionFactory;

    @Mock
    private RedisAuctionEventListener auctionListener;

    @Mock
    private RedisLogisticsEventListener logisticsListener;

    @InjectMocks
    private RedisConfig redisConfig;

    @Test
    void container_ShouldCreateContainerWithListeners() {
        RedisMessageListenerContainer result = redisConfig.container(connectionFactory, auctionListener, logisticsListener);
        assertNotNull(result);
    }
}
