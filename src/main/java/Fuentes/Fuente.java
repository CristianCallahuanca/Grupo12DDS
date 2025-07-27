package Fuentes;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;
import Infraestructura.Repositorios.ColeccionRepositorio;
import Servicios.ServicioDeAgregacion;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class Fuente {

    private List<Hecho> hechos = new ArrayList<>();
    private String nombre;

    /*public Fuente(List<Hecho> hechos){
        this.hechos = hechos;
        ServicioDeAgregacion.getInstancia().guardar(this);
    }*/

    public List<Hecho> obtenerHechos() throws IOException{
        return hechos;
    }

}




