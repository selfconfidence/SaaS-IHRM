package com.ihrm.company.dao;

import com.ihrm.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author misterWei
 * @create 2019年08月17号:19点03分
 * @mailbox mynameisweiyan@gmail.com
 */
public interface DepartmentDao extends JpaRepository<Department,String>,JpaSpecificationExecutor<Department> {
}
