package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.domain.Permission;
import com.ihrm.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class PermissionController extends BaseController{

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = "/permission/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(ResultCode.SUCCESS,permissionService.findById(id));
    }

    @RequestMapping(value = "/permission",method = RequestMethod.POST)
    public Result save(@RequestBody Permission permission) throws Exception {
        //保存操作需要根据 type 去保存, 保存权限以及对应的api | menu | point
        permissionService.save(permission);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/permission/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) throws CommonException {
        permissionService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/permission/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable("id")String id,@RequestBody Permission permission) throws Exception {
        permissionService.update(id,permission);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/permission",method = RequestMethod.GET)
    public Result findByPageList(@RequestParam Map parmeMap){
        List<Permission> permissionList = permissionService.findAll(parmeMap);
        return new Result(ResultCode.SUCCESS,permissionList);
    }

    @RequestMapping(value = "permission/{type}/{pid}",method = RequestMethod.GET)
    public Result findByTypeAndPid(@PathVariable("type") String type,@PathVariable("pid") String pid){
        Map parmas = new HashMap(15);
        parmas.put("type",type);
        parmas.put("pid",pid);
        List<Permission> permissionList =  permissionService.findAll(parmas);
        return new Result(ResultCode.SUCCESS,permissionList);

    }
}
