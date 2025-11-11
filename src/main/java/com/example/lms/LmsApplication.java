package com.example.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class LmsApplication {

    public static void main(String[] args) {
        // Lưu context Spring vào biến
        ApplicationContext context = SpringApplication.run(LmsApplication.class, args);

        // In tất cả các bean Spring scan được
        System.out.println("app started");
    }
}
