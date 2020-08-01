package com.junlaninfo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 辉 on 2020/6/8.
 */
@RestController
public class helloController {

    //    user角色和admin角色都可以访问
    @GetMapping("/")
    public String hello() {
        return "springsecurity测试";
    }

    // 拥有admin角色可以访问
    @GetMapping("/admin/hello")
    public String adminhello() {
        return "只有admin角色才能访问到";
    }

    //拥有user角色才可以访问
    @GetMapping("/user/hello")
    public String userhello() {
        return "只有user角色才能访问到";
    }
}
