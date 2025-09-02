package org.example.metamapa.publica.service.implementaciones;

import org.example.metamapa.publica.clientes.AdministradorClient;
import org.example.metamapa.publica.models.dtos.input.ReporteDTO;
import org.example.metamapa.publica.service.IReportesService;
import org.springframework.stereotype.Service;

@Service
public class ReportesService implements IReportesService {

    private final AdministradorClient adminClient;

    public ReportesService(AdministradorClient adminClient) {
        this.adminClient = adminClient;
    }

    @Override
    public void reportarHecho(ReporteDTO reporte) {
        adminClient.enviarReporteDeHecho(reporte);
    }
}


