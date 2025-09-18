package org.example.metamapa.admin.models.entidades;

import org.example.metamapa.admin.models.entidades.Consenso.AlgoritmoConsenso;
import org.example.metamapa.admin.models.entidades.enums.Origen;

import java.util.List;

public class Coleccion {
    private long id;
    private List<Origen> fuentes;
    private String titulo;
    private String descripcion;
    private List<CondicionFiltrado> criterios;
    private List<Hecho> hechos;
    private String handle;
    private AlgoritmoConsenso algoritmo;

    /*public Coleccion(List<Origen> fuentes, String titulo, String descripcion, List<CriterioDePertenencia> criterios) throws IOException {
        this.fuentes = fuentes;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterios = criterios;
        this.handle = ServicioDeIdentificacion.getInstancia().generarHandle();
        ServicioDeAgregacion.getInstancia().primeraCarga(this);
    }

    public void insertarHechos(List <Hecho> unosHechos) {this.hechos.addAll(unosHechos);}

    public void consensuarHechos(){
        this.algoritmoDeConsenso.verificar(this.hechos);
    }
    public List <Hecho> obtenerHechosPorModo(ModoNavegacion algunModo)
    {
        return algunModo.aplicarModoDeNavegacion(this.obtenerHechosVisibles(), this.algoritmoDeConsenso);
    }


    public List<Hecho> obtenerHechosVisibles() {
        return hechos.stream().filter(hecho->hecho.getEstadoHecho() != EstadoHecho.NO_VISIBLE).toList();
    }


    //Solo para tests
    public void imprimirHechos(List<Hecho> unosHechos) {
        unosHechos.forEach(unHecho -> unHecho.imprimirHecho());
    }

    //Para que los usuarios puedan navegar en la colección
    private List<Hecho> obtenerHechosFiltrados(List<CriterioDePertenencia> filtros) throws IOException {
        return ServicioFiltradorDeHechos.filtrarHechos(this.hechos ,filtros);
    }*/


}
