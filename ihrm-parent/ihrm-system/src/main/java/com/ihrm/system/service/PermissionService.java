package com.ihrm.system.service;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.Permission;
import com.ihrm.domain.PermissionApi;
import com.ihrm.domain.PermissionMenu;
import com.ihrm.domain.PermissionPoint;
import com.ihrm.system.dao.PermissionApiDao;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.PermissionMenuDao;
import com.ihrm.system.dao.PermissionPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author misterWei
 * @create 2019年09月14号:19点35分
 * @mailbox mynameisweiyan@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private PermissionApiDao permissionApiDao;
    @Autowired
    private PermissionMenuDao permissionMenuDao;
    @Autowired
    private PermissionPointDao permissionPointDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 根据ID查询
     */
    public Map<String, Object> findById(String id) {
        Permission permission = permissionDao.findById(id).get();
        Object obj = new Object();
        if (permission.getType().equals(PermissionConstants.PY_API)) {
            obj = permissionApiDao.findById(id).get();
        } else if (permission.getType().equals(PermissionConstants.PY_MENU)) {
            obj = permissionMenuDao.findById(id).get();
        }else if (permission.getType().equals(PermissionConstants.PY_POINT)) {
            obj = permissionPointDao.findById(id).get();

        }
        Map<String, Object> perMap = BeanMapUtils.beanToMap(obj);
        perMap.putAll(BeanMapUtils.beanToMap(permission));
        return perMap;
    }

    /**
     * 根据查询所有用户并分页
     */
    public List<Permission> findAll(Map<String, Object> map) {
        Specification<Permission> specification = new Specification<Permission>() {
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> listPredicate = new ArrayList<>();
                //根据父id查询
                if (!StringUtils.isEmpty(map.get("pid"))) {
                    listPredicate.add(criteriaBuilder.equal(root.get("pid").as(String.class), map.get("pid")));
                }
                //根据enVisible查询
                if (!StringUtils.isEmpty(map.get("enVisible"))) {
                    listPredicate.add(criteriaBuilder.equal(root.get("enVisible").as(Integer.class), Integer.parseInt(map.get("enVisible").toString())));
                }
                //根据type 查询 如果 type 为 0  查询菜单 功能,否则就是api

                String type = (String) map.get("type");
                if (!StringUtils.isEmpty(type)) {
                    //不是null
                    CriteriaBuilder.In<Object> objectIn = criteriaBuilder.in(root.get("type"));
                    if ("0".equals(type)) {
                        //未分配 使用in 的方式
                        objectIn.value(1).value(2);
                    } else {
                        //已经分配
                        objectIn.value(Integer.parseInt(type));
                    }
                    listPredicate.add(objectIn);
                }

                return criteriaBuilder.and(listPredicate.toArray(new Predicate[listPredicate.size()]));
            }
        };

        return permissionDao.findAll(specification);
    }

    /**
     * 更新用户列表
     */
    public void update(String id, Permission permission) throws Exception {
        Permission permissionResult = permissionDao.findById(id).get();
        permissionResult.setCode(permission.getCode());
        permissionResult.setDescription(permission.getDescription());
        permissionResult.setEnVisible(permission.getEnVisible());
        permissionResult.setName(permission.getName());
        permissionResult.setType(permission.getType());
        //1为菜单 2为功能 3为API'
        Integer type = permission.getType();
        Map<String, Object> beanToMap = BeanMapUtils.beanToMap(permission);
        switch (type) {
            case PermissionConstants.PY_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(beanToMap, PermissionMenu.class);
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(beanToMap, PermissionPoint.class);
                permissionPointDao.save(permissionPoint);

                break;
            case PermissionConstants.PY_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(beanToMap, PermissionApi.class);
                permissionApiDao.save(permissionApi);
                break;

            default:
                throw new CommonException(ResultCode.FAIL);
        }

        permissionDao.save(permissionResult);
    }

    /**
     * 删除权限
     */

    public void deleteById(String id) throws CommonException {
        Permission permission = permissionDao.findById(id).get();
        permissionDao.delete(permission);
        switch (permission.getType()) {
            case PermissionConstants.PY_MENU:
                permissionMenuDao.deleteById(id);
                break;
            case PermissionConstants.PY_POINT:
                permissionPointDao.deleteById(id);
                break;
            case PermissionConstants.PY_API:
                permissionApiDao.deleteById(id);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
    }

    /**
     * 保存权限, 注意 权限ID和 menu  api point 是一致的 这是管理关系
     */

    public void save(Permission permission) throws Exception {
        permission.setId(idWorker.nextId());

        //1为菜单 2为功能 3为API'
        Integer type = permission.getType();
        Map<String, Object> beanToMap = BeanMapUtils.beanToMap(permission);
        switch (type) {
            case PermissionConstants.PY_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(beanToMap, PermissionMenu.class);
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(beanToMap, PermissionPoint.class);
                permissionPointDao.save(permissionPoint);

                break;
            case PermissionConstants.PY_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(beanToMap, PermissionApi.class);
                permissionApiDao.save(permissionApi);
                break;

            default:
                throw new CommonException(ResultCode.FAIL);
        }

        permissionDao.save(permission);
    }
}
