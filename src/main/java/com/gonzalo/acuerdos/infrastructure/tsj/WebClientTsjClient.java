package com.gonzalo.acuerdos.infrastructure.tsj;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonzalo.acuerdos.application.TsjClient;
import com.gonzalo.acuerdos.domain.AcuerdoRemoto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class WebClientTsjClient implements TsjClient {

	private static final String PATH = "/AdministracionTSJ/ListaAcuerdos/ConsultaAcuerdos1ra_x_fecha";

	private final RestClient restClient;
	private final ObjectMapper mapper;

	public WebClientTsjClient(TsjProperties properties, ObjectMapper mapper) {
		this.restClient = RestClient.builder().baseUrl(properties.baseUrl())
				.defaultHeader("Accept", "application/json, text/javascript, */*; q=0.01")
				.defaultHeader("Origin", "https://tsjzac.gob.mx").defaultHeader("Referer", "https://tsjzac.gob.mx/")
				.build();

		this.mapper = mapper;
	}

	@Override
	public List<AcuerdoRemoto> consultarPorFecha(String claveJuzgado, LocalDate fecha) {

		TsjAcuerdoResponse[] response = restClient.get().uri(
				uri -> uri.path(PATH).queryParam("clave", claveJuzgado).queryParam("fecha", fecha.toString()).build())
				.retrieve().body(TsjAcuerdoResponse[].class);

		if (response == null) {
			return List.of();
		}

		return Arrays.stream(response).map(item -> toAcuerdoRemoto(item, claveJuzgado, fecha)).toList();
	}

	private AcuerdoRemoto toAcuerdoRemoto(TsjAcuerdoResponse item, String claveJuzgado, LocalDate fecha) {
		String rawPayload;

		try {
			rawPayload = mapper.writeValueAsString(item);
		} catch (Exception ex) {
			rawPayload = item.toString();
		}

		String textoAcuerdo = nullSafe(item.sintesisAcuerdo());

		return new AcuerdoRemoto(item.numExpediente(), claveJuzgado, fecha, textoAcuerdo, rawPayload);
	}

	private String nullSafe(String value) {
		return value == null || value.isBlank() ? "Sin dato" : value.trim();
	}
}