package com.ihrm.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;

public class AESUtil {
    private final static String AES = "AES";
    private final static String ENCODE = "UTF-8";
    private final static String DEFAULTKEY = "xzhkj1302*";

    /**
     * 使用默认key进行加密
     *
     * @param data 需要加密的数据
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        byte[] bt = encrypt(data.getBytes(ENCODE), DEFAULTKEY.getBytes(ENCODE));
        return Base64.encodeBase64String(bt);
    }

    /**
     * 使用默认key进行解密
     *
     * @param data 需解密数据
     * @return
     * @throws Exception
     */
    public static String decrypt(String data) throws Exception {
        if (data == null) {
            return null;
        }
        byte[] bt = decrypt(Base64.decodeBase64(data), DEFAULTKEY.getBytes(ENCODE));
        String str = new String(bt, ENCODE);
        return str;
    }

    /**
     * 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance(AES);
        keygen.init(128, new SecureRandom(key));
        SecretKey srcurekey = keygen.generateKey();
        Key securekey = new SecretKeySpec(srcurekey.getEncoded(), AES);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(AES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey);

        return cipher.doFinal(data);
    }

    /**
     * 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance(AES);
        keygen.init(128, new SecureRandom(key));
        SecretKey srcurekey = keygen.generateKey();
        Key securekey = new SecretKeySpec(srcurekey.getEncoded(), AES);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(AES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey);

        return cipher.doFinal(data);
    }

    public static String cardNumEncryption(Object object) {
        if (object != null) {
            String cardNum = object.toString();
            if (!StringUtils.isEmpty(cardNum)) {
                return cardNum.substring(0, 14) + "***" + cardNum.substring(17, cardNum.length());
            }
        }
        return "";
    }
    
    public static String phoneEncryption(Object object){
        if (object != null) {
            String phone = object.toString();
            if (!StringUtils.isEmpty(phone)) {
                return phone.substring(0,3)+"****"+phone.substring(7,phone.length());
            }
        }
        return "";
    }
    
    /**
     * 指针移位进行数据加密
     *
     * @return
     */
    public static String pointMoveHelp(String data) {
        //把字符串转为字节数组
        byte[] b = data.getBytes();
        //遍历
        for (int i = 0; i < b.length; i++) {
            b[i] += 1;//在原有的基础上+1
        }
        return new String(b);
    }

    /**
     * 指针移位进行数据解密
     *
     * @return
     */
    public static String helpMovePoint(String data) {
        //把字符串转为字节数组
        byte[] b = data.getBytes();
        //遍历
        for (int i = 0; i < b.length; i++) {
            b[i] -= 1;//在原有的基础上-1
        }
        return new String(b);
    }

    public static void main(String[] args) {
        String name = "XZHkj1302*#@0371f2603";
        String key = "xzhkj1302*";
        try {
            String enStr1 = encrypt("jdbc:mysql://47.93.63.135:3306/ay_work_test?useUnicode=true&amp;characterEncoding=utf-8");
//			String enStr1 = encrypt("jdbc:sqlserver://47.93.58.144:1433;DatabaseName=smxface");
            System.out.println(enStr1);
//			String deStr1 = decrypt("cwODY3PXBFD6svSNft6viLT1P2zr4NBbXVw3WN6tn+NUMaYpe3+fizEswepbEVogSeBs+X+TIFukRbzSR4MQf3tt/51YbPFcJEOa6anyXqkF5QUe589ROj0KP6vIu9lR");
//			System.out.println(deStr1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
