package com.junlaninfo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import sun.misc.resources.Messages_it;
import sun.security.util.Password;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
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

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //定义xuexionghui用户、密码和角色
//        auth.inMemoryAuthentication().withUser("xuexionghui").password("xuexionghui").roles("admin");
//        auth.inMemoryAuthentication().withUser("xuexiong").password("xuexiong").roles("user");
//    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("xuexionghui").password("xuexionghui").roles("admin").build());
        manager.createUser(User.withUsername("xuexiong").password("xuexiong").roles("user").build());
        return  manager;
    }
    /*
      上级自动继承下级的权限，本例子admin自动继承user的权限
     */
     @Bean
    RoleHierarchy  roleHierarchy(){
         RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
         roleHierarchy.setHierarchy("ROLE_admin > ROLE_user");
         return  roleHierarchy;
     }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()//开启配置
                .antMatchers("/admin/**").hasAnyRole("admin")
                .antMatchers("/user/**").hasAnyRole("user")
                .anyRequest().authenticated()//除了/admin /user的请求需要对应的角色，其他经过认证就可以访问
                .and().formLogin().usernameParameter("username").passwordParameter("password").loginProcessingUrl("/dologin")//定义登录的接口
                .loginPage("/login").successHandler(new AuthenticationSuccessHandler() {//登录成功的回调，后端直接返回一段json信息，不会控制前端跳转到什么页面
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                //定义返回的信息类型是json
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                HashMap<String, String> map = new HashMap<>();
                map.put("code", "200");
                map.put("message", "登录成功");
                writer.write(new ObjectMapper().writeValueAsString(map));
                writer.flush();
                writer.close();


            }
        }).failureHandler(new AuthenticationFailureHandler() {//登录失败的返回信息
            @Override
            public void onAuthenticationFailure(HttpServletRequest request,
                                                HttpServletResponse response,
                                                AuthenticationException e) throws IOException, ServletException {
                response.setContentType("application/json;charset=utf-8");//返回的是json信息
                PrintWriter writer = response.getWriter();
                HashMap<String, Object> map = new HashMap<>();
                map.put("code", "500");
                map.put("message", "登录失败");
                writer.write(new ObjectMapper().writeValueAsString(map));
                writer.flush();
                writer.close();
            }
        }).permitAll().and().logout().logoutUrl("/logout").permitAll().logoutSuccessHandler(
                new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("code", "200");
                        map.put("message", "登出成功");
                        writer.write(new ObjectMapper().writeValueAsString(map));
                        writer.flush();
                        writer.close();
                    }
                }
        )
                .and().exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {//尚未登录，返回json信息给前端
            @Override
            public void commence(HttpServletRequest request,
                                 HttpServletResponse response,
                                 AuthenticationException e) throws IOException, ServletException {
                response.setContentType("application/json;charset=utf-8");//设置返回的是json信息
                PrintWriter writer = response.getWriter();

                HashMap<String, String> map = new HashMap<>();
                map.put("code","200");
                map.put("message","尚未登录，请登录");
                writer.write(new ObjectMapper().writeValueAsString(map));
                writer.flush();
                writer.close();

            }
        })

                .and().csrf().disable()
        ;
    }
}
