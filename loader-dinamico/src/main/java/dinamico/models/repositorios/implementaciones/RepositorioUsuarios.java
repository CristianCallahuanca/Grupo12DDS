package dinamico.models.repositorios.implementaciones;

import dinamico.models.entidades.contribuyente_registrado.Contribuyente_registrado;
import dinamico.models.repositorios.IRepositorioUsuarios;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositorioUsuarios implements IRepositorioUsuarios {

    private final List<Contribuyente_registrado> usuarios = new ArrayList<>();

    public void guardar(Contribuyente_registrado contribuyente){
        usuarios.add(contribuyente);
        System.out.println("hay cargador:" + usuarios.size() + " usuarios");
    }
}
