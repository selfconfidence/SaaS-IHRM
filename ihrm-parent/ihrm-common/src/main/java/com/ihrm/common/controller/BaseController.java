package com.ihrm.common.controller;

import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 响应多租户请求,定义总web层,减少实际代码冗余性
 * @author  mister_wei
 */
public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String companyId;
    protected String companyName;
    protected Claims claims;

    /**
     *
     * @param request
     * @param response
     * @ModelAttribute 这个注解用于在执行响应方法的时候,首先执行此方法,用来绑定实际参数
     */
    @ModelAttribute
    public void setResAnReq(HttpServletRequest request,HttpServletResponse response) {
        this.request = request;
        this.response = response;
        /**
         * 目前使用 companyId = 1
         *         companyName = "Saas企业固定测试多租户"
         */
        this.claims = (Claims)request.getAttribute("user_claims");
        this.companyId = (String) claims.get("companyId");
        this.companyName = (String) claims.get("companyName");
    }

}
