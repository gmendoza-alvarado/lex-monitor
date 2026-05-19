package com.gonzalo.acuerdos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AcuerdosMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(AcuerdosMonitorApplication.class, args);
    }
}
