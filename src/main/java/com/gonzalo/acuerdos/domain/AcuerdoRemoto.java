package com.gonzalo.acuerdos.domain;

import java.time.LocalDate;

public record AcuerdoRemoto(
        String expediente,
        String juzgado,
        LocalDate fecha,
        String texto,
        String rawPayload
) {}
