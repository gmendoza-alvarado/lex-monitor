package com.gonzalo.acuerdos.application;

import com.gonzalo.acuerdos.domain.Expediente;
import java.util.List;

public interface ExpedienteRepository {
    List<Expediente> findActivosByJuzgado(String claveJuzgado);
    List<Expediente> findAllActivos();
}
