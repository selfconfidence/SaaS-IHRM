package com.weiyan.actadvanced;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import java.util.HashMap;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title 对actitivi的工作流程中的变量配置作出详细说明
 * @package com.weiyan.actadvanced
 * @date 2019/7/24 9:32
 *
 *  此项不常用,但是也需要了解设置
 *设置作用域为任务的 local 变量，每个任务可以设置同名的变量，互不影响
 *local作用域限制与当前节点使用范围
 *     local变量 任务办理时设置 runTime也可以进行设置但是不建议使用,对节点变量不够明确范围限制.
 *
 *
 *
 */
public class ProcessVarsLocal {

    public void processVarLoca(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();

        Task task = taskService.createTaskQuery().taskDefinitionKey("key").singleResult();
        //设置单个local值
        taskService.setVariableLocal(task.getId(),"name","value");
        //设置多个local值
        taskService.setVariablesLocal(task.getId(),new HashMap());
    }


}
