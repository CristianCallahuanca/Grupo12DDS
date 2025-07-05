package Handlers.handlerColeccion;

import lombok.Setter;
import lombok.Getter;

import java.util.List;


@Getter
@Setter
public class BodyColeccion {

    private String titulo;
    private String descripcion;
    private List<String> fuentes;
    private List<CriterioDTO> criterios;
}
