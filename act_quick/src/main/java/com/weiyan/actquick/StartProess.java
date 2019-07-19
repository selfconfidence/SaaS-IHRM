package com.weiyan.actquick;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title web_service
 * @package com.weiyan.actquick
 * @date 2019/7/19 18:13
 *
 * 启动一个work 流程
 */
public class StartProess {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //获取运行表操作service对象
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        ProcessInstance myholiday = runtimeService.startProcessInstanceByKey("myHoliday");
        System.out.println("流程定义ID\r"+myholiday.getProcessDefinitionId());
        System.out.println("流程实例ID\r"+myholiday.getId());
        System.out.println("当前活动ID\r"+myholiday.getActivityId());
        }



}
