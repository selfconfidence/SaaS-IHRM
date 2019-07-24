package com.weiyan.concurrence;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.weiyan.concurrence
 * @date 2019/7/24 14:58
 */
@Configuration
public class SpringConfig {

    @Bean
    public RateLimiter getRateLimiter(){
        //一秒需要生成多少个桶
        return RateLimiter.create(100d);
    }
    @Bean
    public RedisTemplate getRedisTemplate(){
        return new RedisTemplate();
    }
}
