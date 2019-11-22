package com.ihrm.company;

import com.ihrm.common.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

/**
 * @author misterWei
 * @create 2019年08月03号:17点05分
 * @mailbox mynameisweiyan@gmail.com
 */
@SpringBootApplication(scanBasePackages = {"com.ihrm"})
@EntityScan("com.ihrm.domain")
public class CompanyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class,args);
    }
    @Bean
    public IdWorker getIdWorker(){
      return  new IdWorker();
    }

}
