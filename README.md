# Acuerdos Monitor SQLite

Monitor diario de acuerdos del TSJ Zacatecas para los juzgados de Río Grande.

## Qué hace

- Consulta de lunes a viernes a las 11:00 a.m.
- Revisa los juzgados `1RG`, `2RG`, `3RG`.
- Filtra los acuerdos contra tus expedientes activos.
- Guarda acuerdos nuevos en SQLite.
- Notifica por Telegram.

## Cómo cargar tus expedientes

Tienes 2 formas.

### Opción A: editar migración SQL

Edita:

```text
src/main/resources/db/migration/V2__seed_example.sql
```

Ejemplo:

```sql
INSERT OR IGNORE INTO expedientes (numero_expediente, expediente_endpoint, clave_juzgado, nombre_cliente, activo)
VALUES
('153/2005', '153_2005', '1RG', 'Clara Hernández', 1),
('299/2025', '299_2025', '2RG', 'Jesús Canales', 1);
```

Regla:

- `numero_expediente`: como lo lees tú: `153/2005`
- `expediente_endpoint`: con guion bajo: `153_2005`
- `clave_juzgado`: `1RG`, `2RG`, `3RG`
- `activo`: `1` activo, `0` inactivo

Esta opción es la mejor si vas a subirlo por GitHub/Render.

### Opción B: importar CSV al arrancar

Archivo ejemplo:

```csv
numero_expediente,clave_juzgado,nombre_cliente,activo
153/2005,1RG,Clara Hernández,1
299/2025,2RG,Jesús Canales,1
```

Configura variable:

```bash
EXPEDIENTES_CSV_PATH=/app/expedientes.csv
```

## Telegram

1. En Telegram abre `@BotFather`.
2. Crea bot con `/newbot`.
3. Copia el token.
4. Escríbele un mensaje a tu bot.
5. Abre en navegador:

```text
https://api.telegram.org/botTU_TOKEN/getUpdates
```

6. Busca el `chat.id`.

Variables:

```bash
TELEGRAM_ENABLED=true
TELEGRAM_BOT_TOKEN=tu_token
TELEGRAM_CHAT_ID=tu_chat_id
```

## Correr local

```bash
mvn spring-boot:run
```

Para revisar manualmente:

```bash
curl -X POST "http://localhost:8080/internal/check?fecha=2026-05-14"
```

## Deploy con Render

1. Sube este proyecto a GitHub.
2. En Render crea `New Web Service`.
3. Conecta el repo.
4. Render detectará `render.yaml` y `Dockerfile`.
5. Agrega variables secretas:
   - `TELEGRAM_BOT_TOKEN`
   - `TELEGRAM_CHAT_ID`

## Nota importante sobre SQLite en Render Free

Render Free puede reiniciar el servicio y el disco local puede no ser persistente si no usas disco persistente. Para algo más serio, usa PostgreSQL/Supabase. Para prueba y arranque, SQLite sirve perfecto.
