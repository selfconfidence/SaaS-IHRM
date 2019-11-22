package com.ihrm.company.controller;

import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.domain.Company;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author misterWei
 * @create 2019年08月03号:17点22分
 * @mailbox mynameisweiyan@gmail.com
 */

@RestController
@RequestMapping("/company")
@CrossOrigin
public class CompanyController {

    @Autowired
    private CompanyService compayService;

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
              Company company =  compayService.findById(id);
        return new Result(ResultCode.SUCCESS,company);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Company company){
        compayService.save(company);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id){
        compayService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable("id")String id,@RequestBody Company company){
        compayService.update(id,company);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/{page}/{size}",method = RequestMethod.POST)
    @RequiresPermissions({"API-LOOK-COMPANY-LIST_ADMIN","自定义权限"})
    public PageResult findByPageList(@PathVariable("page")int page,@PathVariable("size")int size,@RequestBody Company company){
       return compayService.findByPageList(company,(page -1)*size,size);
    }
}
