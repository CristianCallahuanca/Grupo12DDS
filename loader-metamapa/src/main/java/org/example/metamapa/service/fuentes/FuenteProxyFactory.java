package org.example.metamapa.service.fuentes;

import org.example.metamapa.models.entidades.FuenteConfigurada;
import org.example.metamapa.service.adapters.IAdapaterFuenteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FuenteProxyFactory {

    private final IAdapaterFuenteProxy adapterDemo;
    private FuenteDemo fuenteDemoSingleton;

    @Autowired
    public FuenteProxyFactory(IAdapaterFuenteProxy adapterDemo) {
        this.adapterDemo = adapterDemo;
    }

    public IFuenteProxy construirFuente(FuenteConfigurada fuente) {
        switch (fuente.getTipo()) {
            case DEMO -> {
                if (fuenteDemoSingleton == null) {
                    fuenteDemoSingleton = new FuenteDemo(adapterDemo); // Por el enunciado me da a entender que puede ser un singleton pero lo estoy dudando
                    // Creeria que tambien pueden ser distintas demos, pero por el momento lo dejo asi hasta testear
                }
                return fuenteDemoSingleton;
            }
            case METAMAPA -> {
                FuenteMetaMapa meta = new FuenteMetaMapa();
                meta.setUrlBase(fuente.getUrl());
                return meta;
            }
            default -> throw new IllegalArgumentException("Tipo de fuente no soportado: " + fuente.getTipo());
        }
    }

    public FuenteDemo getFuenteDemo() {
        return fuenteDemoSingleton;
    }
}
