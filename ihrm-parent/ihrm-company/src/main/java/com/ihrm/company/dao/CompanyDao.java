package com.ihrm.company.dao;

import com.ihrm.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author misterWei
 * @create 2019年08月03号:17点13分
 * @mailbox mynameisweiyan@gmail.com
 */
public interface CompanyDao extends JpaRepository<Company,String> {
}
