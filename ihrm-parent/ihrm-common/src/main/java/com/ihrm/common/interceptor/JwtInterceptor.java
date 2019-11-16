package com.ihrm.common.interceptor;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author misterWei
 * @create 2019年11月17号:00点32分
 * @mailbox mynameisweiyan@gmail.com
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter{

    private final static  Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);
    @Autowired
    private JwtUtils jwtUtils;
    /**
     * 实际逻辑操作
     * @param request
     * @param response
     * @param handler 用于拿取当前请求方法信息,不是GET,POST,是实际的方法以及注解方式
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authentication = request.getHeader("Authentication");
        if (!StringUtils.isEmpty(authentication) && authentication.startsWith("Token")) {
            /**
             * 进行解析
             */
            try {

                Claims claims = jwtUtils.parseJWT(authentication.replace("Token ", ""));
                /**
                 * 根据当前访问的用户所API,查询该用户是否有权限访问,健全到 RequestMapping 中的标识信息name
                 */
                String apis = (String) claims.get("apis");
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                RequestMapping methodAnnotation = handlerMethod.getMethodAnnotation(RequestMapping.class);
                if (apis.contains(methodAnnotation.name())){
                    request.setAttribute("user_claims",claims);
                }else {
                    throw new CommonException(ResultCode.UNAUTHORISE);

                }

            }catch (Exception e){
                logger.error("解析错误,原因为: "+e.getMessage());
                throw new CommonException(ResultCode.UNAUTHENTICATED);
            }
            return true;
        }
        throw new CommonException(ResultCode.UNAUTHENTICATED);


    }

    /**
     * 处理 结果返回view 时候的方法
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 用于解决一些异常,在该请求为请求结束前
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
