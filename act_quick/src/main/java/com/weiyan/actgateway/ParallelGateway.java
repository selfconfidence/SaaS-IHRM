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
 * @date 2019/7/29 16:25
 *
 * //这是一个同行网关的实例
 * act_hi_actinst    act_ru_task
 *
 * 业务需求提示: 例如有一个流程必须需要两个人一起同意才可以.
 */
public class ParallelGateway {
    //部署工作流程
    @Test
    public void   deploymentProcess(){

        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addInputStream("parallel.bpmn", ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/gateway/parallel.bpmn"))
                .addInputStream("parallel.png",ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/gateway/parallel.png"))
                .addInputStream("parallel.xml.uml",ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/gateway/parallel.xml.uml"))
                .name("同行网关测试")
                .key("parallel")
                .deploy();
    }

    //启动某一个流程实例并且指定多用户
    @Test
    public void startProcess(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        Map userMap = new HashMap();
        userMap.put("a","zhangsan");
        userMap.put("b","lisi");
        //其实在actitivi中启动实例流程的时候就能够指定多用户组.
        userMap.put("c","wangwu");
        userMap.put("d","zhaoliu");
        runtimeService.startProcessInstanceByKey("parallel","1",userMap);
    }


    //完成对应的任务信息
    @Test
    public void taskEnd(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("parallel").taskAssignee("wangwu").singleResult();
        taskService.complete(task.getId());
    }
}
