package com.example.dump;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DumpApplication {

    public static void main(String[] args) {
        System.setProperty("user.timezone","GMT +08");
        SpringApplication.run(DumpApplication.class, args);
    }

}
