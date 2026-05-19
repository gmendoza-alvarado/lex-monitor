package com.gonzalo.acuerdos.infrastructure.tsj;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TsjAcuerdoResponse(

		@JsonProperty("num_expediente") String numExpediente,

		@JsonProperty("Promovente") String promovente,

		@JsonProperty("Juicio") String juicio,

		@JsonProperty("Sintesis_acuerdo") String sintesisAcuerdo) {
}