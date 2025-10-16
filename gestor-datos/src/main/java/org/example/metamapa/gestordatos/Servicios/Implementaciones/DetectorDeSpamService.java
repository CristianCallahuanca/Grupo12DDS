package org.example.metamapa.gestordatos.Servicios.Implementaciones;


import org.example.metamapa.gestordatos.Servicios.DetectorDeSpam;
import org.springframework.stereotype.Service;

@Service
public class DetectorDeSpamService implements DetectorDeSpam {

    @Override
    public boolean esSpam(String texto) {
        if (texto == null || texto.isBlank()) return false;
        String normalized = texto.toLowerCase();
        return normalized.contains("dinero") || normalized.contains("$$$")
                || normalized.contains("gana rápido") || normalized.contains("oferta exclusiva");
    }
}