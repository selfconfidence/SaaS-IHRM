package com.weiyan.concurrence;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.weiyan.concurrence
 * @date 2019/7/24 15:20
 */
public class CountDownLua {
    static final int num = 1000;

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {


            Runnable runnable = () -> {

                try {
                    //到这里阻塞线程
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //高并发测试
                {
                    System.err.println("我是一个请求!");
                }

            };
            new Thread(runnable).start();

            try {
                //用来递减
                countDownLatch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void redisCon(){
      Jedis jedis = new Jedis("127.0.0.1");
        jedis.set("1","12");
        System.out.println(jedis.get("1"));
    }
}
