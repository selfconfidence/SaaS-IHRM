package com.weiyan.actquick;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
/**
 * @author mister_wei
 * @version 1.1.1
 * @title web_service
 * @package com.weiyan.actquick
 * @date 2019/7/19 11:40
 * //初始化工作流方式
 */
public class ActivitiInit {
    //对基础工作流操作的第一步: 部署工作流系统产生系统表
    public static void main(String[] args) {
        //获取配置文件信息  第一种方式指定获取
       // ProcessEngine processEngine = ProcessEngines.getProcessEngine("activiti.cfg.xml");

        //获取配置文件信息  第二种方式指定获取 要求 名称必须一直, 存放路径也是
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        //单个文件部署方式

        Deployment deploy = repositoryService.createDeployment()
                .addInputStream("holiday.bpmn",ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/holiday.bpmn"))
                .addInputStream("holiday.png",ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/holiday.png"))
                .name("请假流程单")
                .key("myHoliday")
                .deploy();
        System.out.println("流程部署ID\r"+deploy.getId());
        System.out.println("流程部署名称\r"+deploy.getName());
        /**
         //压缩包方式部署
         InputStream resourceAsStream = ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/holidayBPMN.zip");
         ZipInputStream zipInputStream = new ZipInputStream(resourceAsStream);
         Deployment deploy1 = repositoryService.createDeployment().addZipInputStream(zipInputStream)
         .name("请假流程单申请")
         .deploy();
         System.out.println("流程部署ID\r"+deploy1.getId());
         System.out.println("流程部署名称\r"+deploy1.getKey());
          */
    }
}
