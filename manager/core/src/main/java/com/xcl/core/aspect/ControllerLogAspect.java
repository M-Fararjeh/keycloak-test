package com.xcl.core.aspect;

import com.google.common.collect.Maps;
import com.xcl.core.utils.DateUtils;
import com.xcl.core.utils.JSONUtils;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author :xiaochanglu
 * @Description :控制层日志
 * @date :2019/3/19 9:31
 */
@Aspect
@Component
@Slf4j
public class ControllerLogAspect {

    public ControllerLogAspect() {
        log.info("控制层日志输出加载......");
    }
    /**
     * @Description  ：打印请求中的内容
     * @author       : xcl
     * @return       : void
     * @date         : 2019/3/19 10:06
     */
    private void printRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //读取session中的用户 等其他和业务相关的信息，
        //比如当前用户所在应用，以及其他信息, 例如ip
        String path = request.getRequestURI();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(ip.lastIndexOf(",") + 1).trim();
        }
        log.info("请求的sessionID:"+session.getId()+"||RemoteIp:"+ ip+"||RequestURI:"+path);
        log.info("=================Parameters=================");
        Enumeration<String> keys = request.getParameterNames();
        Map<String, String> parameterMap = Maps.newHashMap();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String[] values = request.getParameterValues(key);
            StringBuffer sb = new StringBuffer();
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    sb.append(values[i]).append(",");
                }
            }
            parameterMap.put(key,sb.deleteCharAt(sb.length() - 1).toString());
        }
        log.info("请求参数:"+ JSONUtils.beanToString(parameterMap));
        log.info("=================Cookie_value=================");
        Cookie[] cookies=request.getCookies();
        Map<String, String> cookiesMap = Maps.newHashMap();
        if(cookies!=null){
            for(Cookie c:cookies){
                String key=c.getName();
                String value=c.getValue();
                cookiesMap.put(key,value);
            }
        }
        log.info("Cookie参数:"+JSONUtils.beanToString(cookiesMap));
    }
    /**
     * @Description  ：打印输出内容
     * @author       : xcl
     * @param        : [joinPoint]
     * @return       : void
     * @date         : 2019/3/19 9:42
     */
    private void printContent(JoinPoint joinPoint, LocalDateTime localDateTime,int type){
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        if(type==1){
            log.info(targetName+" 调用的方法:"+methodName+" 的开始时间:"+ DateUtils.dataToString(localDateTime,DateUtils.DATEFORMAT3+",执行的开始毫秒数为:"+DateUtils.dataToLong(localDateTime)));
        }else if(type==2){
            log.info(targetName+" 调用的方法:"+methodName+" 的结束时间:"+ DateUtils.dataToString(localDateTime,DateUtils.DATEFORMAT3+",执行的结束毫秒数为:"+DateUtils.dataToLong(localDateTime)));
        }
    }

    @Pointcut("execution(public * com.xcl.*.apply.*.controller..*.*(..))")
    public void beforeLog(){}
    /**
     * @Description  ：前置通知
     * @author       : xcl
     * @param        : [joinPoint]
     * @return       : void
     * @date         : 2019/3/19 9:46
     */
    @Before("beforeLog()")
    public void doBefore(JoinPoint joinPoint){
        //记录方法开始执行的时间
        printContent(joinPoint,DateUtils.now(),1);
        //记录请求的参数 数据
        printRequest();
    }

    @Pointcut("execution(public * com.xcl.*.apply.*.controller..*.*(..))")
    public void afterLog(){}

    @AfterReturning(value = "afterLog()",returning = "result")
    public  void doAfter(JoinPoint joinPoint, Object result) {
        //记录方法执行结束的时间
        printContent(joinPoint,DateUtils.now(),2);
    }
}
