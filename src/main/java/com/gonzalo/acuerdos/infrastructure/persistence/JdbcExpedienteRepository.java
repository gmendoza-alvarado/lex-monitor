package com.gonzalo.acuerdos.infrastructure.persistence;

import com.gonzalo.acuerdos.application.ExpedienteRepository;
import com.gonzalo.acuerdos.domain.Expediente;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcExpedienteRepository implements ExpedienteRepository {
    private final JdbcTemplate jdbc;

    public JdbcExpedienteRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Expediente> findActivosByJuzgado(String claveJuzgado) {
        return jdbc.query("""
                SELECT id, numero_expediente, expediente_endpoint, clave_juzgado, nombre_cliente, activo
                FROM expedientes
                WHERE activo = 1 AND clave_juzgado = ?
                """, (rs, rowNum) -> new Expediente(
                rs.getLong("id"),
                rs.getString("numero_expediente"),
                rs.getString("expediente_endpoint"),
                rs.getString("clave_juzgado"),
                rs.getString("nombre_cliente"),
                rs.getInt("activo") == 1
        ), claveJuzgado);
    }

	@Override
	public List<Expediente> findAllActivos() {
		return jdbc.query("""
	            SELECT
	                id,
	                numero_expediente,
	                expediente_endpoint,
	                clave_juzgado,
	                nombre_cliente,
	                activo
	            FROM expedientes
	            WHERE activo = 1
	            """,
	            (rs, rowNum) -> new Expediente(
	                    rs.getLong("id"),
	                    rs.getString("numero_expediente"),
	                    rs.getString("expediente_endpoint"),
	                    rs.getString("clave_juzgado"),
	                    rs.getString("nombre_cliente"),
	                    rs.getBoolean("activo")
	            ));
	}
}
