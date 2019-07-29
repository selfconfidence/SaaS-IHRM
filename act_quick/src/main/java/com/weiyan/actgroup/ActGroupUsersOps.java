package com.weiyan.actgroup;

import com.weiyan.actquick.ActivitiInit;
import org.activiti.engine.*;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title Gropu在Activiti中的引用规则
 * @package com.weiyan
 * @date 2019/7/24 10:36
 *
 * 业务场景: 请假单--> 提交请假单,部门经理如果只有一个那么就无法请假,所有就有了userGroup的概念,多用户处理任务
 * 多人处理任务
 */
public class ActGroupUsersOps {


    @Test
    public void deleteDep(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        repositoryService.deleteDeployment("45001",true);
    }
    //部署工作流程
     @Test
    public void   deploymentProcess(){

         ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
         RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
         repositoryService.createDeployment()
                 .addInputStream("holiday.bpmn", ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/group/holiday.bpmn"))
                 .addInputStream("holiday.png",ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/group/holiday.png"))
                 .addInputStream("holiday.xml.uml",ActivitiInit.class.getClassLoader().getResourceAsStream("diagram/group/holiday.xml.uml"))
                 .name("请假组别流程单")
                 .key("myProcess_1")
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
         userMap.put("group","wangwu,zhaoliu");
         runtimeService.startProcessInstanceByKey("myProcess_1","1",userMap);
     }


     //完成对应的任务信息
     @Test
    public void taskEnd(){
         ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
         TaskService taskService = defaultProcessEngine.getTaskService();
         Task task = taskService.createTaskQuery().processDefinitionKey("myProcess_1").taskAssignee("lisi").singleResult();
         taskService.complete(task.getId());
     }

     //对相应的任务进行拾取 然后归还
    @Test
    public void taskComOrGive(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("myProcess_1")
                //查询组任务的时候需要特别注意 taskCandidateUser 需要用这种方法去查询
                .taskCandidateUser("zhaoliu")
                .singleResult();
        //如果当前有组任务的时候就需要拾取之后才能进行处理
        if (task != null) {
            // claim 就是对任务的拾取 第一个参数为 任务的id,第二个参数为具体的拾取人
            taskService.claim(task.getId(),"zhaoliu");
            System.out.println(task.getName());
        }
        //如果拾取之后 组任务就被取消成为个人任务

        //如果想归还组任务?
        task = taskService.createTaskQuery().taskAssignee("zhaoliu").singleResult();
        if(task != null){
            //此步操作就是归还组任务
            taskService.setAssignee(task.getId(),null);
        }

    }

}
