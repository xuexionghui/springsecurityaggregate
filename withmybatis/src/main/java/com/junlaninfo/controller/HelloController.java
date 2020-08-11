package com.junlaninfo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 辉 on 2020/8/11.
 */
@RestController
public class HelloController {
    @GetMapping("/admin/hello")
    public  String  hello1(){
        return   " 我是admin的hello";
    }

    @GetMapping("/user/hello")
    public  String  hello2(){
        return   " 我是user的hello";
    }
}
