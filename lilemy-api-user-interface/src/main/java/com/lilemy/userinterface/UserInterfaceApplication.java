package com.lilemy.userinterface;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.lilemy.userinterface.mapper")
@SpringBootApplication
@EnableDubbo
public class UserInterfaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserInterfaceApplication.class, args);
    }
}
