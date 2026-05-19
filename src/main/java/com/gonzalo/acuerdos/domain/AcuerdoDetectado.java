package com.gonzalo.acuerdos.domain;

import java.time.LocalDate;

public record AcuerdoDetectado(
        Long expedienteId,
        String claveJuzgado,
        LocalDate fechaAcuerdo,
        String resumen,
        String rawPayload,
        String hashAcuerdo
) {}
