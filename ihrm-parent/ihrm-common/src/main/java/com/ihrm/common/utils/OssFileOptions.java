package com.ihrm.common.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.ihrm.common.utils
 * @date 2019/8/5 10:27
 * //使用阿里的远程调用服务对该
 * http 用来在线预览,https是用来下载
 */
public class OssFileOptions  {
    private static String accessKeyId = null;
    private static String accessKeySecret = null;
    private static String url = null;
    private static String bucket=null;
    private static String uri= null;
    private static OSS oss = null;
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,20,200, TimeUnit.MINUTES,new LinkedBlockingDeque<Runnable>(5));


    static {
        Properties properties = new Properties();
        try {
            properties.load(OssFileOptions.class.getResourceAsStream("/application.properties"));
            accessKeyId = properties.getProperty("accessKeyId");
            accessKeySecret = properties.getProperty("accessKeySecret");
            url = properties.getProperty("url");
            bucket = properties.getProperty("bucket");
            uri = properties.getProperty("uri");
            oss = new OSSClient(url,accessKeyId,accessKeySecret);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //上传文件到阿里OSS对象空间
 public static  Map  ossFileUpload(InputStream inputStream,String fileNameSubfix){
     Map resultMap = new HashMap();
     //文件后缀
     String subFixName = fileNameSubfix.substring(fileNameSubfix.lastIndexOf("."),fileNameSubfix.length());
     //生成的文件名称
     String fileName  = UUID.randomUUID().toString();
     //生成文件的完整名称路径
     String fileLocal = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"/"+ fileName+subFixName;
     // 创建上传文件的元信息，可以通过文件元信息设置HTTP header。
     ObjectMetadata meta = new ObjectMetadata();
     meta.setContentEncoding("utf-8");
     //这里需要待完善,文件属于活性的,不能指定死类型
     Path path = Paths.get(fileNameSubfix);

     try {
         meta.setContentType(Files.probeContentType(path));
     } catch (IOException e) {
       meta.setContentType(null);
     }

     try {
            /*File file = new File("C:\\Users\\Administrator\\Pictures\\Feedback\\{3AD8DD58-BEBF-4870-BF3D-DDADAC24A22C}\\Capture001.png");
            InputStream inputStream = new FileInputStream(file);*/
            //不影响主业务流程
         threadPoolExecutor.execute(() ->{
             oss.putObject(bucket,  fileLocal, inputStream,meta);
             try {
                 if (inputStream != null) {
                     inputStream.close();
                 }

             } catch (IOException e) {
                 e.printStackTrace();
             }
         });



            resultMap.put("uri",uri+fileLocal);
            resultMap.put("fileName",fileName+subFixName);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
          threadPoolExecutor.shutdown();
        }
        return resultMap;
 }


 //下载阿里OSS文件到本地
    public static void fileOssToLoca(String fileName,File file){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            OSSObject object = oss.getObject(bucket,
                    fileName);
             inputStream = object.getObjectContent();
             outputStream = new FileOutputStream(file);
            byte[] arr = new byte[1024] ;
            int len;
            while ((len = inputStream.read(arr))!= -1)
            {
                outputStream.write(arr,0,len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

/*    public static void main(String[] args)throws Exception {


                Map map = ossFileUpload(new FileInputStream(new File("F:\\c语言\\C语言资料\\c基础\\07函数\\3_视频\\01_复习01.mp4")), "01_复习01.mp4");
        System.err.println(map);

         *//* String s = "1.jpg";
        String substring = s.substring(s.lastIndexOf("."), s.length());
        System.out.println(substring);
        Path path = Paths.get(s);
        System.out.println(Files.probeContentType(path));*//*
        System.err.println("1");

    }*/

}
