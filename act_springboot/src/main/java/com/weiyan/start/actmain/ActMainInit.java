package com.weiyan.start.actmain;

import com.weiyan.start.utils.SecurityUtils;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.weiyan.start.actmain
 * @date 2019/7/31 17:28
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ActMainInit {

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    //启动流程
    @Test
    public void processStart(){
        securityUtils.logInAs("other");
        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
        for (ProcessDefinition processDefinition : processDefinitionPage.getContent()) {
            System.out.println("流程key: "+ processDefinition.getKey());
        }
        System.out.println("总条数: "+processDefinitionPage.getTotalItems());
    }
    //查询当前任务组
    @Test
    public void taskQuery(){
        //这句话是主要的,拾取任务以及完成任务都是根据当前用户进行操作的
        securityUtils.logInAs("other");
        //查询任务                                              //根据指定的用户组去管理
        Task task = taskRuntime.create(TaskPayloadBuilder.create().withGroup("activitiTeam").build());
        //拾取任务
        taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
        //完成任务;
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
        System.out.println(task);
    }
}
