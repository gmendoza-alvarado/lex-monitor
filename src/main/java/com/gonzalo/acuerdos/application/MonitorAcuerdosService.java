package com.gonzalo.acuerdos.application;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.gonzalo.acuerdos.domain.AcuerdoDetectado;
import com.gonzalo.acuerdos.domain.AcuerdoNotificacion;
import com.gonzalo.acuerdos.domain.Expediente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitorAcuerdosService {

    private final ExpedienteRepository expedienteRepository;
    private final AcuerdoRepository acuerdoRepository;
    private final TsjClient tsjClient;
    private final Notifier notifier;
    private final HashService hashService;

	public int revisarAcuerdosDelDia() {
		return revisar(LocalDate.now(), List.of("1RG", "2RG", "3RG", "1SO"));
	}

	public int revisar(LocalDate fecha, List<String> clavesJuzgados) {
		int encontrados = 0;
		List<AcuerdoNotificacion> acuerdosParaNotificar = new ArrayList<>();

		for (String clave : clavesJuzgados) {
			var expedientes = expedienteRepository.findActivosByJuzgado(clave);

			if (expedientes.isEmpty()) {
				log.info("No hay expedientes activos para el juzgado {}", clave);
				continue;
			}

			var acuerdos = tsjClient.consultarPorFecha(clave, fecha);

			log.info("Juzgado {} - fecha {} - acuerdos recibidos: {} - expedientes monitoreados: {}", clave, fecha,
					acuerdos.size(), expedientes.size());

			for (Expediente expediente : expedientes) {
				var coincidencias = acuerdos.stream()
						.filter(a -> normalize(a.expediente()).equals(normalize(expediente.numeroExpediente()))
								|| normalize(a.expediente()).equals(normalize(expediente.expedienteEndpoint())))
						.toList();

				for (var remoto : coincidencias) {
					var hash = hashService.sha256(
							clave + "|" + expediente.expedienteEndpoint() + "|" + fecha + "|" + remoto.rawPayload());

					if (acuerdoRepository.existsByHash(hash)) {
						log.info("Acuerdo ya registrado. Expediente: {}, juzgado: {}, fecha: {}",
								expediente.numeroExpediente(), clave, fecha);
						continue;
					}

					var acuerdo = new AcuerdoDetectado(expediente.id(), clave, fecha, safeSummary(remoto.texto()),
							remoto.rawPayload(), hash);

					acuerdoRepository.save(acuerdo);

					acuerdosParaNotificar.add(
					        new AcuerdoNotificacion(expediente, acuerdo)
					);

					log.info(
					        "Nuevo acuerdo detectado. Expediente: {}, cliente: {}, juzgado: {}, fecha: {}",
					        expediente.numeroExpediente(),
					        expediente.nombreCliente(),
					        clave,
					        fecha
					);

					encontrados++;
				}
			}
			
		}
		if (!acuerdosParaNotificar.isEmpty()) {
		    notifier.notifyNewAgreements(acuerdosParaNotificar);
		}

		return encontrados;
	}

	private String normalize(String value) {
		if (value == null || value.isBlank()) {
			return "";
		}

		String normalized = value.toLowerCase(Locale.ROOT).replace(" ", "").replace("/", "_").trim();

		String[] parts = normalized.split("_");

		if (parts.length == 2) {
			String numero = parts[0].replaceFirst("^0+", "");
			String anio = parts[1];

			return numero + "_" + anio;
		}

		return normalized.replaceFirst("^0+", "");
	}

	private String safeSummary(String value) {
		if (value == null || value.isBlank()) {
			return "Acuerdo publicado en lista.";
		}

		return value.length() <= 700 ? value : value.substring(0, 700) + "...";
	}
}