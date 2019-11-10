package com.ihrm.system.enpyt;

import com.alibaba.druid.pool.DruidDataSource;
import com.ihrm.common.utils.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author misterWei
 * @create 2019年10月17号:20点50分
 * @mailbox mynameisweiyan@gmail.com
 */
public class DriverSource extends DruidDataSource {
    private static final Logger logger = LoggerFactory.getLogger(DriverSource.class);
    @Override
    public void setUsername(String username) {
        try {
            super.setUsername(AESUtil.decrypt(username));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void setPassword(String password) {
        try {
            super.setPassword(AESUtil.decrypt(password));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void setUrl(String jdbcUrl) {
        try {
            super.setUrl(AESUtil.decrypt(jdbcUrl));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static void main(String[] args)throws Exception {
        System.out.println(AESUtil.encrypt("jdbc:mysql://39.96.24.21:3306/cat_app?useUnicode=true&characterEncoding=utf8"));
        System.out.println(AESUtil.encrypt("root"));
        System.out.println(AESUtil.encrypt("123"));
    }
}
