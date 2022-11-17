package com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.company")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }
}