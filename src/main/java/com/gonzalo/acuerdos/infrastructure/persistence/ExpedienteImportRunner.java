package com.gonzalo.acuerdos.infrastructure.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ExpedienteImportRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ExpedienteImportRunner.class);
    private final JdbcTemplate jdbc;
    private final String csvPath;

    public ExpedienteImportRunner(JdbcTemplate jdbc, @Value("${EXPEDIENTES_CSV_PATH:}") String csvPath) {
        this.jdbc = jdbc;
        this.csvPath = csvPath;
    }

    @Override
    public void run(String... args) throws Exception {
        if (csvPath == null || csvPath.isBlank()) return;
        Path path = Path.of(csvPath);
        if (!Files.exists(path)) {
            log.warn("No existe el CSV de expedientes: {}", csvPath);
            return;
        }
        var lines = Files.readAllLines(path).stream().skip(1).filter(l -> !l.isBlank()).toList();
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length < 4) continue;
            String numero = parts[0].trim();
            String endpoint = numero.replace("/", "_");
            String clave = parts[1].trim();
            String cliente = parts[2].trim();
            int activo = Boolean.parseBoolean(parts[3].trim()) || "1".equals(parts[3].trim()) ? 1 : 0;
            jdbc.update("""
                INSERT OR IGNORE INTO expedientes (numero_expediente, expediente_endpoint, clave_juzgado, nombre_cliente, activo)
                VALUES (?, ?, ?, ?, ?)
                """, numero, endpoint, clave, cliente, activo);
        }
        log.info("Importados expedientes desde CSV: {}", lines.size());
    }
}
