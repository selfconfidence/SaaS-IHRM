package com.ihrm.company.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.DepartmentDao;
import com.ihrm.domain.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author misterWei
 * @create 2019年08月17号:19点03分
 * @mailbox mynameisweiyan@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DepartmentService extends BaseService<Department>{

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private IdWorker idWorker;

    /**
     *  根据ID查询
     */
    public Department findById(String id){
      return   departmentDao.findById(id).get();
    }

    /**
     * 根据companyId查询所有部门
     */
    public List<Department> findAll(String companyId){

        return departmentDao.findAll(getComId(companyId));
    }

    /**
     * 更新部门列表
     */
    public void update(String id,Department department){
        Department dept = departmentDao.findById(id).get();
        //2.设置部门属性
        dept.setCode(department.getCode());
        dept.setIntroduce(department.getIntroduce());
        dept.setName(department.getName());
        //3.更新部门
        departmentDao.save(dept);
    }

    /**
     * 删除部门
     */

    public void deleteById(String id ){departmentDao.deleteById(id);}

    /**
     * 保存部门
     */

    public void save(Department department ){
        department.setId(idWorker.nextId());
        departmentDao.save(department);}

}
