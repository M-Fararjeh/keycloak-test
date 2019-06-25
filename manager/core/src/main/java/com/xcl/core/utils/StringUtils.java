package com.xcl.core.utils;

import com.google.common.base.CharMatcher;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author :xiaochanglu
 * @Description :字符串工具类
 * @date :2019/1/8 19:07
 */
public class StringUtils {
    private static final char[] zeroArray = "0000000000000000000000000000000000000000000000000000000000000000".toCharArray();

    /**
     * @Description  ：判断字符串 是否为空
     * @author       : xcl
     * @param        : [value]
     * @return       : boolean  true为空 false不为空
     * @date         : 2019/1/8 19:07
     */
    public static final boolean isEmpty(String value){
        return (value == null||"".equals(value));
    }
    /**
     * @Description  ：判断字符串 是否为空
     * 包括空字符串  或者  空格  都算 为空
     * @author       : xcl
     * @param        : [string]
     * @return       : boolean true为空 false不为空
     * @date         : 2019/1/8 19:07
     */
    public static boolean isBlank(String string) {
        return isEmpty(string) || string.trim().length() == 0;
    }
    /**
     * @Description  ：首字母转大写
     * @author       : xcl
     * @param        : [string]
     * @return       : java.lang.String
     * @date         : 2018/12/14 15:07
     */
    public static String toUpperCaseFirstOne(String string){
        if (StringUtils.isBlank(string)) {
            throw new IllegalArgumentException("传入的字符串为空");
        }
        if(Character.isUpperCase(string.charAt(0))){
            return string;
        }else{
            char[] chars = string.toCharArray();
            if (chars[0] >= 'a' && chars[0] <= 'z') {
                chars[0] = (char)(chars[0] - 32);
            }
            return new String(chars);
        }
    }
    /**
     * @Description  ：首字母转小写
     * @author       : xcl
     * @param        : [string]
     * @return       : java.lang.String
     * @date         : 2018/12/14 15:07
     */
    public static String toLowerCaseFirstOne(String string){
        if (StringUtils.isBlank(string)) {
            throw new IllegalArgumentException("传入的字符串为空");
        }
        if(Character.isLowerCase(string.charAt(0))){
            return string;
        }else{
            return (new StringBuilder()).append(Character.toLowerCase(string.charAt(0))).append(string.substring(1)).toString();
        }
    }
    /**
     * @Description  ：将String型表达式计算结果后以String形式返回
     * 利用JavaScript的eval方法进行计算处理
     * @author       : xcl
     * @param        : [str]
     * @return       : java.lang.String
     * @date         : 2018/12/14 15:12
     */
    public static String caculation(String str){
        if(str.equals("")||str.equals("null")||str==null){
            str = "0";
        }
        String result ;
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        try {
            Object obj = engine.eval(str);
            result = obj.toString();
        } catch (ScriptException e) {
            result = "0";
            System.out.println("无法识别表达式");
            e.printStackTrace();
        }
        return result ;
    }
    /**
     * @Description  ：随机生成UUID字符串
     * @author       : xcl
     * @param        : []
     * @return       : java.lang.String
     * @date         : 2019/1/8 19:14
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    /**
     * @Description  ：随机生成UUID  long型数据
     * @author       : xcl
     * @param        : []
     * @return       : java.lang.String
     * @date         : 2019/1/8 19:14
     */
    public static long randomLong() {
        return Math.abs(new SecureRandom().nextLong());
    }
    /**
     * @Description  ：Base64 编码
     * @author       : xcl
     * @param        : [bytes]
     * @return       : java.lang.String
     * @date         : 2019/1/8 19:16
     */
    public static String encoder(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }
    /**
     * @Description  ：Base64 解码
     * @author       : xcl
     * @param        : [bytes]
     * @return       : java.lang.String
     * @date         : 2019/1/8 19:16
     */
    public static String decoder(byte[] bytes){
        return new String(Base64.getDecoder().decode(bytes));
    }

    public static final String zeroPadString(String string, int length) {
        if (string != null && string.length() <= length) {
            StringBuffer buf = new StringBuffer(length);
            buf.append(zeroArray, 0, length - string.length()).append(string);
            return buf.toString();
        } else {
            return string;
        }
    }
    /**
     * @Description  ：获取指定字符串中的数字
     * @author       : xcl
     * @param        : [string]
     * @return       : java.lang.String
     * @date         : 2019/3/19 14:41
     */
    public static String getNum(String string){
        return CharMatcher.forPredicate(Character::isDigit).retainFrom(string);
    }
    /**
     * @Description  ：字符串去除指定 字符串
     * @author       : xcl
     * @param        : [string, removeStr]
     * @return       : java.lang.String
     * @date         : 2019/3/19 16:34
     */
    public static String getOthers(String string, String removeStr){
        return string.replace(removeStr,"");
    }
    /**
     * @Description  ：密码加密处理
     * @author       : xcl
     * @param        : [password]
     * @return       : java.lang.String
     * @date         : 2019/3/20 16:57
     */
    public static String encryption (String password){
        return new EncryPtUtils("MD5").calcHexDigest(password);
    }
//
//    public static void main(String[] args) {
//        System.out.println(getOthers("/sso/selectByPrimaryKey","/sso"));
//        System.out.println(zeroPadString("35",4));
//        System.out.println(getNum("some 2343 te222xt 89983 and more"));
//    }
}
