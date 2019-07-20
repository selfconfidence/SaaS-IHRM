package com.weiyan.actoption;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title web_service
 * @package com.weiyan.actoption
 * @date 2019/7/20 10:40
 *
 * 删除流程操作 以及删除的注意事项
 *
 * // 1. deleteDeployment("1"); 用来删除一个刚部署还未启动的流程节点
 * // 2.deleteDeployment("1",true); 用来强制删除一个启动并且正在执行的流程节点
 *
 */
public class ProcessDefinitionDelete {
    public static void main(String[] args) {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
       // repositoryService.deleteDeployment("1",true);   //建议管理员删除, 这是强制删除一个执行中的流程,删除所有关于流程的节点信息.
        repositoryService.deleteDeployment("10001");// 删除一个刚部署还未启动流程的节点流程

    }
}
