package org.example.metamapa.publica.service.implementaciones;

import org.example.metamapa.publica.clientes.AgregadorClient;
import org.example.metamapa.publica.models.dtos.input.ReporteDTO;
import org.example.metamapa.publica.service.IReportesService;
import org.springframework.stereotype.Service;

@Service
public class ReportesService implements IReportesService {

    private final AgregadorClient agregadorClient;

    public ReportesService(AgregadorClient agregadorClient) {
        this.agregadorClient = agregadorClient;
    }

    @Override
    public void reportarHecho(ReporteDTO reporte) {
        agregadorClient.enviarReporteDeHecho(reporte);
    }
}

