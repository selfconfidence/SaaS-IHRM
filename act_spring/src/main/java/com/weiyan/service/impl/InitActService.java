package com.weiyan.service.impl;

import org.activiti.engine.HistoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.weiyan.service.impl
 * @date 2019/7/30 11:25
 * //整合之后测试的是否整合成功~
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:activiti-spring.xml")
public class InitActService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HistoryService historyService;

    @Test
    public void initAct(){
        jdbcTemplate.toString();
        System.out.println("部署对象:"+historyService);
        }

}
