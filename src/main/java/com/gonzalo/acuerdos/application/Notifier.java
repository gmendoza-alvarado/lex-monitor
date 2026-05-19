package com.gonzalo.acuerdos.application;

import java.util.List;

import com.gonzalo.acuerdos.domain.AcuerdoDetectado;
import com.gonzalo.acuerdos.domain.AcuerdoNotificacion;
import com.gonzalo.acuerdos.domain.Expediente;

public interface Notifier {
    void notifyNewAgreements(List<AcuerdoNotificacion> acuerdos);
}
