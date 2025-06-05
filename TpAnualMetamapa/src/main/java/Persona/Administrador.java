package Persona;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import Fuentes.Fuente;
import AdministracionDeHechos.Coleccion;

import java.util.List;

public class Administrador {
    private String nombre;
    private String apellido;
    private int edad;

    public void crearColeccion(Fuente fuente, String titulo, String descripcion, List<CriterioDePertenencia> criterios, String handle) {
        Coleccion unaColeccion = new Coleccion( fuente,  titulo,  descripcion,  criterios, handle);

    }
}


