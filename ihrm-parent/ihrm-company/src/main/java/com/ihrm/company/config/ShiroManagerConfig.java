package com.ihrm.company.config;

import com.ihrm.common.shiro.manager.TokenManager;
import com.ihrm.common.shiro.realm.IhrmRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author misterWei
 * @create 2019年11月23号:02点52分
 * @mailbox mynameisweiyan@gmail.com
 *
 * Shiro 权限控制的主要核心
 */
@Configuration
public class ShiroManagerConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Autowired
    private IhrmRealm ihrmRealm;

    @Autowired
    private RedisManager redisManager;

   // 配置自定义Realm 域
    @Bean
    public IhrmRealm getIhrmRealm(){
        return new IhrmRealm();
    }

    // 配置安全管理器
    @Bean
    public SecurityManager securityManager(){
     //使用默认的安全管理器
        DefaultWebSecurityManager deManager = new DefaultWebSecurityManager();
        //使用自定义的Session 会话管理器
        deManager.setSessionManager(defaultWebSessionManager());
        //使用自定义的缓存管理器
        deManager.setCacheManager(redisCacheManager());
        deManager.setRealm(ihrmRealm);
        return deManager;
    }

    //Filter 工厂,设置拦截条件,以及跳转路径
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean (SecurityManager securityManager){
        ShiroFilterFactoryBean sf = new ShiroFilterFactoryBean();
        //配置安全管理器
        sf.setSecurityManager(securityManager);
        //配置未登录的URL 可以是页面 也可以路由到其它接口处理
        sf.setLoginUrl("/autherror?code=1");
        //配置没权限的URL
        sf.setUnauthorizedUrl("/autherror?code=2");
        //配置拦截规则,需注意 顺序执行  key 为路径  value 为标识
        /**
         * value：过滤器类型
          shiro常用过滤器
                anon   ：匿名访问（表明此链接所有人可以访问）
                authc   ：认证后访问（表明此链接需登录认证成功之后可以访问）
         perms roles 等 均配注解中认证
         */
        Map<String,String> filterMap = new LinkedHashMap<>();
        filterMap.put("/autherror","anon");
        filterMap.put("/**","authc");
        sf.setFilterChainDefinitionMap(filterMap);
        return sf;
    }

   // 配置Redis Manager
    @Bean
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setTimeout(3000);
        return redisManager;
    }

    //cacheManager缓存 redis实现
    public RedisCacheManager redisCacheManager(){
        RedisCacheManager rcm = new RedisCacheManager();
        rcm.setRedisManager(redisManager);
        return rcm;
    }
    //RedisSessionDAO shiro sessionDao层的实现 通过redis
    //     * 使用的是shiro-redis开源插件
    public SessionDAO sessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        //redisSessionDAO.setExpire(); redis 存储的过期时长
        return redisSessionDAO;
    }
    //配置Shiro Session 管理
    public DefaultWebSessionManager defaultWebSessionManager(){
        TokenManager tokenManager = new TokenManager();
        tokenManager.setSessionIdCookieEnabled(false);//禁用cookie 传值
        tokenManager.setSessionIdUrlRewritingEnabled(false);//防止URL重载
        tokenManager.setSessionDAO(sessionDAO());
        return tokenManager;
    }




    //配置shiro注解支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
