package com.weiyan.actadvanced;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.weiyan.actadvanced
 * @date 2019/7/22 9:49
 * @head  分配任务负责人
 * // 针对业务场景: 在实际应用操作activiti的时候 我们不可能在bpmn建模得时候指定派发人,肯定需要在编码中指定派发人.
 * 1.技术实现:  1.1 UML表达式,${} 在bnpm中去指定变量 通过启动流程的步骤中去进行表达式分配(activiti 支持两个 UEL 表达式：UEL-value 和 UEL-method)
 *              1.2 在bpmn中固定去指定分配任务.
 *              1.3 通过事件去指定
 */
public class ActStartProcessAssignee {


    // 这种方式最常见, 在bnpm建模图中去添加UML表达式,通过启流程实例去指定task分配
    @Test
    public void umlAssignee(){
       Map<String,Object> params = new HashMap();
       params.put("a","王五");
        params.put("b","李四");
        params.put("c","张三");
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        //启动一个流程, 使用模型key, 指定uml的传参填充  这就是流程变量
        ProcessInstance myHoliday = runtimeService.startProcessInstanceByKey("myHoliday", params);
        System.out.println(myHoliday.getBusinessKey());
    }


    //延申红黑树算法?
  /*  public static void main(String[] args) {
        int[] arr = {21,321,34,1,23,56};
        int i,j;
        for (i = 0 ; i < arr.length;i++){
            for (j = 0 ; j< arr.length - 1 -i;j++){
                if (arr[j] > arr[j+1]){
                    int temp = arr[j+1];
                    arr[j+1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        for (int i1 : arr) {
            System.out.println(i1);
        }
    }*/
}
