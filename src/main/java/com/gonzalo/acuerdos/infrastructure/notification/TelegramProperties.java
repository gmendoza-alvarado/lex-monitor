package com.gonzalo.acuerdos.infrastructure.notification;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram")
public record TelegramProperties(boolean enabled, String botToken, String chatId) {}
