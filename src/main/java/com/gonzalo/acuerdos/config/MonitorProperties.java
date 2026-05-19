package com.gonzalo.acuerdos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties(prefix = "monitor")
public record MonitorProperties(boolean enabled, String cron, String zone, List<JuzgadoConfig> juzgados) {
    public record JuzgadoConfig(String clave, String nombre) {}
}
