package com.ihrm.system.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author misterWei
 * @create 2019年11月18号:19点29分
 * @mailbox mynameisweiyan@gmail.com
 */

public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }



    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println(new java.lang.String(message.getBody()));
        System.out.println(new java.lang.String(message.getChannel()));
    }
}
