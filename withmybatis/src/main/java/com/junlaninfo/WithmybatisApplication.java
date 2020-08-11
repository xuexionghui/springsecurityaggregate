package com.junlaninfo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.junlaninfo.mapper")
public class WithmybatisApplication {

	public static void main(String[] args) {
		SpringApplication.run(WithmybatisApplication.class, args);
	}

}
