package com.ihrm.system.config;

import com.ihrm.system.listener.RedisKeyExpirationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author misterWei
 * @create 2019年11月18号:19点25分
 * @mailbox mynameisweiyan@gmail.com
 */
@Configuration
public class RedisListenerConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
   public RedisMessageListenerContainer container(){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
       container.setConnectionFactory(redisConnectionFactory);
       return container;
    }
    @Bean
   public RedisKeyExpirationListener redisKeyExpirationListener(){
        return new RedisKeyExpirationListener(this.container());
    }

}
