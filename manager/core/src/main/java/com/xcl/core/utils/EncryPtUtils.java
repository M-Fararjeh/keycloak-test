package com.xcl.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author :xiaochanglu
 * @Description :加密工具类
 * @date :2018/12/13 14:31
 */
@Slf4j
public class EncryPtUtils {

    private static MessageDigest msgDigest;
    /**
     * @Description  ：构造  传入指定的加密类型
     * @author       : xcl
     * @param        : [alg] [加密类型（MD5 和 SHA）]
     * @return       : 数据加密类MessageDigest
     * @date         : 2018/12/13 19:24
     */
    public EncryPtUtils(String alg){
        try {
            msgDigest = MessageDigest.getInstance(alg);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    /**
     * @Description  ：构造  默认使用MD5加密
     * @author       : xcl
     * @return       : 数据加密类MessageDigest
     * @date         : 2018/12/13 19:29
     */
    public EncryPtUtils() {
        try {
            log.info("加密工具类  初始化......  默认使用MD5加密");
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }

    public String calcHexDigest(String msg) {
        byte[] bytes = msgDigest.digest(msg.getBytes());
        return bytes2hex(bytes);
    }

    private String bytes2hex(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (int pos = 0; pos < bytes.length; pos++) {
            int n = bytes[pos] & 0xFF;
            buf.append(halfByte2hex((n >> 4) & 0xF));
            buf.append(halfByte2hex(n & 0xF));
        }
        return buf.toString();
    }

    private char halfByte2hex(int n) {
        return (char) (n > 9 ? 'A' + n - 10 : '0' + n);
    }
}
