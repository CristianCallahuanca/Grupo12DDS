package org.example.metamapa.models.repositories;


import org.example.metamapa.models.usuarios.Permiso;
import org.example.metamapa.models.usuarios.Rol;
import org.example.metamapa.models.usuarios.Usuario;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuariosRepository {
    private List<Usuario> usuarios;

    public UsuariosRepository() {
        this.usuarios = new ArrayList<>();
        this.cargarUsuarios();
    }

    private void cargarUsuarios() {
        var encoder = new BCryptPasswordEncoder();

        var usuario1 = new Usuario();
        usuario1.setNombre("Jose");
        usuario1.setNombreDeUsuario("admin");
        usuario1.setContrasenia(encoder.encode("1234"));
        usuario1.setRol(Rol.ADMINISTRADOR);
        usuario1.agregarPermiso(Permiso.CONFIGURAR_FUENTES_Y_CRITERIOS);
        usuario1.agregarPermiso(Permiso.CREAR_COLECCION);
        usuario1.agregarPermiso(Permiso.CREAR_HECHO);

        var usuario2 = new Usuario();
        usuario2.setNombre("Marlene");
        usuario2.setNombreDeUsuario("Contribuyente");
        usuario2.setContrasenia(encoder.encode("1234"));
        usuario2.setRol(Rol.CONTRIBUYENTE);

        this.usuarios.add(usuario1);
        this.usuarios.add(usuario2);
    }
}
