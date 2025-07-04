package Fuentes;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import Servicios.ServicioDeAgregacion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Fuente {

    protected List<Hecho> hechos = new ArrayList<>();

    /*public Fuente(List<Hecho> hechos){
        this.hechos = hechos;
        ServicioDeAgregacion.getInstancia().guardar(this);
    }*/

    public List<Hecho> obtenerHechos() throws IOException{
        return hechos;
    }

}




