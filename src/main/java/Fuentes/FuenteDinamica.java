package Fuentes;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorOrigen;
import AdministracionDeHechos.Hecho;
import Fuentes.FuenteEstatica.Dataset;
import Fuentes.FuenteEstatica.FuenteEstatica;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;
import Servicios.ServicioDeAgregacion;
import Servicios.ServicioFiltradorDeHechos;

import java.util.ArrayList;
import java.util.List;

import static AdministracionDeHechos.Origen.DINAMICA;

public class FuenteDinamica extends Fuente {

    private static final FuenteDinamica instance = new FuenteDinamica();
    //Singleton

    public static FuenteDinamica getInstancia() {
        return instance;
    }

    @Override
    public List<Hecho> obtenerHechos(){
        return
        ServicioFiltradorDeHechos.filtrarHechos(HechoRepositoryEnMemoria.getInstancia().obtenerTodas(),List.of(new PorOrigen(DINAMICA)));
    }
    /*
    public void agregarHecho(Hecho hecho) {
        if (hecho == null || hecho.getTitulo() == null) return;

        // Verificar si ya existe un hecho con el mismo titulo
        hechos.removeIf(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()));

        this.hechos.add(hecho);
        this.cargarEnRepository(hecho); */



}
