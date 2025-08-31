package dinamico.service.implementacion;

import dinamico.models.dtos.input.usuarioDTO;
import dinamico.models.entidades.contribuyente_registrado.Contribuyente_registrado;
import dinamico.models.repositorios.IRepositorioHechosCrudos;
import dinamico.models.repositorios.IRepositorioUsuarios;
import dinamico.service.ICrearUsuarioService;
import org.springframework.stereotype.Service;

@Service
public class crearUsuarioService implements ICrearUsuarioService {

    private final IRepositorioUsuarios usuariosRepository;

    public crearUsuarioService(IRepositorioUsuarios usuariosRepository){
        this.usuariosRepository = usuariosRepository;
    }

    public void cargarUsuario(usuarioDTO usuario){

        Contribuyente_registrado contribuyente = new Contribuyente_registrado(
                usuario.getNombre(),
                usuario.getApellido(),
                Integer.parseInt(usuario.getEdad()),
                Integer.parseInt(usuario.getDni())
        );

        usuariosRepository.guardar(contribuyente);
    }
}
