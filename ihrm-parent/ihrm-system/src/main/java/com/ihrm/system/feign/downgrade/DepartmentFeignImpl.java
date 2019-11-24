package com.ihrm.system.feign.downgrade;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.Department;
import com.ihrm.system.feign.DepartmentFeign;
import org.springframework.stereotype.Component;

/**
 * @author misterWei
 * @create 2019年11月23号:20点04分
 * @mailbox mynameisweiyan@gmail.com
 */
@Component
public class DepartmentFeignImpl implements DepartmentFeign {
    @Override
    public Result findById(String id) {
        /**
         * 降级处理
         */
        return new Result(ResultCode.FAIL,"服务熔断");
    }

    @Override
    public Department searchDep(String code, String companyId) {
        /**
         * 降级处理
         */
        return null;
    }
}
