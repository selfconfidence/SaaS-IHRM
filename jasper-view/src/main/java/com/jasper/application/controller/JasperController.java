package com.jasper.application.controller;

import com.jasper.application.entity.User;
import com.jasper.application.entity.UserChartsCount;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

/**
 * @author misterWei
 * @create 2019年11月28号:22点28分
 * @mailbox mynameisweiyan@gmail.com
 */
@RestController
public class JasperController {

    /**
     * 关于传参数据补充
     * @param request
     * @param response
     */

    @RequestMapping("/japser_par")
    public void japserPar(HttpServletRequest request, HttpServletResponse response) {
        try {
            Resource resource = new ClassPathResource("templates/jasper_parms.jasper");
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("userName", "张三");
            objectObjectHashMap.put("bro", new Date());
            objectObjectHashMap.put("age", 20);
            objectObjectHashMap.put("gender", "男人");

            JasperPrint jasperPrint = JasperFillManager.fillReport(resource.getInputStream(), objectObjectHashMap, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用数据库sql进行数据补充
     * @param request
     * @param response
     *
     * 优点: 采用Sql 获取数据,比较灵活
     * 缺点: 在Java 程序中 对数据库的资源链接不友好,不能够优雅释放.
     */

    @RequestMapping("/japser_source")
    public void japserSource(HttpServletRequest request, HttpServletResponse response) {
        try {
            Resource resource = new ClassPathResource("templates/jasper_dataSource.jasper");
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("status", "1");
            objectObjectHashMap.put("page", 0);
            objectObjectHashMap.put("size", 10);
            JasperPrint jasperPrint = JasperFillManager.fillReport(resource.getInputStream(), objectObjectHashMap, getConnection());
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
           return DriverManager.getConnection("jdbc:mysql://47.93.63.135:3306/ay_work","root","XZHkj123");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 采用JavaBean方式补充.
     */

    @RequestMapping("/japser_bean")
    public void japserBean(HttpServletRequest request, HttpServletResponse response){

        try {
            Resource resource = new ClassPathResource("templates/jasper_bean.jasper");
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(getUsers());
            objectObjectHashMap.put("author", "mister_Wei");
            JasperPrint jasperPrint = JasperFillManager.fillReport(resource.getInputStream(), objectObjectHashMap, jrBeanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public List<User> getUsers(){
        LinkedList list = new LinkedList();
        for (int i = 0; i < 10; i++) {
            User user = new User(i,"用户"+1,"阿里巴巴","研发部","臭王凯");
        list.addLast(user);
        }
        return list;
    }

    /**
     * 采用分组维度统计方式补充.
     */

    @RequestMapping("/japser_group")
    public void japserGroup(HttpServletRequest request, HttpServletResponse response){

        try {
            Resource resource = new ClassPathResource("templates/jasper_group.jasper");
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(getGroup());
            objectObjectHashMap.put("author", "mister_Wei");
            JasperPrint jasperPrint = JasperFillManager.fillReport(resource.getInputStream(), objectObjectHashMap, jrBeanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<User> getGroup(){
        LinkedList list = new LinkedList();
        for (int i = 0; i < 10; i++) {
            User user = new User(i,"用户"+1,"研发","阿里巴巴","臭王凯");
            list.addLast(user);
        }

        for (int i = 0; i < 8; i++) {
            User user = new User(i,"用户"+1,"产品","腾讯","666");
            list.addLast(user);
        }
        return list;
    }

    /**
     * 采用分组维度统计方式补充.
     */

    @RequestMapping("/japser_charts")
    public void japserCharts(HttpServletRequest request, HttpServletResponse response){

        try {
            Resource resource = new ClassPathResource("templates/japser_charts.jasper");
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(getCharts());
            JasperPrint jasperPrint = JasperFillManager.fillReport(resource.getInputStream(), objectObjectHashMap, jrBeanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 采用父子报表图形展示
     */

    @RequestMapping("/japser_parent")
    public void japserParent(HttpServletRequest request, HttpServletResponse response){

        try {
            Resource resource = new ClassPathResource("templates/japser_parent.jasper");
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("subList",getCharts());
            objectObjectHashMap.put("subPath",new ClassPathResource("templates/japser_child_charts.jasper").getPath());
            JasperPrint jasperPrint = JasperFillManager.fillReport(resource.getInputStream(), objectObjectHashMap, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Collection<?> getCharts() {
        LinkedList list = new LinkedList();
            UserChartsCount chartsCount = new UserChartsCount("阿里",500L);
        UserChartsCount chartsCount1 = new UserChartsCount("腾讯",300L);
        UserChartsCount chartsCount2 = new UserChartsCount("百度",800L);
        list.addFirst(chartsCount);
        list.addFirst(chartsCount1);
        list.addFirst(chartsCount2);

        return list;
    }

    /**
     * 采用父子报表图形展示
     */

    @RequestMapping("/{id}/pdf")
    public void pdf(@PathVariable("id") String id, HttpServletResponse response){

        try {
            Resource resource = new ClassPathResource("templates/profile.jasper");
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("userId",id);
            objectObjectHashMap.put("staffPhoto","https://nuxt-vue.oss-cn-beijing.aliyuncs.com/2018-09-18_201715.png");
            objectObjectHashMap.put("userName","张三");
            objectObjectHashMap.put("workCart","北京");
            objectObjectHashMap.put("workNum","77years");
            objectObjectHashMap.put("workType","123123");
            objectObjectHashMap.put("workContent","你猜我画");

            JasperPrint jasperPrint = JasperFillManager.fillReport(resource.getInputStream(), objectObjectHashMap, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
