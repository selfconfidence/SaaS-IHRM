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
 * @date 2019/7/29 17:26
 *
 * 包含网关:
 * 排它网关得条件特性
 * 并行网关得并行特性
 *
 * 以上两个特性得结合体,根据条件能够并行执行流程
 */
public class InclusiveGateway {
    //inclusive

    //部署工作流程
    @Test
    public void deploymentProcess() {

        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addInputStream("inclusive.bpmn", ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/gateway/inclusive.bpmn"))
                .addInputStream("inclusive.png", ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/gateway/inclusive.png"))
                .addInputStream("inclusive.xml.uml", ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/gateway/inclusive.xml.uml"))
                .name("体检申请单")
                .key("inclusive")
                .deploy();
    }
    //启动某一个流程实例并且指定条件输出
    @Test
    public void startProcess() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        Map userMap = new HashMap();
        userMap.put("a", "zhangsan");
        userMap.put("b", "lisi");
        //其实在actitivi中启动实例流程的时候就能够指定多用户组.
        userMap.put("c", "wangwu");
        userMap.put("d", "zhaoliu");
        userMap.put("e", "tianqi");
        userMap.put("roleNum", "0");
        runtimeService.startProcessInstanceByKey("inclusive", "1", userMap);
    }

    //完成对应的任务信息
    @Test
    public void taskEnd(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("inclusive").taskAssignee("zhangsan").singleResult();
        taskService.complete(task.getId());
    }
}
