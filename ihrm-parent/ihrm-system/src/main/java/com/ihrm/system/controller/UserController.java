package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.JwtUtils;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.Permission;
import com.ihrm.domain.Role;
import com.ihrm.domain.User;
import com.ihrm.domain.response.UserResult;
import com.ihrm.system.result.UserInitResult;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author misterWei
 * @create 2019年09月14号:20点06分
 * @mailbox mynameisweiyan@gmail.com
 */
@RestController
@RequestMapping("/sys")
@CrossOrigin
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PermissionService permissionService;

    /**
     * 分配角色
     */
    @RequestMapping(value = "/user/assignRoles", method = RequestMethod.PUT)
    public Result assignRoles(@RequestBody Map<String, Object> map) {
        //1.获取被分配的用户id
        String userId = (String) map.get("id");
        //2.获取到角色的id列表
        List<String> roleIds = (List<String>) map.get("roleIds");
        //3.调用service完成角色分配
        userService.assignRoles(userId, roleIds);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        User user = userService.findById(id);
        UserResult userResult = new UserResult(user);
        return new Result(ResultCode.SUCCESS, userResult);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public Result save(@RequestBody User user) {
        //固定好公司值传递

        userService.save(user);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE,name = "API_USER_DELETE")
    public Result delete(@PathVariable String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable("id") String id, @RequestBody User user) {
        userService.update(id, user);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user/{page}/{size}", method = RequestMethod.GET)
    public Result findByPageList(@PathVariable Integer page, @PathVariable Integer size, @RequestParam Map parmeMap) {
        //添加企业ID
        parmeMap.put("companyId", companyId);
        Page userList = userService.findAll(page, size, parmeMap);
        return new Result(ResultCode.SUCCESS, new PageResult<User>(userList.getTotalElements(), userList.getContent()));
    }

    /**
     * 用户登陆
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(String phone, String password) {
        User user = userService.findByPhone(phone);
        if (user != null && user.getPassword().equals(password)) {
            //使用JWT生成密钥形式
            Map<String, Object> parMap = BeanMapUtils.beanToMap(user);
            /**
             * 需要API访问后端控制,需要加入token中
             */
            StringBuilder apis = new StringBuilder();
            for (Role role : user.getRoles()) {
                for (Permission permission : role.getPermissions()) {
                    if (permission.getType() == PermissionConstants.PY_API) {
                        apis.append(permission.getCode()).append(",");
                    }
                }
            }
             parMap.put("apis",apis.toString());
            String jwtToken = jwtUtils.createJWT(user.getId(), user.getUsername(), parMap);
            return new Result(ResultCode.SUCCESS, jwtToken);
        }
        return new Result(ResultCode.USERAUTHERROR);
    }

    /**
     * 根据当前token 加载出用户的所有信息
     */

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public Result profit(HttpServletRequest request) {
        String authentication = request.getHeader("Authentication");
        if (authentication == null) {
            return new Result(ResultCode.UNAUTHENTICATED);
        }
        String id = (String) claims.get("id");
        User user = userService.findById(id);
        String level = user.getLevel();
        Result result = null;
        List<Permission> permissions = null;
        //笃定是否是特定管理员
        Map parMap = new HashMap();
        if ("saasAdmin".equals(level)) {
            //查询所有的权限控制
            permissions = permissionService.findAll(parMap);
            result = new Result(ResultCode.SUCCESS, new UserInitResult(user,permissions));
        }else if ("coAdmin".equals(level)){
            //查询企业特定的权限控制
            parMap.put("enVisible",1);
            permissions = permissionService.findAll(parMap);
            result = new Result(ResultCode.SUCCESS, new UserInitResult(user,permissions));
        }else {
          result = new Result(ResultCode.SUCCESS, new UserInitResult(user));
        }
        return result;
    }
}
