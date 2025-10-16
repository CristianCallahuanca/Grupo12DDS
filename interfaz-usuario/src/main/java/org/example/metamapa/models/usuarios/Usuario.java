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

    public String getNombreDeUsuario() {
        return this.nombre;
    }

    public void setRol(Rol rol) {
        switch (rol) {
            case ADMINISTRADOR:
                this.rol = Rol.ADMINISTRADOR;
                this.agregarPermiso(Permiso.CONFIGURAR_FUENTES);
                this.agregarPermiso(Permiso.CONFIGURAR_CRITERIOS);
                this.agregarPermiso(Permiso.CREAR_COLECCION);
                this.agregarPermiso(Permiso.CREAR_HECHO);
                break;
            case CONTRIBUYENTE:
                this.rol = Rol.CONTRIBUYENTE;
                this.agregarPermiso(Permiso.CREAR_HECHO);
                this.agregarPermiso(Permiso.CREAR_SOLICITUD_ELIMINACION);
                break;
            default:
                this.rol = Rol.CONTRIBUYENTE;
                break;
        }

    }
}