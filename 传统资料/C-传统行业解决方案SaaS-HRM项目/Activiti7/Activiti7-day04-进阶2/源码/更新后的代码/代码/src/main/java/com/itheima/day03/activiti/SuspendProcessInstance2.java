package com.itheima.day03.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

/**
 *  单个流程实例挂起与激活
 *
 *
 */
public class SuspendProcessInstance2 {

    /*public static void main(String[] args) {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3.查询流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("2501").singleResult();

        //4.得到当前流程定义的实例是否都为暂停状态
        boolean suspended = processInstance.isSuspended();

        String processInstanceId = processInstance.getId();
        //5.判断
        if(suspended){
            //说明是暂停，就可以激活操作
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("流程："+processInstanceId+"激活");
        }else{
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("流程："+processInstanceId+"挂起");
        }

    }
*/

    /**
     * 当流程实例2501,已经处于挂起状态，如果此时要让该实例继续执行，问题是:是否可以成功？
     * 如果不能执行，是否会抛出异常？ActivitiException： Cannot complete a suspended task
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3.查询当前用户的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("zhangsan")
                .singleResult();

        //4.处理任务,结合当前用户任务列表的查询操作的话,任务ID:task.getId()
        taskService.complete(task.getId());

        //5.输出任务的id
        System.out.println(task.getId());
    }
}
