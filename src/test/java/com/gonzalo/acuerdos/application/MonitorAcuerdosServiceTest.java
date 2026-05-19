package com.gonzalo.acuerdos.application;

import com.gonzalo.acuerdos.domain.AcuerdoRemoto;
import com.gonzalo.acuerdos.domain.Expediente;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class MonitorAcuerdosServiceTest {
    @Mocked ExpedienteRepository expedienteRepository;
    @Mocked AcuerdoRepository acuerdoRepository;
    @Mocked TsjClient tsjClient;
    @Mocked Notifier notifier;

    @Test
    void shouldNotifyWhenExpedienteAppearsInAgreement() {
        var hashService = new HashService();
        var service = new MonitorAcuerdosService(expedienteRepository, acuerdoRepository, tsjClient, notifier, hashService);
        var fecha = LocalDate.of(2026, 5, 14);
        var expediente = new Expediente(1L, "153/2005", "153_2005", "1RG", "Cliente", true);
        var remoto = new AcuerdoRemoto("153/2005", "1RG", fecha, "Se acuerda escrito", "{expediente:153/2005}");

        new Expectations() {{
            expedienteRepository.findActivosByJuzgado("1RG"); result = List.of(expediente);
            tsjClient.consultarPorFecha("1RG", fecha); result = List.of(remoto);
            acuerdoRepository.existsByHash(anyString); result = false;
        }};

        service.revisar(fecha, List.of("1RG"));

        new Verifications() {{
            acuerdoRepository.save(withNotNull()); times = 1;
            notifier.notifyNewAgreements(null); times = 1;
        }};
    }
}
