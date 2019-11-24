package com.ihrm.system.feign;

import com.ihrm.common.entity.Result;
import com.ihrm.domain.Department;
import com.ihrm.system.feign.downgrade.DepartmentFeignImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author misterWei
 * @create 2019年11月23号:20点01分
 * @mailbox mynameisweiyan@gmail.com
 */
// 指定远程访问的 微服务名称
//     fallback 服务降级熔断处理
@FeignClient(value = "ihrm-company",fallback = DepartmentFeignImpl.class)
public interface DepartmentFeign {

    @RequestMapping(value = "/company/department/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id);

    @RequestMapping(value = "/department/search",method = RequestMethod.POST)
    public Department searchDep(@RequestParam("code") String code, @RequestParam("companyId") String companyId);

}
