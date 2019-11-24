package com.ihrm.employee.feign.impl;

import com.ihrm.domain.response.EmployeeReportResult;
import com.ihrm.employee.feign.UserServiceFeign;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author misterWei
 * @create 2019年11月24号:21点01分
 * @mailbox mynameisweiyan@gmail.com
 */
@Component
public class UserServiceFeignImpl implements UserServiceFeign {
    @Override
    public List<EmployeeReportResult> findByEmployeeResult(String month, String companyId) {
        System.out.println("熔断器启动");
        return null;
    }
}
