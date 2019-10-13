package com.ihrm.system.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.User;
import com.ihrm.system.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @author misterWei
 * @create 2019年09月14号:19点35分
 * @mailbox mynameisweiyan@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {


    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    /**
     *  根据ID查询
     */
    public User findById(String id){
        return   userDao.findById(id).get();
    }

    /**
     * 根据查询所有用户并分页
     */
    public Page<User> findAll(int page, int size, Map<String,Object> map){
        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> listPredicate = new ArrayList<>();
                // 查询条件 企业ID companyId
                if (!StringUtils.isEmpty(map.get("companyId"))){
                    listPredicate.add(criteriaBuilder.equal(root.get("companyId").as(String.class),map.get("companyId")));
                }
                //departmentId
                if (!StringUtils.isEmpty(map.get("departmentId"))){
                    listPredicate.add(criteriaBuilder.equal(root.get("departmentId").as(String.class),map.get("departmentId")));
                }
                //hasDept    //根据请求的hasDept判断  是否分配部门 0未分配（departmentId = null），1 已分配 （departmentId ！= null）
                 if (!StringUtils.isEmpty(map.get("hasDept"))){
                    //不是null
                     if ("0".equals(map.get("hasDept"))){
                         //未分配
                         listPredicate.add(criteriaBuilder.isNull(root.get("departmentId")));
                     }else {
                         //已经分配
                         listPredicate.add(criteriaBuilder.isNotNull(root.get("departmentId")));

                     }
                 }

                return criteriaBuilder.and(listPredicate.toArray(new Predicate[listPredicate.size()]));
            }
        };

        return userDao.findAll(specification,new PageRequest(page - 1,size));
    }

    /**
     * 更新用户列表
     */
    public void update(String id,User user){
        User userResult = userDao.findById(id).get();
        //2.设置用户属性
        userResult.setUsername(user.getUsername());
        userResult.setPassword(user.getPassword());
        userResult.setEnableState(user.getEnableState());
        userResult.setInServiceStatus(user.getInServiceStatus());
        //3.更新用户
        userDao.save(userResult);
    }

    /**
     * 删除部门
     */

    public void deleteById(String id ){userDao.deleteById(id);}

    /**
     * 保存部门
     */

    public void save(User user ){
        user.setId(idWorker.nextId());
        //设置属性
        //默认密码
        user.setPassword("123456");
        //默认状态
        user.setEnableState(1);
        user.setCreateTime(new Date());

        userDao.save(user);}
}
