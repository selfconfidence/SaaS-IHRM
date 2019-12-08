package com.ihrm.common.utils;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Date;

@Component
public class QiniuUploadUtil {

    private static final String accessKey = "r8vHhzhe5h-T_Vp58tj2z0ZBsnkzxkQpyVi_txYQ";
    private static final String secretKey = "dI-oEd68M57IpFWnfEbq5BVRKxTOLOovDpij0hLD";
    private static final String bucket = "imagespance";
    private static final String prix = "http://q1m26hzo5.bkt.clouddn.com/";
    private UploadManager manager;

    public QiniuUploadUtil() {
        //初始化基本配置                          //华南地区
        Configuration cfg = new Configuration(Zone.zone2());
        //创建上传管理器
        manager = new UploadManager(cfg);
    }

	//文件名 = key
	//文件的byte数组
    public String upload(String imgName , InputStream bytes) {
        Auth auth = Auth.create(accessKey, secretKey);
        //构造覆盖上传token
        String upToken = auth.uploadToken(bucket,imgName);
        try {
            byte[] bytes1 = new byte[bytes.available()];
            bytes.read(bytes1);
            Response response = manager.put(bytes1, imgName, upToken);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            //返回请求地址
            return prix+putRet.key+"?t="+new Date().getTime();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
