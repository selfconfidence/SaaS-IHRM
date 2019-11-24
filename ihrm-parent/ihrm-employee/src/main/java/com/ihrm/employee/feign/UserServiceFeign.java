package com.ihrm.employee.feign;

import com.ihrm.domain.response.EmployeeReportResult;
import com.ihrm.employee.feign.impl.UserServiceFeignImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author misterWei
 * @create 2019年11月24号:21点01分
 * @mailbox mynameisweiyan@gmail.com
 */
@FeignClient(value = "ihrm-system",fallback = UserServiceFeignImpl.class)
public interface UserServiceFeign {

    @RequestMapping(value = "/sys/findByEmployeeResult/{month}",method = RequestMethod.GET)
    public List<EmployeeReportResult> findByEmployeeResult(@PathVariable("month") String month, @RequestParam("companyId")String companyId);
}
