package org.example.metamapa.service.fuentes;

import org.example.metamapa.models.entidades.HechoCrudo;
import java.util.List;

public interface IFuenteProxy {
    List<HechoCrudo> cargarHechosExternos();
}
