package com.gonzalo.acuerdos.domain;

public record AcuerdoNotificacion(
        Expediente expediente,
        AcuerdoDetectado acuerdo
) { 
}