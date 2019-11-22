package com.ihrm.system.shiro;

import com.ihrm.common.shiro.realm.IhrmRealm;
import com.ihrm.domain.Permission;
import com.ihrm.domain.User;
import com.ihrm.domain.response.UserInitResult;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author misterWei
 * @create 2019年11月23号:01点49分
 * @mailbox mynameisweiyan@gmail.com
 */
public class UserRealm extends IhrmRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 实现 认证方法
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        SimpleAuthenticationInfo saInfo = null;
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        /**
         * 1 查询数据库,是否属实
         *
         * 2 认证成功 返回info
         *
         * 3 认证失败返回 null 报错
         */
        User user = userService.findByPhone(upToken.getUsername());
        //返回null，会抛出异常，标识用户名和密码不匹配
        if (user != null && user.getPassword().equals(new String(upToken.getPassword()))) {
            // 特殊处理,这边持久会话需 需使用UserInitResult 类实现 贴近业务逻辑
            List<Permission> permissions = null;
            UserInitResult userInitResult = null;
            //笃定是否是特定管理员
            Map parMap = new HashMap();
            if ("saasAdmin".equals(user.getLevel())) {
                //查询所有的权限控制
                permissions = permissionService.findAll(parMap);
                userInitResult = new UserInitResult(user, permissions);
            } else if ("coAdmin".equals(user.getLevel())) {
                //查询企业特定的权限控制
                parMap.put("enVisible", 1);
                permissions = permissionService.findAll(parMap);
                userInitResult = new UserInitResult(user, permissions);
            } else {
                userInitResult = new UserInitResult(user);
            }
            //构造方法：安全数据，密码，realm域名
            saInfo = new SimpleAuthenticationInfo(userInitResult, user.getPassword(), super.getName());

        }
        return saInfo;
    }
}
