package com.ihrm.system.dao;

import com.ihrm.domain.User;
import com.ihrm.domain.response.EmployeeReportResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author misterWei
 * @create 2019年09月14号:19点34分
 * @mailbox mynameisweiyan@gmail.com
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User> {

     User findByMobile(String mobile);

     @Query(nativeQuery = false,value = "select new com.ihrm.domain.response.EmployeeReportResult(u,e) from UserCompanyPersonal" +
             " as u left join EmployeeResignation as e on e.userId = u.userId where u.companyId = ?2 and (u.timeOfEntry like ?1 or e.resignationTime like ?1 )")
    List<EmployeeReportResult> findByEmployeeResult(String month, String companyId);
}
