package com.junlaninfo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junlaninfo.model.Hr;
import com.junlaninfo.model.RespBean;
import com.junlaninfo.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by 辉 on 2020/8/11.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    HrService hrService;
    @Autowired
    VerificationCodeFilter  verificationCodeFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
      让admin有user角色的权限
     */
    @Bean
    RoleHierarchy  roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_admin  >  ROLE_user");
        return   roleHierarchy;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/code");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //在认证密码先加一个过滤器
        http.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/admin/**").hasAnyRole("admin")
                .antMatchers("/user/**").hasAnyRole("user")
                .anyRequest().authenticated()
                .and().formLogin().loginProcessingUrl("/dologin").usernameParameter("username")
                .passwordParameter("password").permitAll().successHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                RespBean respBean = new RespBean();
                respBean.setStatus(200);
                respBean.setMsg("登录成功");
                Hr hr = (Hr) authentication.getPrincipal();
                hr.setPassword(null);//将密码置空
                respBean.setObj(hr);
                String s = new ObjectMapper().writeValueAsString(respBean);
                writer.write(s);
                writer.flush();
                writer.close();
            }
        }).failureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                RespBean respBean = new RespBean();
                respBean.setStatus(500);
                respBean.setMsg("登录失败");
                String s = new ObjectMapper().writeValueAsString(respBean);
                writer.write(s);
                writer.flush();
                writer.close();
            }
        }).and().csrf().disable();
    }
}
