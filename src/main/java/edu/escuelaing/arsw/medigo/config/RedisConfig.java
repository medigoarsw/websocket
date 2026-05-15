package edu.escuelaing.arsw.medigo.config;

import edu.escuelaing.arsw.medigo.pubsub.RedisAuctionEventListener;
import edu.escuelaing.arsw.medigo.pubsub.RedisLogisticsEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   RedisAuctionEventListener auctionListener,
                                                   RedisLogisticsEventListener logisticsListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(auctionListener, new ChannelTopic("auction-events"));
        container.addMessageListener(logisticsListener, new ChannelTopic("logistics:order:status"));
        return container;
    }
}
