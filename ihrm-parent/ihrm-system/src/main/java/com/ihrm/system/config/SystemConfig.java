package com.ihrm.system.config;

import com.ihrm.common.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author  Mister_wei
 */
/*@Configuration*/
public class SystemConfig extends WebMvcConfigurationSupport {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    /**
     * 添加拦截器的配置
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //1.添加自定义拦截器
        registry.addInterceptor(jwtInterceptor).
                //2.指定拦截器的url地址
                        addPathPatterns("/**").
                //3.指定不拦截的url地址
                        excludePathPatterns("/sys/login", "/frame/register/**");
    }

/*    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //重写这个方法，映射静态资源文件
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
        super.addResourceHandlers(registry);
    }*/
}