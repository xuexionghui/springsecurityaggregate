package com.junlaninfo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by 辉 on 2020/6/10.
 */
@Component
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        增添两个用户，分别需要admin、user的角色才能访问对应的接口
        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("admin");
        auth.inMemoryAuthentication().withUser("user").password("user").roles("user");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().//开启配置
                antMatchers("/admin/**").hasRole("admin").
                antMatchers("/user/**").hasRole("user")
                .antMatchers("/").hasAnyRole("admin", "user").
                anyRequest().authenticated().//剩下的其他请求都需要登录认证后才能访问
                and().formLogin().//配置表单登陆
                loginProcessingUrl("/doLogin").usernameParameter("uname").passwordParameter("pwd").
                successHandler(new AuthenticationSuccessHandler() {
                    @Override                //             authentication 保存了刚刚登陆成功的用户信息
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");//返回的是json
                        PrintWriter writer = response.getWriter();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("status", 200);
                        map.put("msg", authentication.getPrincipal());
                        writer.write(new ObjectMapper().writeValueAsString(map));
                        writer.flush();
                        writer.close();
                    }
                }).
                failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("status", 500);
                        map.put("msg", "登陆失败");
                        writer.write(new ObjectMapper().writeValueAsString(map));
                        writer.flush();
                        writer.close();
                    }
                })
                .permitAll().//登录的url全部允许
                and().
                logout().logoutUrl("/logout").logoutSuccessHandler(new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
              response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                HashMap<String, Object> map = new HashMap<>();
                map.put("status",200);
                map.put("msg","登出成功");
                writer.write(new ObjectMapper().writeValueAsString(map));
                writer.flush();
                writer.close();
            }
        }).and().csrf().disable();
    }
}
