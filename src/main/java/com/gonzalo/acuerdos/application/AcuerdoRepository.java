package com.gonzalo.acuerdos.application;

import com.gonzalo.acuerdos.domain.AcuerdoDetectado;

public interface AcuerdoRepository {
    boolean existsByHash(String hash);
    void save(AcuerdoDetectado acuerdo);
}
