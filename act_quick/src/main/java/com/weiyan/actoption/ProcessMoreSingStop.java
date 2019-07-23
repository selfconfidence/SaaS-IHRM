package com.weiyan.actoption;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.weiyan.actoption
 * @date 2019/7/21 16:48
 * 1.对整个流程进行挂起,挂起的操作就是不允许任何人再次操作此流程
 * 2,挂起之后还能进行
 *
 * 问题:
 *  如果挂起的任务流程被执行就会报错,再或者就再次激活该流程
 */
public class ProcessMoreSingStop {
    public static void main(String[] args) {

        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        //如果要挂起整个流程就需要使用到 RepositoryService
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        //获取到当前执行的流程
        ProcessDefinition myHoliday = repositoryService.createProcessDefinitionQuery().processDefinitionKey("myHoliday").singleResult();

        //true挂起  //false未挂起
        boolean suspended = myHoliday.isSuspended();
        if (suspended) {
            //1, 参数1 为该启动流程的ID  2 是否有效 , 3 时间戳,固定null值
          repositoryService.activateProcessDefinitionById(myHoliday.getId(),true,null);
            System.out.println("激活:"+myHoliday.getKey());
        }else{
            repositoryService.suspendProcessDefinitionById(myHoliday.getId(),true,null);
            System.out.println("挂起:"+myHoliday.getKey());
        }
    }
}
