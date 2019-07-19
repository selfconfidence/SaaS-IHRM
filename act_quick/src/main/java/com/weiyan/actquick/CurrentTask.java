package com.weiyan.actquick;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title web_service
 * @package com.weiyan.actquick
 * @date 2019/7/19 19:28
 *
 * 查询当前人员任务的结点,同时解决问题走一段完整的流程
 */
@SuppressWarnings("all")
public class CurrentTask {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //获取当前任务的service 对象 TaskService
        TaskService taskService = defaultProcessEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("myHoliday").taskAssignee("zhangsan").singleResult();
        System.out.println("当前任务名称\r"+task.getName());
        System.out.println("当前任务负责人\r"+task.getAssignee());
        System.out.println("当前任务ID\r"+task.getId());
       //完成第一个任务
        taskService.complete(task.getId());


        //查询第二个任务  lisi的任务
         task = taskService.createTaskQuery().taskAssignee("lisi").processDefinitionKey("myHoliday").singleResult();
        System.out.println("当前任务名称\r"+task.getName());
        System.out.println("当前任务负责人\r"+task.getAssignee());
        System.out.println("当前任务ID\r"+task.getId());
        //完成第二个任务
        taskService.complete(task.getId());

        //查询第三个任务  wangwu的任务
         task = taskService.createTaskQuery().taskAssignee("wangwu").processDefinitionKey("myHoliday").singleResult();
        System.out.println("当前任务名称\r"+task.getName());
        System.out.println("当前任务负责人\r"+task.getAssignee());
        System.out.println("当前任务ID\r"+task.getId());
        //完成第三个任务
        taskService.complete(task.getId());

        //走到现在 bpmn 流程节点已经跑完,到此结束工作流.

    }
}
