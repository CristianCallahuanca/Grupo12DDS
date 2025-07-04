package Handlers;

import lombok.Setter;
import lombok.Getter;

import java.util.List;


@Getter
@Setter
public class BodyColeccion {

    private String titulo;
    private String descripcion;
    private String handle;
    private List<CriterioDTO> criterios;
}
