package org.example.metamapa.gestordatos.models.entidades;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.metamapa.gestordatos.models.entidades.enums.Origen.DINAMICA;

public class ContribuyenteRegistrado  {
        private long id;
        private String nombre;
        private String apellido;
        private Number dni;
        private Number edad;
        private List<Hecho> hechos;

        public List<Hecho> getMisHechos() {
                return hechos;
        }

        public void agregarAListaDeHechos(Hecho unHecho) {
                hechos.add(unHecho);
        }


        public ContribuyenteRegistrado(String nombre, String apellido, int edad, int dni) {
                this.nombre = nombre;
                this.apellido = apellido;
                this.edad = edad;
                this.dni = dni;
        }

        public void cargarHecho(Hecho hecho) {
                hecho.setFechaCarga(LocalDateTime.now());
                hecho.setContribuyente(this);
                hecho.agregarOrigen(DINAMICA);
                hechos.add(hecho);
        }

        public void eliminarHecho(Hecho hecho) {
                hechos.remove(hecho);
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
