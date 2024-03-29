package com.xcl.core.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;

/**
 * @author :xiaochanglu
 * @Description :跨域filter
 * @date :2019/6/25 14:06
 */
@Configuration
public class CorsFilterConfig implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        res.setHeader("Access-Control-Max-Age", "1728000");
        res.setHeader("Access-Control-Allow-Headers","Authorization, Content-Type, Accept, x-requested-with, Cache-Control");
        filterChain.doFilter(servletRequest, res);
    }

    @Override
    public void destroy() {}
}
