package com.weiyan.actvariable;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.weiyan.actvariable
 * @date 2019/7/23 9:19
 * //制定新的bpmn 符号建模  使用uml表达式对workfowl连线流程进行活性判断
 */
public class ActProVarObj {
    private  static Map resultMap = null;
    private static HolidayEntity holidayEntity = null;

    static {
        resultMap = new HashMap();
        holidayEntity = new HolidayEntity();
        //添加流程条件变量
        holidayEntity.setId("1024");
        holidayEntity.setHolidayNum(3F);
        //添加流程派发人
        resultMap.put("staff","content");
        resultMap.put("depManager","bumenjingli");
        resultMap.put("boss","zongjingli");
        resultMap.put("hr","hr");
        resultMap.put("holiday",holidayEntity);

    }

    public static void main(String[] args) {
        //获取总操作对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

      ActProVarObj actProVarObj = new ActProVarObj();
      //用来部署流程的
        actProVarObj.deploy(defaultProcessEngine);
        //用来启动流程实例的
        actProVarObj.processInstance(defaultProcessEngine);
        //用来查看当前任务,以及完成任务的
        actProVarObj.taskOver(defaultProcessEngine);



    }
  public void taskOver(ProcessEngine defaultProcessEngine){
      //查看当前人员任务示例,并完成节点任务
      TaskService taskService = defaultProcessEngine.getTaskService();
      Task context = taskService.createTaskQuery().taskAssignee("content").singleResult();
      if(context != null){
          System.err.println("员工任务:"+context.getName());
          taskService.complete(context.getId());
          System.err.println("员工任务完善:"+context.getAssignee());
      }
      context = taskService.createTaskQuery().taskAssignee("bumenjingli").singleResult();
      if(context != null) {
          System.err.println("部门经理任务:"+context.getName());
          taskService.complete(context.getId());
          System.err.println("部门经理任务完善:"+context.getAssignee());
      }



     //流程测试,如果holiday.num 大于3 就需要通过总经理流程,小于等于3 直接交由人资归档
      context = taskService.createTaskQuery().taskAssignee("zongjingli").singleResult();
      if(context != null){
          System.err.println("总经理任务:"+context.getName());
          taskService.complete(context.getId());
          System.err.println("总经理任务完善:"+context.getAssignee());
      }



      context = taskService.createTaskQuery().taskAssignee("hr").singleResult();
      System.err.println("HR任务:"+context.getName());
      taskService.complete(context.getId());
      System.err.println("HR任务完善:"+context.getAssignee());

  }

    public void processInstance(ProcessEngine defaultProcessEngine){
        //获取启动流程对象
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        ProcessInstance holidayQuery = runtimeService.startProcessInstanceByKey("holiday_query",holidayEntity.getId(), resultMap);
        System.err.println("启动流程示例" +holidayQuery.getName());
    }

    public void deploy(ProcessEngine defaultProcessEngine){
        //获取部署对象
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        //添加流程信息
        Deployment deploy = repositoryService.createDeployment()
                .addInputStream("holiday.bpmn", ActProVarObj.class.getClassLoader().getResourceAsStream("diagram/holiday1.bpmn"))
                .addInputStream("holiday.png", ActProVarObj.class.getClassLoader().getResourceAsStream("diagram/holiday1.png"))
                .name("条件请假流程单申请")
                .key("holiday_query")
                //部署
                .deploy();
        System.err.println("部署请假单成功:"+deploy.getName());
    }
}
