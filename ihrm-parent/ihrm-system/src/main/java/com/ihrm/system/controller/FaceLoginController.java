package com.ihrm.system.controller;

import com.baidu.aip.util.Base64Util;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.utils.BaiduAiUtil;
import com.ihrm.domain.response.FaceLoginResult;
import com.ihrm.domain.response.QRCode;
import com.ihrm.system.service.FaceLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/sys/faceLogin")
public class FaceLoginController {

    @Autowired
    private FaceLoginService faceLoginService;

    @Autowired
    private BaiduAiUtil baiduAiUtil;


    /**
     * 获取刷脸登录二维码
     */
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    public Result qrcode() throws Exception {
        QRCode qrCode = faceLoginService.getQRCode();
        return new Result(ResultCode.SUCCESS,qrCode);
    }

    /**
     * 检查二维码：登录页面轮询调用此方法，根据唯一标识code判断用户登录情况
     */
    @RequestMapping(value = "/qrcode/{code}", method = RequestMethod.GET)
    public Result qrcodeCeck(@PathVariable(name = "code") String code) throws Exception {
        FaceLoginResult faceLoginResult = faceLoginService.checkQRCode(code);
        return new Result(ResultCode.SUCCESS,faceLoginResult);
    }

    /**
     * 人脸登录：根据落地页随机拍摄的面部头像进行登录
     *          根据拍摄的图片调用百度云AI进行检索查找
     */
    @RequestMapping(value = "/{code}", method = RequestMethod.POST)
    public Result loginByFace(@PathVariable(name = "code") String code, @RequestParam(name = "file") MultipartFile attachment) throws Exception {
		//人脸登陆
        //自动完成shiro生成token,完成redis数据实时变更数据
        String userId = faceLoginService.loginByFace(code, attachment);
        if (Objects.nonNull(userId)) {
            // 成功
            return new Result(ResultCode.SUCCESS);
        }else {
            //失败
            return new Result(ResultCode.FAIL);
        }
    }


    /**
     * 图像检测，判断图片中是否存在面部头像
     */
    @RequestMapping(value = "/checkFace", method = RequestMethod.POST)
    public Result checkFace(@RequestParam(name = "file") MultipartFile attachment) throws Exception {
        Boolean aBoolean = baiduAiUtil.faceCheck(Base64Util.encode(attachment.getBytes()));
        return new Result(ResultCode.SUCCESS,aBoolean);
    }

}