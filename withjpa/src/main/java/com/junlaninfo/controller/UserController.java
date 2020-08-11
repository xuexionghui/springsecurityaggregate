package com.junlaninfo.controller;

import com.junlaninfo.dao.UserDao;
import com.junlaninfo.entity.Role;
import com.junlaninfo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 辉 on 2020/8/3.
 */
@RestController
public class UserController {
    @Autowired
    UserDao  userDao;

    @GetMapping("/hello")
    public   String  hello(){
        return  "您已经登录，欢迎访问接口！";
    }

    @GetMapping("/init")
    public   void  init(){
        User u1 = new User();
        u1.setUsername("javaboy");
        u1.setPassword("123");
        u1.setAccountNonExpired(true);
        u1.setAccountNonLocked(true);
        u1.setCredentialsNonExpired(true);
        u1.setEnabled(true);
        List<Role> rs1 = new ArrayList<>();
        Role r1 = new Role();
        r1.setName("ROLE_admin");
        r1.setNameZh("admin");
        rs1.add(r1);
        u1.setRoles(rs1);
        userDao.save(u1);
        User u2 = new User();
        u2.setUsername("xuexionghui");
        u2.setPassword("123");
        u2.setAccountNonExpired(true);
        u2.setAccountNonLocked(true);
        u2.setCredentialsNonExpired(true);
        u2.setEnabled(true);
        List<Role> rs2 = new ArrayList<>();
        Role r2 = new Role();
        r2.setName("ROLE_user");
        r2.setNameZh("user");
        rs2.add(r2);
        u2.setRoles(rs2);
        userDao.save(u2);
    }
}
