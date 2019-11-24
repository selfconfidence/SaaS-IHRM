package com.ihrm.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author misterWei
 * @create 2019年11月23号:20点14分
 * @mailbox mynameisweiyan@gmail.com
 *
 * 配置对Feign 远程调用时对Head头信息的传递
 */
@Configuration
public class FeignAuthRequestConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // 获取到第一次系统级访问时的所有头信息
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (requestAttributes != null){
                    HttpServletRequest request = requestAttributes.getRequest();
                    Enumeration<String> headerNames = request.getHeaderNames();
                    if (headerNames !=  null && headerNames.hasMoreElements()){
                        while (headerNames.hasMoreElements()){
                            String headerName = headerNames.nextElement();
                            String headerValue = request.getHeader(headerName);
                            //装载头信息
                            requestTemplate.header(headerName,headerValue);
                        }
                    }
                }
            }
        };

    }
    /**
     * 监听器：监听HTTP请求事件
     * 解决RequestContextHolder.getRequestAttributes()空指针问题
     * @return
     */
    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }
}
