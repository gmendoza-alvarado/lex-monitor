package com.gonzalo.acuerdos.infrastructure.tsj;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tsj")
public record TsjProperties(String baseUrl) {}
