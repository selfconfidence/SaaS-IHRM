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
 * @date 2019/7/21 16:30
 * 1.让自己的业务系统与actitivi系统进行连接
 */
public class BusinessKeyActitivi {

    //启动一个流程这个流程与业务系统的表进行关联
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        // 1.是一个流程实例的key
        // 2. 业务系统的标识与其关联  act_ru_execution
        ProcessInstance myHoliday = runtimeService.startProcessInstanceByKey("myHoliday", "1024");
        System.out.println("流程启动的ID:"+myHoliday.getId());
        System.out.println("业务流程系统的标识:"+myHoliday.getBusinessKey());
    }
}
