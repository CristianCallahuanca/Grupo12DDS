package Fuentes.Proxy;

import AdministracionDeHechos.Hecho;

import java.util.List;

public class FuenteDemo extends FuenteProxy {
    private AdaptadorDemo adapter;

    public FuenteDemo(AdaptadorDemo adapter) {
        this.adapter = adapter;
    }

    @Override
    public void sincronizar() {
        List<Hecho> nuevosHechos = adapter.obtenerHechos();
        this.hechos.addAll(nuevosHechos);
    }


}

