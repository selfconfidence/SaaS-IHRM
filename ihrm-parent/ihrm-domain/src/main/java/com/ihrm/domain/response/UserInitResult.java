package com.ihrm.domain.response;

import com.ihrm.domain.Permission;
import com.ihrm.domain.PermissionConstants;
import com.ihrm.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.crazycake.shiro.AuthCachePrincipal;

import java.io.Serializable;
import java.util.*;

/**
 * @author misterWei
 * @create 2019年11月10号:17点29分
 * @mailbox mynameisweiyan@gmail.com
 *
 * 该类需保存到 Shiro + Redis 中去保持会话,所有需实现AuthCachePrincipal类完成.
 */
@Setter
@Getter
@NoArgsConstructor
public class UserInitResult implements Serializable,AuthCachePrincipal {
    private String userName;
    private String mobile;
    private String company;
    private String companyId;
    private Map roles = new HashMap();

    public UserInitResult(User user) {
        this.userName = user.getUsername();
        this.mobile = user.getMobile();
        this.company = user.getCompanyName();
        this.companyId = user.getCompanyId();
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        user.getRoles().forEach(role -> {
            role.getPermissions().forEach(permission -> {
                Integer type = permission.getType();
                String id = permission.getId();
                if (type.equals(PermissionConstants.PY_MENU)){
                menus.add(id);
                }else if (type.equals(PermissionConstants.PY_POINT)){
                    points.add(id);
                }else if (type.equals(PermissionConstants.PY_API)){
                    apis.add(id);
                }

            });
        });
        this.roles.put("menus",menus);
        this.roles.put("points",points);
        this.roles.put("apis",apis);

    }

    public UserInitResult(User user, List<Permission> perList) {
        this.userName = user.getUsername();
        this.mobile = user.getMobile();
        this.company = user.getCompanyName();
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();

            perList.forEach(permission -> {
                Integer type = permission.getType();
                String id = permission.getId();
                if (type.equals(PermissionConstants.PY_MENU)){
                    menus.add(id);
                }else if (type.equals(PermissionConstants.PY_POINT)){
                    points.add(id);
                }else if (type.equals(PermissionConstants.PY_API)){
                    apis.add(id);
                }

            });

        this.roles.put("menus",menus);
        this.roles.put("points",points);
        this.roles.put("apis",apis);

    }

    // 空实现即可
    @Override
    public String getAuthCacheKey() {
        return null;
    }
}
