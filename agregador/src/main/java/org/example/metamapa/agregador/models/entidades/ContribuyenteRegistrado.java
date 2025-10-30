package org.example.metamapa.agregador.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

//import org.example.metamapa.gestordatos.models.entidades.enums.Origen;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contribuyente_registrado")

public class ContribuyenteRegistrado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "dni")
    private Number dni;

    @Column(name = "edad")
    private Number edad;

    @OneToMany(mappedBy = "contribuyente")
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

        /*public void cargarHecho(Hecho hecho) {
                hecho.setFechaCarga(LocalDateTime.now());
                hecho.setContribuyente(this);
                hecho.agregarOrigen(Origen.DINAMICA);
                hechos.add(hecho);
        }*/

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
