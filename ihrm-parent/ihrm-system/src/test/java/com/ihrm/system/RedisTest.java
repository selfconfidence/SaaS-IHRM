package com.ihrm.system;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author misterWei
 * @create 2019年11月18号:18点51分
 * @mailbox mynameisweiyan@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1(){
        redisTemplate.opsForValue().set("111","222",5L, TimeUnit.MINUTES);
        System.out.println(redisTemplate.opsForValue().get("111"));
    }
}
