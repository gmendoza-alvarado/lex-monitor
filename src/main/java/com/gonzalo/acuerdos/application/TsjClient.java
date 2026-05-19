package com.gonzalo.acuerdos.application;

import com.gonzalo.acuerdos.domain.AcuerdoRemoto;
import java.time.LocalDate;
import java.util.List;

public interface TsjClient {
    List<AcuerdoRemoto> consultarPorFecha(String claveJuzgado, LocalDate fecha);
}
