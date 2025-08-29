package dinamico.models.repositorios.implementaciones;

import dinamico.models.entidades.hecho.HechoCrudo;
import dinamico.models.repositorios.IRepositorioHechosCrudos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioHechosCrudos implements IRepositorioHechosCrudos {

    private final List<HechoCrudo> hechos = new ArrayList<>();

    public List<HechoCrudo> obtenerHechos() {
        return hechos;
    }

}
