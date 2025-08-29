package org.example.metamapa.agregador.models.entidades;

import dinamico.models.entidades.hecho.EstadoHecho;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Hecho {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private List<String> archivosMultimedia;
    private String etiqueta;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private String contribuyente_id;
    private Origen origen;
    private String id_hecho;
    private EstadoEdicionHecho estadoEdicionHecho;
    private EstadoHecho estadoHecho;


    public Hecho(String titulo, String descripcion, String categoria, Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento,String etiqueta) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.etiqueta = etiqueta;
        this.estadoHecho = EstadoHecho.EN_REVISION;
        this.estadoEdicionHecho = EstadoEdicionHecho.NO_EDITADO;
        this.fechaCarga = LocalDateTime.now();
        this.id_hecho = UUID.randomUUID().toString().replaceAll("-", "");
    }

    //Ahora los hechos provenientes de fuente estatica se guardan?

    /*public void setOrigen(Origen unOrigen) {
        this.origen = unOrigen;
        if (unOrigen != Origen.ESTATICA) {RepositorioHechos.guardar(this);}
    }*/

    public void marcarComoNoVisible() {
        this.estadoHecho = EstadoHecho.NO_VISIBLE;
    }

}
