package com.weiyan.concurrence;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title 消峰限流
 * @package com.weiyan.concurrence
 * @date 2019/7/24 14:34
 */
public class Throttling {
    //这里表示 一秒 生成 200个桶

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        ((AnnotationConfigApplicationContext) applicationContext).register(SpringConfig.class);
        ((AnnotationConfigApplicationContext) applicationContext).refresh();
        //在高并发的环境下有必要进行这类的操作,不会导致系统的溃崩.
        RateLimiter  rateLimiter =  applicationContext.getBean(RateLimiter.class);
        while (true) {
            if (rateLimiter.tryAcquire(100)) {
                System.err.println(System.currentTimeMillis());
                System.err.println("正常执行");
            }else{
               // System.out.println("消峰限流");
            }
        }

    }

}
