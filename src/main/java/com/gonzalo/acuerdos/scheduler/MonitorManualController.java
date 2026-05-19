package com.gonzalo.acuerdos.scheduler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gonzalo.acuerdos.application.MonitorAcuerdosService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
public class MonitorManualController {

    private final MonitorAcuerdosService monitorAcuerdosService;

    @GetMapping("/ejecutar-hoy")
    public String ejecutarHoy() {
        monitorAcuerdosService.revisarAcuerdosDelDia();
        return "Revisión ejecutada correctamente";
    }
}