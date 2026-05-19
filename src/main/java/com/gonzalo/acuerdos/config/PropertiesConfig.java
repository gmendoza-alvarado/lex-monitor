package com.gonzalo.acuerdos.config;

import com.gonzalo.acuerdos.infrastructure.notification.TelegramProperties;
import com.gonzalo.acuerdos.infrastructure.tsj.TsjProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({TsjProperties.class, TelegramProperties.class, MonitorProperties.class})
public class PropertiesConfig {}
