package com.ihrm.common.service;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author misterWei
 * @create 2019年08月17号:18点53分
 * @mailbox mynameisweiyan@gmail.com
 */

/**
 * 用来响应 多租户,在Service中固定一个通用的根据企业查的方法
 * @param <T>
 */
@NoArgsConstructor
public class BaseService<T> {

    protected Specification getComId(String companyId){
       return   new Specification<T>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder cd) {

                return cd.equal(root.get("companyId").as(String.class),companyId);
            }
        };
    }
}
