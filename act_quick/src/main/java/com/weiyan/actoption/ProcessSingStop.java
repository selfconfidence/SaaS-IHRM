package com.weiyan.actoption;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.weiyan.actoption
 * @date 2019/7/21 16:48
 * 1.对单个流程进行挂起,挂起的操作就是不允许任何人再次操作此流程
 * 2,挂起之后还能进行
 *
 * 问题:
 *  如果挂起的任务流程被执行就会报错,再或者就再次激活该流程
 */
public class ProcessSingStop {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //如果要挂起单个流程就需要使用到 RunTimeService
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        //获取到当前执行的流程
        ProcessInstance myHoliday = runtimeService.createProcessInstanceQuery().processDefinitionKey("myHoliday").singleResult();
       //true挂起  //false未挂起
        boolean suspended = myHoliday.isSuspended();
        if (suspended) {
            //测试用例如果如果挂起就继续
            runtimeService.activateProcessInstanceById(myHoliday.getId());
            System.out.println("激活:"+myHoliday.getProcessDefinitionKey());
        }else{
          //测试用例如果活动就挂起
            runtimeService.suspendProcessInstanceById(myHoliday.getId());
            System.out.println("挂起:"+myHoliday.getProcessDefinitionKey());
        }
    }
}
