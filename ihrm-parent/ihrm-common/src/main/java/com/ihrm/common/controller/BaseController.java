package com.ihrm.common.controller;

import com.ihrm.domain.response.UserInitResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
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
         *  // 获取到安全数据
         */
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        if (principals != null && !principals.isEmpty()){
          UserInitResult userInitResult = (UserInitResult) principals.getPrimaryPrincipal();
          this.companyId = userInitResult.getCompanyId();
          this.companyName = userInitResult.getCompany();
        }


    }

}
