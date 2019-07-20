package com.weiyan.actoption;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.weiyan.actoption
 * @date 2019/7/20 11:09
 *
 * // 用来获取actitivi流程资源的 bpmn 以及 png 的数据 输出到指定目录
 */
public class ResourceGetFile {
    //两种实现方式
    //1. 自己写底层编码根据条件获取到当前数据信息打成字节流存储到指定位置
    //2. 使用activiti的api 提供到快速指定到对象流存储到指定位置
    // 优选 第2种方式;

    public static void main(String[] args) throws Exception {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //获取到持久存储时服务
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        //获取到流程定义实例
        ProcessDefinition sourceStatic = repositoryService.createProcessDefinitionQuery().processDefinitionKey("myHoliday").singleResult();
        String bnpmPng = sourceStatic.getDiagramResourceName();
        String bnpmName = sourceStatic.getResourceName();
        //使用repositoryService 获取到非结构化数据的字节流
        //getResourceAsStream 两个参数  第一个 流程部署实例ID 通过查询出的ProcessDefinition 获取, 第二个资源名称
        //blob 大对象数据
        InputStream bnpmPngIoRed = repositoryService.getResourceAsStream(sourceStatic.getDeploymentId(), bnpmPng);
        InputStream bnpmNameIoRed = repositoryService.getResourceAsStream(sourceStatic.getDeploymentId(), bnpmName);
        //写入资源位置
        OutputStream bnpmPngIoOut = new FileOutputStream("H:\\beidaSoftCode\\"+bnpmPng);
        OutputStream bnpmNameIoOut = new FileOutputStream("H:\\beidaSoftCode\\"+bnpmName);

        //原生的方式比较靠谱点.
        byte arr[] = new byte[1024];
        int len;
        while ( (len = (bnpmNameIoRed.read(arr))) != -1){
            bnpmNameIoOut.write(arr,0,len);
        }

        while ( (len = (bnpmPngIoRed.read(arr))) != -1){
            bnpmPngIoOut.write(arr,0,len);
        }
        bnpmNameIoOut.flush();
        bnpmPngIoOut.flush();
        bnpmNameIoOut.close();
        bnpmPngIoOut.close();
        bnpmPngIoRed.close();
        bnpmNameIoRed.close();
    }
}
