package Persona.Contribuyente;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import Fuentes.FuenteDinamica;
import HechosSinRevisar.HechosSinRevisar;
import java.time.LocalDateTime;


public class Contribuyente_Registrado extends Contribuyente {
    private String nombre;
    private String apellido;
    private int edad;

    public Contribuyente_Registrado(String nombre, String apellido, int edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
    }

    public void subirHecho(Hecho hecho) {
        hecho.setFechaCarga(LocalDateTime.now());
        hecho.setContribuyente(this);
        hecho.setOrigen(Origen.DINAMICA);
        HechosSinRevisar.getInstance().agregarHecho(hecho);
        listaDeHechos.add(hecho);
    }

    public void eliminarHecho(Hecho hecho) {
        listaDeHechos.remove(hecho);
        hecho.marcarComoNoVisible(); //puede hacer esto? no es con solicitud de elim
    }

    public void modificarHecho(Hecho hechoModificado, Hecho cambios) {
        if (hechoModificado.puedeSerEditado()) {
            hechoModificado.editarCon(cambios);
        } else {
            throw new IllegalStateException("El hecho no puede ser editado despues de una semana.");
        }
    }



}



