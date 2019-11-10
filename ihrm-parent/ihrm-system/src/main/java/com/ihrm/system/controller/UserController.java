package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.JwtUtils;
import com.ihrm.domain.User;
import com.ihrm.domain.response.UserResult;
import com.ihrm.system.result.UserInitResult;
import com.ihrm.system.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
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
            String jwtToken = jwtUtils.createJWT(user.getId(), user.getUsername(), BeanMapUtils.beanToMap(user));
            return new Result(ResultCode.SUCCESS, jwtToken);
        }
        return new Result(ResultCode.USERAUTHERROR);
    }

    /**
     * 根据当前token 加载出用户的所有信息
     */

    @RequestMapping(value = "/profit", method = RequestMethod.POST)
    public Result profit(HttpServletRequest request) {
        String authentication = request.getHeader("Authentication");
        if (authentication == null) {
            return new Result(ResultCode.UNAUTHENTICATED);
        }
        Claims claims = jwtUtils.parseJWT(authentication.replace("Token ", ""));
        String id = (String) claims.get("id");
        User user = userService.findById(id);
        return new Result(ResultCode.SUCCESS, new UserInitResult(user));
    }
}
