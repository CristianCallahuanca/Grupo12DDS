package Persona.Contribuyente;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import AdministracionDeHechos.HechosSinRevisar;

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

    public void subirHecho(Hecho hecho, boolean esPublico) {
        hecho.setVisible(esPublico);
        hecho.setFechaCarga(LocalDateTime.now());
        hecho.setContribuyente(this);
        listaDeHechos.add(hecho);
        hecho.setOrigen(Origen.DINAMICA);
        HechosSinRevisar.getInstance().agregarHecho(hecho);
    }

    public void eliminarHecho(Hecho hecho) {
        listaDeHechos.remove(hecho);
        hecho.marcarComoNoVisible();
    }

    public void modificarHecho(Hecho hechoModificado, Hecho cambios) {
        if (hechoModificado.puedeSerEditado()) {
            hechoModificado.editarCon(cambios);
        } else {
            throw new IllegalStateException("El hecho no puede ser editado despues de una semana.");
        }
    }



}



