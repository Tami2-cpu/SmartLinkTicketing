package com.zhilian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.zhilian")
public class ZhilianServeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZhilianServeApplication.class, args);
    }
}