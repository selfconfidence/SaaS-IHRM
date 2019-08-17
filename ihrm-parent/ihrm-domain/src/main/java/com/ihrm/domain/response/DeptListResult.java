package com.ihrm.domain.response;

import com.ihrm.domain.Company;
import com.ihrm.domain.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 为了响应 Saas架构模型的多租户的概念产生的数据结构
 * 定位某个公司下的所有部门-- 这样的数据结构
 */
@Getter
@Setter
@NoArgsConstructor
public class DeptListResult {
    private String companyId;
    private String companyName;
    private String companyManage;
    private List<Department> depts;

    public DeptListResult(Company company, List<Department> list) {
        this.companyId = company.getId();
        this.companyName = company.getName();
        this.companyManage = company.getLegalRepresentative();
        this.depts = list;
    }

}
