package com.gonzalo.acuerdos.scheduler;

import com.gonzalo.acuerdos.application.MonitorAcuerdosService;
import com.gonzalo.acuerdos.config.MonitorProperties;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/internal/check")
public class ManualCheckController {
    private final MonitorAcuerdosService service;
    private final MonitorProperties properties;

    public ManualCheckController(MonitorAcuerdosService service, MonitorProperties properties) {
        this.service = service;
        this.properties = properties;
    }

    @PostMapping
    public CheckResponse run(@RequestParam(required = false) LocalDate fecha) {
        var claves = properties.juzgados().stream().map(MonitorProperties.JuzgadoConfig::clave).toList();
        int nuevos = service.revisar(fecha == null ? LocalDate.now() : fecha, claves);
        return new CheckResponse(nuevos);
    }

    public record CheckResponse(int nuevos) {}
}
