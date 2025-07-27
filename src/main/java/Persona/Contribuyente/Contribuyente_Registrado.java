package Persona.Contribuyente;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;

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

    public void cargarHecho(Hecho hecho) {
        hecho.setFechaCarga(LocalDateTime.now());
        hecho.setContribuyente(this);
        hecho.setOrigen(Origen.DINAMICA);
        listaDeHechos.add(hecho);
    }

    public void eliminarHecho(Hecho hecho) {
        listaDeHechos.remove(hecho);
        //hecho.marcarComoNoVisible(); //puede hacer esto? no es con solicitud de elim
    }

    public void solicitarModificarHecho(Hecho hechoModificado, Hecho cambios) {
        if (hechoModificado.puedeSerEditado()) {
            hechoModificado.editarCon(cambios);
            // esto se puede pasar a Hechos sin revisar
            /*
            /*if(!Hecho.HechosSinRevisar.getInstance().contiene(cambios)){
                Hecho.HechosSinRevisar.getInstance().agregarHecho(cambios);
            } else {
                Hecho.HechosSinRevisar.getInstance().sacarHecho(hechoModificado);
                Hecho.HechosSinRevisar.getInstance().agregarHecho(cambios);
            }*/


        } else {
            throw new IllegalStateException("El hecho no puede ser editado despues de una semana.");
        }
    }



}



