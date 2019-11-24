package com.ihrm.company.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.company.service.DepartmentService;
import com.ihrm.domain.Company;
import com.ihrm.domain.Department;
import com.ihrm.domain.response.DeptListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author misterWei
 * @create 2019年08月17号:19点17分
 * @mailbox mynameisweiyan@gmail.com
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/company")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "/department/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        Department Department =  departmentService.findById(id);
        return new Result(ResultCode.SUCCESS,Department);
    }

    @RequestMapping(value = "/department",method = RequestMethod.POST)
    public Result save(@RequestBody Department Department){
        //固定好公司值传递

        departmentService.save(Department);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/department/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id){
        departmentService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/department/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable("id")String id,@RequestBody Department Department){
        departmentService.update(id,Department);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/department",method = RequestMethod.GET)
    public Result findByPageList(){
        List<Department> departmentList = departmentService.findAll(companyId);
        Company company = companyService.findById(companyId);

           return new Result(ResultCode.SUCCESS,new DeptListResult(company,departmentList));
    }

    /**
     * 远程 Feign 调用提供的接口
     */
    @RequestMapping(value = "/department/search",method = RequestMethod.POST)
    public Department searchDep(@RequestParam("code") String code,@RequestParam("companyId") String companyId){
               Department department = departmentService.findByCodeAndCompanyId(code,companyId);
                return department;
    }
}
