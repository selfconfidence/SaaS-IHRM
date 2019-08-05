package com.itheima.day03.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

/**
 *  启动流程实例，添加进businessKey
 *
 *  本质：act_ru_execution表中的businessKey的字段要存入业务标识
 */
public class BusinessKeyAdd {


    public static void main(String[] args) {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到RuntimeService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3.启动流程实例,同时还要指定业务标识businessKey  它本身就是请假单的id
        //第一个参数：是指流程定义key
        //第二个参数：业务标识businessKey
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday", "1001");

        //4.输出processInstance相关的属性,取出businessKey使用:processInstance.getBusinessKey()
        System.out.println(processInstance.getBusinessKey());

    }
}
