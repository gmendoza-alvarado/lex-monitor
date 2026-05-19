package com.gonzalo.acuerdos.config;

import com.gonzalo.acuerdos.application.HashService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    HashService hashService() {
        return new HashService();
    }
}
