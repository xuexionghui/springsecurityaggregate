package com.junlaninfo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junlaninfo.model.RespBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by 辉 on 2020/8/11.
 */
@Configuration
@Component
public class VerificationCodeFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //方法是post方法，并且方法的路径是dologin，那么需要被拦截验证验证码
        if ("POST".equals(request.getMethod()) && "/dologin".equals(request.getServletPath())) {
            String code = request.getParameter("code");// 从请求中拿到的验证码
            String verify_code = (String) request.getSession().getAttribute("verify_code");//从session拿到的验证码
            if (code == null || verify_code == null || "".equals(code) || !verify_code.toLowerCase().equals(code.toLowerCase())) {
                //验证码不正确
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write(new ObjectMapper().writeValueAsString(RespBean.error("验证码填写错误")));
                out.flush();
                out.close();
                return;
            } else {//验证码正确，放行
                request.getSession().removeAttribute("verify_code");//比对完验证码，应该把session中的验证码清理掉
                filterChain.doFilter(request, response);
            }

        } else {
            //放行
            filterChain.doFilter(request, response);
        }
    }
}
