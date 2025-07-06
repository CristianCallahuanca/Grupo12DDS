package Handlers.handlerColeccion;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BodyModificarFuentesColeccion {

    private List<String> nuevasFuentes;
    private List<String> fuentesABorrar;
}
