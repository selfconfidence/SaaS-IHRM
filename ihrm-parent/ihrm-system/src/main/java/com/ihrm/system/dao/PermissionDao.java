package com.ihrm.system.dao;

import com.ihrm.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author misterWei
 * @create 2019年09月14号:19点34分
 * @mailbox mynameisweiyan@gmail.com
 */
public interface PermissionDao extends JpaRepository<Permission,String>,JpaSpecificationExecutor<Permission> {
    public List<Permission> findByTypeAndPid(Integer type,String pid);
}
