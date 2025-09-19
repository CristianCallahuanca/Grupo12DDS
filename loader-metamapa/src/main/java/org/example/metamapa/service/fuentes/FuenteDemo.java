package org.example.metamapa.service.fuentes;

import org.example.metamapa.models.entidades.HechoCrudo;
import org.example.metamapa.service.adapters.IAdapaterFuenteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FuenteDemo  implements IFuenteProxy {

    private final IAdapaterFuenteProxy adapter;
    private final List<HechoCrudo> cacheHechos = new ArrayList<>();
    private LocalDateTime ultimaActualizacion = LocalDateTime.now();

    public FuenteDemo(IAdapaterFuenteProxy adapter) {
        this.adapter = adapter;
    }

    /** Invocado por el scheduler cada 1h. */
    public void actualizarHechosDesdeFuente() {
        List<HechoCrudo> nuevos = adapter.adaptarHechosDesdeFuente();
        cacheHechos.addAll(nuevos);
        ultimaActualizacion = LocalDateTime.now();
        System.out.println("FuenteDemo se sincronizo " + nuevos.size() + " hechos.");
    }

    /** Devuelve los hechos del cache (actualizados). */
    @Override
    public List<HechoCrudo> cargarHechosExternos() {
        return new ArrayList<>(cacheHechos);
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }
}
