package com.yzg.codes.core;

import com.yzg.codes.utils.FileUtils;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author misterWei
 * @create 2019年12月08号:00点33分
 * @mailbox mynameisweiyan@gmail.com
 * //使用Freemark的方式去生成代码
 * 核心数据输出实现
 */
public class Generator {

    private String templatePath;
    private String outPutName;
    private Configuration configuration;

    public Generator(String templatePath, String outPutName) throws IOException {
        this.templatePath = templatePath;
        this.outPutName = outPutName;
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateLoader(new FileTemplateLoader(new File(templatePath)));
    }

    /**
     * 把所有的模板内容查询出来
     */
    public void processFileGenerate(Map dataMap) throws Exception {
       //拿到模板下的所有文件
        List<File> fileList = FileUtils.searchAllFile(new File(templatePath));
        fileList.forEach((file -> {
            //根据查询的模板路径进行替换操作
            String newFileTemplate = file.getAbsolutePath().replace(this.templatePath, "");
            try {
                String temp = newFileTemplate;
                newFileTemplate = processPathGenerate(newFileTemplate,dataMap);
                // 加载模板内容打印到outPath路径中
                File outAbs = FileUtils.mkdir(outPutName, newFileTemplate);
                Template template = configuration.getTemplate(temp);
                FileWriter fileWriter = new FileWriter(outAbs);
                template.process(dataMap,fileWriter);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }));

    }

    private String processPathGenerate(String newFileTemplate, Map dataMap) throws Exception {
        StringWriter stringWriter = new StringWriter();
        Template template = new Template("ts",new StringReader(newFileTemplate),configuration);
        template.process(dataMap,stringWriter);
        return  stringWriter.toString();
    }

/*    public static void main(String[] args) throws Exception {
        String templatePath = "G:\\study-target\\Saas-IHRM\\IHRM项目\\day13-代码生成器实现\\gen\\模板\\";
        String outPath = "G:\\study-target\\Saas-IHRM\\IHRM项目\\day13-代码生成器实现\\gen\\codes\\";
        Map dataMap = new HashMap();
        dataMap.put("ClassName",10);
        Generator generator = new Generator(templatePath,outPath,dataMap);
        generator.processFileGenerate();
    }*/



}
