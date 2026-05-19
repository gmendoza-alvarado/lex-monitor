CREATE TABLE IF NOT EXISTS expedientes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_expediente TEXT NOT NULL,
    expediente_endpoint TEXT NOT NULL,
    clave_juzgado TEXT NOT NULL,
    nombre_cliente TEXT NOT NULL,
    activo INTEGER NOT NULL DEFAULT 1,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (expediente_endpoint, clave_juzgado)
);

CREATE TABLE IF NOT EXISTS acuerdos_detectados (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    expediente_id INTEGER NOT NULL,
    clave_juzgado TEXT NOT NULL,
    fecha_acuerdo TEXT NOT NULL,
    resumen TEXT NOT NULL,
    raw_payload TEXT NOT NULL,
    hash_acuerdo TEXT NOT NULL,
    notificado INTEGER NOT NULL DEFAULT 0,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (hash_acuerdo),
    FOREIGN KEY (expediente_id) REFERENCES expedientes(id)
);

CREATE INDEX IF NOT EXISTS idx_expedientes_activos ON expedientes(activo, clave_juzgado);
CREATE INDEX IF NOT EXISTS idx_acuerdos_fecha ON acuerdos_detectados(fecha_acuerdo, clave_juzgado);
