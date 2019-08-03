package com.ihrm.company.service;

import com.ihrm.common.entity.PageResult;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.CompanyDao;
import com.ihrm.domain.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author misterWei
 * @create 2019年08月03号:17点12分
 * @mailbox mynameisweiyan@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyService {
    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private IdWorker idWorker;

    public Company findById(String id) {
        return companyDao.findById(id).get();
    }

    public void save(Company company) {
        company.setId(idWorker.nextId());
        company.setAuditState("0");
        company.setState(0);
        companyDao.save(company);
    }

    public void update(String id, Company company) {
        company.setId(id);
        companyDao.saveAndFlush(company);
    }

    public void deleteById(String id) {
        companyDao.deleteById(id);
    }

    public PageResult findByPageList(Company company, int page, int size) {
        PageResult pageResult = new PageResult();
        Page<Company> all = companyDao.findAll(PageRequest.of(page, size));
        pageResult.setTotal(all.getTotalElements());
        pageResult.setRows(all.getContent());
        return pageResult;

    }
}
