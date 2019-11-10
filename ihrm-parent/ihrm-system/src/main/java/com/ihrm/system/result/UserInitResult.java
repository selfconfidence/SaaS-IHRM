package com.ihrm.system.result;

import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author misterWei
 * @create 2019年11月10号:17点29分
 * @mailbox mynameisweiyan@gmail.com
 */
@Setter
@Getter
@NoArgsConstructor
public class UserInitResult {
    private String userName;
    private String mobile;
    private String company;
    private Map roles = new HashMap();

    public UserInitResult(User user) {
        this.userName = user.getUsername();
        this.mobile = user.getMobile();
        this.company = user.getCompanyName();
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
}
