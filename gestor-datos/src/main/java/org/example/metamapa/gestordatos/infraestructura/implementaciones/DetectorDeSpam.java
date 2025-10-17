package org.example.metamapa.gestordatos.infraestructura.implementaciones;


import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.gestordatos.infraestructura.IDetectorDeSpam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DetectorDeSpam implements IDetectorDeSpam {

    private final IEstrategiaDeteccion estrategiaCompuesta;


    @Autowired
    public DetectorDeSpam(List<IEstrategiaDeteccion> estrategias) {
        this.estrategiaCompuesta = new EstrategiaCompuesta(estrategias);
    }

    @Override
    public boolean esSpam(String texto) {
        return estrategiaCompuesta.detectar(texto);
    }
}