package com.weiyan.actquick;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;

import java.util.List;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title web_service
 * @package com.weiyan.actquick
 * @date 2019/7/19 18:10
 * 工作流引擎的查询
 */
public class TaskLook {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //查看所有工作流程   RepositoryService 是用来查看的service
        /*
          RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        for (Deployment deployment : list) {
            System.out.println("流程部署ID\r"+deployment.getId());
            System.out.println("流程部署名称\r"+deployment.getName());
            System.out.println("流程部署唯一key\r"+deployment.getKey());
        }

         */
       //查看单个流程
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeploymentQuery()
                //根据指定key查询
                .deploymentKey("myHoliday")
                //查询一条数据
                .singleResult();
        System.out.println("流程部署ID\r"+deployment.getId());
        System.out.println("流程部署名称\r"+deployment.getName());
        System.out.println("流程部署唯一key\r"+deployment.getKey());
    }

}
