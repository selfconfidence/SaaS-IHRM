package com.ihrm.common.shiro.manager;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @author misterWei
 * @create 2019年11月23号:01点23分
 * @mailbox mynameisweiyan@gmail.com
 *
 * 自定义 Shiro 管理器
 */
public class TokenManager extends DefaultWebSessionManager {

    /**
     * 重写该方法,参考源码方式实现,从hender 中拿到token
     * @param request
     * @param response
     * @return
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {

        String authorization = WebUtils.toHttp(request).getHeader("Authorization");


        if (StringUtils.isEmpty(authorization)) {
            // 如果该认证为null 让父类逻辑生成 token
           return super.getSessionId(request,response);
        }else{
            //如果有这个认证需处理返回
            authorization = authorization.replaceAll("Bearer ", "");
            // 一定注意 请求域中的参数
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "Stateless request");
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, authorization);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        return authorization;
        }
    }
}
