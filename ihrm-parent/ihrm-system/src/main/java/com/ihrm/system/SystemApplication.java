package com.ihrm.system;

import com.ihrm.common.interceptor.JwtInterceptor;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

/**
 * @author misterWei
 * @create 2019年09月14号:19点33分
 * @mailbox mynameisweiyan@gmail.com
 */
@SpringBootApplication
@EntityScan("com.ihrm.domain")
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
    }
    @Bean
    public IdWorker getIdWorker(){
        return  new IdWorker();
    }
    @Bean
    public JwtUtils getJwtUtils(){
        return new JwtUtils();
    }

    /**
     * 解决 JPA no session 问题
     * @return
     */
    @Bean
    public OpenEntityManagerInViewFilter getNoSession(){
        return new OpenEntityManagerInViewFilter();
    }

    @Bean
    public JwtInterceptor jwtInterceptor (){
        return new JwtInterceptor();
    }
}
