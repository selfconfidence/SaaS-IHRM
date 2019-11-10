package com.ihrm.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;
import java.util.Map;

/**
 * @author misterWei
 * @create 2019年11月10号:16点53分
 * @mailbox mynameisweiyan@gmail.com
 * 校验jwt认证规则
 */
@ConfigurationProperties("jwt.config")
@Setter
@Getter
public class JwtUtils {
    private String key;
    private long ttl = 0L;

    /**
     * 生成JWT
     *
     * @param id
     * @param subject
     * @return
     */
    public String createJWT(String id, String subject, Map keyValue) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key).setClaims(keyValue);//这是签名秘钥
        if (ttl == 0) {//默认为一小时
            builder.setExpiration( new Date( nowMillis + 3600000));
        }else{
            builder.setExpiration( new Date( nowMillis + ttl));
        }
        return builder.compact();
    }

    /**
     * 解析JWT
     * @param jwtStr
     * @return
     */
    public Claims parseJWT(String jwtStr){
        return  Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }

}
