package com.ihrm.common.shiro.realm;

import com.ihrm.domain.response.UserInitResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

/**
 * @author misterWei
 * @create 2019年11月23号:01点35分
 * @mailbox mynameisweiyan@gmail.com
 *
 * 自定义 Realm 实现方式
 */
public class IhrmRealm extends AuthorizingRealm {
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    //实现授权方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //得到安全数据
       UserInitResult initResult  =  (UserInitResult)principalCollection.getPrimaryPrincipal();
       //创建权限信息  我这边没有 角色控制,只有权限;
        Set<String> apisPer = (Set<String>) initResult.getRoles().get("apis");
        SimpleAuthorizationInfo szInfo = new SimpleAuthorizationInfo();
        szInfo.setStringPermissions(apisPer);
        //szInfo.setRoles(); 添加角色的
       return szInfo;
    }

    // 子类实现认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }
}
