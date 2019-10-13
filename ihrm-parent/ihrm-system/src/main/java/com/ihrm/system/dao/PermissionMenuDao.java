package com.ihrm.system.dao;

import com.ihrm.domain.PermissionMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author misterWei
 * @create 2019年09月14号:19点34分
 * @mailbox mynameisweiyan@gmail.com
 */
public interface PermissionMenuDao extends JpaRepository<PermissionMenu,String>,JpaSpecificationExecutor<PermissionMenu> {
}
