package com.lilemy.interfaceinfo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.lilemy.interfaceinfo.mapper")
@SpringBootApplication
@EnableDubbo
public class InterfaceInfoApplication {
    public static void main(String[] args) {
        SpringApplication.run(InterfaceInfoApplication.class, args);
    }
}
