package com.ihrm.system.service;

import com.baidu.aip.util.Base64Util;
import com.ihrm.common.utils.BaiduAiUtil;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.QRCodeUtil;
import com.ihrm.domain.User;
import com.ihrm.domain.response.FaceLoginResult;
import com.ihrm.domain.response.QRCode;
import com.ihrm.system.dao.UserDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class FaceLoginService {

    @Value("${qr.url}")
    private String url;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    @Autowired
    private IdWorker idWorker;

    @Autowired
    private QRCodeUtil qrCodeUtil;
    
    @Autowired
    private BaiduAiUtil baiduAiUtil;
    
    @Autowired
    private UserDao userDao;

	//创建二维码 构建code标识,存储Redis
    public QRCode getQRCode() throws Exception {
        String code = idWorker.nextId();
        String content = qrCodeUtil.crateQRCode(url + "?code=" + code);
        FaceLoginResult faceLoginResult = new FaceLoginResult("-1");
        System.out.println(content);
        redisTemplate.boundValueOps(getCacheKey(code)).set(faceLoginResult, 10,TimeUnit.MINUTES);
        return new QRCode(code,content);
    }

	//根据唯一标识，查询用户是否登录成功
    public FaceLoginResult checkQRCode(String code) {
		return (FaceLoginResult) redisTemplate.boundValueOps(getCacheKey(code)).get();
    }

	//扫描二维码之后，使用拍摄照片进行登录
    public String loginByFace(String code, MultipartFile attachment) throws Exception {
		//根据文件去查询人脸库
        FaceLoginResult faceLoginResult = new FaceLoginResult("0");
        String userId = baiduAiUtil.faceSearch(Base64Util.encode(attachment.getBytes()));
        if (Objects.nonNull(userId)){
            //查询出来之后用户自动登陆
            User user = userDao.findById(userId).get();
            if (Objects.nonNull(user)) {
                Subject subject = SecurityUtils.getSubject();
                subject.login(new UsernamePasswordToken(user.getMobile(),user.getPassword()));
                String token = (String) subject.getSession().getId();
                faceLoginResult = new FaceLoginResult("1",token,userId);
            }else {
                userId = null;
            }

        }
        //更改redis数据
        redisTemplate.boundValueOps(getCacheKey(code)).set(faceLoginResult,10,TimeUnit.MINUTES);
        return userId;
    }

	//构造缓存key
    private String getCacheKey(String code) {
        return "qrcode_" + code;
    }
}
