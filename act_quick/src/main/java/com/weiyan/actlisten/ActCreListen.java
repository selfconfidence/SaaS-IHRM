package com.weiyan.actlisten;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.weiyan.actlisten
 * @date 2019/7/22 14:57
 * // 这是一个创建任务之后所能触发得监听器
 */
public class ActCreListen  implements TaskListener {


    public void notify(DelegateTask delegateTask) {
        System.out.println(delegateTask.getAssignee());
    }
}
