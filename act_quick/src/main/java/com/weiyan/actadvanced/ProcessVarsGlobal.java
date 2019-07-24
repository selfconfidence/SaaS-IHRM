package com.weiyan.actadvanced;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.HashMap;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title 对actitivi的工作流程中的变量配置作出详细说明
 * @package com.weiyan.actadvanced
 * @date 2019/7/24 9:32
 *   全局变量分为 2大类   分别2大类对应 4小类
 *
 *   2大类 : 1.1,启动流程中设置    1.1.1,初始化启动流程初始化去设置变量
 *                                 1,1,2,通过流程实例去设置变量
 *
 *          1.2,任务中去设置       1.2.1, 通过任务办理完成同时去设置变量
 *                                 1.2.2, 通过任务实例去设置变量
 *
 */
public class ProcessVarsGlobal {

    //1.1.1 初始化启动流程示例去设置变量
    public void startGlobalProcessInstance(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        //第一个参数是对应部署唯一key  第二个参数是 业务系统关联的ID,第三个参数就是全局设置参
        runtimeService.startProcessInstanceByKey("key","buss_id",new HashMap<String, Object>());
    }
    //1.1.2 通过流程实例去设置变量
    public void startGlobalProcessInstance1(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("key").singleResult();
        //设置单个流程变量  第一个参数具体的实例id
        runtimeService.setVariable(processInstance.getId(),"name","value");

        //设置多个流程变量 第一个参数具体的实例id
        runtimeService.setVariables(processInstance.getId(),new HashMap());
    }
    //1.2.1 通过任务办理完成同时去设置变量
    public void taskEndSettingVar(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        Task task = defaultProcessEngine.getTaskService()
                .createTaskQuery()
                .processDefinitionKey("key").singleResult();
        TaskService taskService = defaultProcessEngine.getTaskService();
        //第一个参数为 任务实际任务ID,第二个参数就是流程参数
        taskService.complete(task.getId(),new HashMap<String, Object>());
    }
    //1.2.2 通过任务实例去设置变量
    public void taskEndSettingVar1(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        Task task = defaultProcessEngine.getTaskService().createTaskQuery().taskDefinitionKey("key").singleResult();
        TaskService taskService = defaultProcessEngine.getTaskService();
        //第一个参数为  任务实例id
        taskService.setVariable(task.getId(),"name","value");
    }
}
