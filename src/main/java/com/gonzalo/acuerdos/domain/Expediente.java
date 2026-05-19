package com.gonzalo.acuerdos.domain;

public record Expediente(
        Long id,
        String numeroExpediente,
        String expedienteEndpoint,
        String claveJuzgado, 
        String nombreCliente,
        boolean activo
) {}
