package org.example.metamapa.gestordatos.infraestructura.implementaciones;


import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.gestordatos.infraestructura.IDetectorDeSpam;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DetectorDeSpam implements IDetectorDeSpam {

    private final IEstrategiaDeteccion estrategia;

    public DetectorDeSpam(IEstrategiaDeteccion estrategia) {
        this.estrategia = estrategia;
    }

    @Override
    public boolean esSpam(String texto) {
        return estrategia.detectar(texto);
    }
}