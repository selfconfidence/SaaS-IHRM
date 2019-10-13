package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.User;
import com.ihrm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author misterWei
 * @create 2019年09月14号:20点06分
 * @mailbox mynameisweiyan@gmail.com
 */
@RestController
@RequestMapping("/sys")
@CrossOrigin
public class UserController  extends BaseController{

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        User user =  userService.findById(id);
        return new Result(ResultCode.SUCCESS,user);
    }

    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public Result save(@RequestBody User user){
        //固定好公司值传递

        userService.save(user);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id){
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable("id")String id,@RequestBody User user){
        userService.update(id,user);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user/{page}/{size}",method = RequestMethod.GET)
    public Result findByPageList(@PathVariable Integer page,@PathVariable Integer size,@RequestParam Map parmeMap){
         //添加企业ID
        parmeMap.put("companyId",companyId);
        Page userList = userService.findAll(page, size, parmeMap);
        return new Result(ResultCode.SUCCESS,new PageResult<User>(userList.getTotalElements(),userList.getContent()));
    }
}
