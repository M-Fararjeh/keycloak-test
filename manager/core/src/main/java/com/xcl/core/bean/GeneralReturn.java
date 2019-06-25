package com.xcl.core.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.xcl.core.utils.JSONUtils;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @author :xiaochanglu
 * @Description :统一返回结果
 * @date :2019/3/21 9:53
 */
@Data
public class GeneralReturn implements Serializable {
    private static Map<Integer, String> messageMap = Maps.newHashMap();
    private int code;
    private boolean success = true;
    private String message;
    private Object data;

    //初始化状态码与文字说明
    static {
        messageMap.put(0, "");

        messageMap.put(200, "操作成功");

        messageMap.put(400, "HTTP 错误 400.0 - 访问被拒绝：错误的请求");
        messageMap.put(401, "HTTP 错误 401.1 - 未经授权：访问由于凭据无效被拒绝");
        messageMap.put(405, "用来访问本页面的 HTTP 谓词不被允许（方法不被允许）");
        messageMap.put(406, "客户端浏览器不接受所请求页面的 MIME 类型");
        messageMap.put(500, "HTTP 错误 500.0 - 服务器出错：内部服务器出错");

        messageMap.put(1005, "[服务器]运行时异常");
        messageMap.put(1006, "[服务器]空值异常");
        messageMap.put(1007, "[服务器]数据类型转换异常");
        messageMap.put(1008, "[服务器]IO异常");
        messageMap.put(1009, "[服务器]未知方法异常");
        messageMap.put(1010, "[服务器]数组越界异常");
        messageMap.put(1011, "[服务器]网络异常");
    }

    public GeneralReturn() {

    }

    public static String getMessage(int code) {
        return messageMap.get(code);
    }

    /**
     * @Description  ：成功  普通格式
     * @author       : xcl
     * @return       : com.xcl.core.bean.GeneralReturn
     * @date         : 2019/3/21 10:11
     */
    public static GeneralReturn success() {
        GeneralReturn generalReturn = new GeneralReturn();
        generalReturn.setSuccess(true);
        generalReturn.setMessage(messageMap.get(200));
        return generalReturn;
    }
    /**
     * @Description  ：自定义 成功  返回信息
     * @author       : xcl
     * @param        : [message]
     * @return       : com.xcl.core.bean.GeneralReturn
     * @date         : 2019/3/21 10:12
     */
    public static GeneralReturn success(String message) {
        GeneralReturn generalReturn = new GeneralReturn();
        generalReturn.setSuccess(true);
        generalReturn.setCode(200);
        generalReturn.setMessage(message);
        return generalReturn;
    }
    /**
     * @Description  ：自定义 成功  返回信息  和  code码
     * @author       : xcl
     * @param        : [message, code]
     * @return       : com.xcl.core.bean.GeneralReturn
     * @date         : 2019/3/21 10:12
     */
    public static GeneralReturn success(String message,int code) {
        GeneralReturn generalReturn = new GeneralReturn();
        generalReturn.setSuccess(true);
        generalReturn.setCode(code);
        generalReturn.setMessage(message);
        return generalReturn;
    }
    /**
     * @Description  ：自定义 成功  返回信息  和  code码  和  返回数据
     * @author       : xcl
     * @param        : [message, code, object]
     * @return       : com.xcl.core.bean.GeneralReturn
     * @date         : 2019/3/21 10:15
     */
    public static GeneralReturn success(String message,int code, Object object) {
        GeneralReturn generalReturn = new GeneralReturn();
        generalReturn.setSuccess(true);
        generalReturn.setCode(code);
        generalReturn.setMessage(message);
        generalReturn.setData(object);
        return generalReturn;
    }

    public static GeneralReturn error(int code) {
        GeneralReturn generalReturn = new GeneralReturn();
        generalReturn.setSuccess(true);
        generalReturn.setCode(code);
        generalReturn.setMessage(messageMap.get(code));
        return generalReturn;
    }

    public static GeneralReturn fail(String message) {
        GeneralReturn generalReturn = new GeneralReturn();
        generalReturn.setSuccess(false);
        generalReturn.setCode(-1);
        generalReturn.setMessage(message);
        return generalReturn;
    }

    public static GeneralReturn fail(String message, int code) {
        GeneralReturn generalReturn = new GeneralReturn();
        generalReturn.setSuccess(false);
        generalReturn.setCode(code);
        generalReturn.setMessage(message);
        return generalReturn;
    }

    /**
     * @Description  ：自定义 返回信息  和  code码  和  返回数据
     * @author       : xcl
     * @param        : [message, code, object]
     * @return       : com.xcl.core.bean.GeneralReturn
     * @date         : 2019/3/21 10:15
     */
    public static GeneralReturn custom (String message,int code, Object object) {
        GeneralReturn generalReturn = new GeneralReturn();
        generalReturn.setSuccess(true);
        generalReturn.setCode(code);
        generalReturn.setMessage(message);
        generalReturn.setData(object);
        return generalReturn;
    }

    public static GeneralReturn custom (String message,int code, Object obj, String[] attributes) {
        JSONObject jsonObject = JSONUtils.stringTOJSON(JSONUtils.beanToString(obj,attributes));
        return custom(message,code,jsonObject);
    }

    public static GeneralReturn custom (String message,int code, List list, String[] attributes) {
        JSONArray jsonArray = JSONUtils.listToJSONArray(list,attributes);
        return custom(message,code,jsonArray);
    }

}
