package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class AlienCloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlienCloudApplication.class, args);
    }
}