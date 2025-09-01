package dinamico.models.repositorios.implementaciones;

import dinamico.models.entidades.hecho.HechoCrudo;
import dinamico.models.repositorios.IRepositorioHechosCrudos;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositorioHechosCrudos implements IRepositorioHechosCrudos {

    private final List<HechoCrudo> hechos = new ArrayList<>();

    public List<HechoCrudo> obtenerHechos() {
        return hechos;
    }

    public void guardar(HechoCrudo hecho){
        hechos.add(hecho);
        System.out.println("hay cargador:" + hechos.size() + " hechos");
    }

    public void vaciarListaHechos(){
        hechos.clear();
    }

}
