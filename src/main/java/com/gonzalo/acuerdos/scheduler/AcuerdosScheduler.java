package com.gonzalo.acuerdos.scheduler;

import com.gonzalo.acuerdos.application.MonitorAcuerdosService;
import com.gonzalo.acuerdos.config.MonitorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AcuerdosScheduler {
    private static final Logger log = LoggerFactory.getLogger(AcuerdosScheduler.class);
    private final MonitorAcuerdosService service;
    private final MonitorProperties properties;

    public AcuerdosScheduler(MonitorAcuerdosService service, MonitorProperties properties) {
        this.service = service;
        this.properties = properties;
    }

    @Scheduled(cron = "${monitor.cron}", zone = "${monitor.zone}")
    public void ejecutarRevisionDiaria() {
        if (!properties.enabled()) {
            log.info("Monitor desactivado");
            return;
        }
        var claves = properties.juzgados().stream().map(MonitorProperties.JuzgadoConfig::clave).toList();
        int encontrados = service.revisar(LocalDate.now(), claves);
        log.info("Revisión terminada. Acuerdos nuevos detectados: {}", encontrados);
    }
}
