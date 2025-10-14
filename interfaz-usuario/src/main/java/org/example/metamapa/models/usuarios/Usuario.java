package org.example.metamapa.models.usuarios;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Usuario {
    private Long id;
    private String nombre;
    private String apellido;
    private String nombreDelUsuario;
    private String contrasenia;
    private Rol rol;
    private List<Permiso> permisos = new ArrayList<>();

    public void agregarPermiso(Permiso p) {
        this.permisos.add(p);
    }

    public void setNombreDeUsuario(String nombre) {
        this.nombre = nombre;
    }
}