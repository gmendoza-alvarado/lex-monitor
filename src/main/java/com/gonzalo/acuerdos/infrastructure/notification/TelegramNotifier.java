package com.gonzalo.acuerdos.infrastructure.notification;

import com.gonzalo.acuerdos.application.Notifier;
import com.gonzalo.acuerdos.domain.AcuerdoDetectado;
import com.gonzalo.acuerdos.domain.AcuerdoNotificacion;
import com.gonzalo.acuerdos.domain.Expediente;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TelegramNotifier implements Notifier {

	private static final Logger log = LoggerFactory.getLogger(TelegramNotifier.class);

	private final TelegramProperties properties;

	private final RestClient restClient = RestClient.builder().baseUrl("https://api.telegram.org").build();

	public TelegramNotifier(TelegramProperties properties) {
		this.properties = properties;
	}

	
	
	@Override
	public void notifyNewAgreements(List<AcuerdoNotificacion> acuerdos) {

	    if (acuerdos == null || acuerdos.isEmpty()) {
	        return;
	    }

	    StringBuilder message = new StringBuilder();

	    message.append("⚖️ Acuerdos detectados\n\n");

	    for (int i = 0; i < acuerdos.size(); i++) {
	        AcuerdoNotificacion item = acuerdos.get(i);

	        Expediente expediente = item.expediente();
	        AcuerdoDetectado acuerdo = item.acuerdo();

	        message.append(i + 1).append(") ")
	                .append("Expediente: ").append(expediente.numeroExpediente()).append("\n")
	                .append("Cliente: ").append(expediente.nombreCliente()).append("\n")
	                .append("Juzgado: ").append(acuerdo.claveJuzgado()).append("\n")
	                .append("Fecha: ").append(acuerdo.fechaAcuerdo()).append("\n")
	                .append("Acuerdo: ").append(acuerdo.resumen()).append("\n\n");
	    }

	    sendMessage(message.toString());
	}

	private void sendMessage(String message) {

	    if (!properties.enabled()) {
	        log.info("Telegram desactivado. Mensaje: {}", message);
	        return;
	    }

	    try {

	        String response = restClient.post()
	                .uri("/bot{token}/sendMessage", properties.botToken())
	                .body(new TelegramMessage(properties.chatId(), message))
	                .retrieve()
	                .body(String.class);

	        log.info("Telegram enviado correctamente: {}", response);

	    } catch (Exception ex) {

	        log.error("Error enviando Telegram", ex);
	    }
	}
	private record TelegramMessage(String chat_id, String text) {
	}
}