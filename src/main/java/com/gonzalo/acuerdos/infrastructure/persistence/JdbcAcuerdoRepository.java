package com.gonzalo.acuerdos.infrastructure.persistence;

import com.gonzalo.acuerdos.application.AcuerdoRepository;
import com.gonzalo.acuerdos.domain.AcuerdoDetectado;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcAcuerdoRepository implements AcuerdoRepository {
    private final JdbcTemplate jdbc;

    public JdbcAcuerdoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public boolean existsByHash(String hash) {
        Integer count = jdbc.queryForObject("SELECT COUNT(1) FROM acuerdos_detectados WHERE hash_acuerdo = ?", Integer.class, hash);
        return count != null && count > 0;
    }

    @Override
    public void save(AcuerdoDetectado acuerdo) {
        jdbc.update("""
                INSERT INTO acuerdos_detectados
                (expediente_id, clave_juzgado, fecha_acuerdo, resumen, raw_payload, hash_acuerdo, notificado)
                VALUES (?, ?, ?, ?, ?, ?, 1)
                """,
                acuerdo.expedienteId(), acuerdo.claveJuzgado(), acuerdo.fechaAcuerdo().toString(),
                acuerdo.resumen(), acuerdo.rawPayload(), acuerdo.hashAcuerdo());
    }
}
