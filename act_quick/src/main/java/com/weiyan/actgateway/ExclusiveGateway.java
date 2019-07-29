package com.weiyan.actgateway;

import com.weiyan.actquick.ActivitiInit;
import org.activiti.engine.*;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.weiyan.actgateway
 * @date 2019/7/29 16:48
 *
 * 排它网关,如果流程线条件都为true的话 只走一个(当初画图拉线排名最前的线先走),如果条件都为false的话(报错)
 */
public class ExclusiveGateway {

    //部署工作流程
    @Test
    public void deploymentProcess() {

        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addInputStream("exclusive.bpmn", ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/gateway/exclusive.bpmn"))
                .addInputStream("exclusive.png", ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/gateway/exclusive.png"))
                .addInputStream("exclusive.xml.uml", ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/gateway/exclusive.xml.uml"))
                .name("离职申请单")
                .key("exclusive")
                .deploy();
    }

    //启动某一个流程实例并且指定多用户
    @Test
    public void startProcess() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        Map userMap = new HashMap();
        userMap.put("a", "zhangsan");
        userMap.put("b", "lisi");
        //其实在actitivi中启动实例流程的时候就能够指定多用户组.
        userMap.put("c", "wangwu");

        userMap.put("role", "8");


        runtimeService.startProcessInstanceByKey("exclusive", "1", userMap);
    }


    //完成对应的任务信息
    @Test
    public void taskEnd(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("exclusive").taskAssignee("lisi").singleResult();
        taskService.complete(task.getId());
    }

}
