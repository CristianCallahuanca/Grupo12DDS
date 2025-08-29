package dinamico.models.entidades.contribuyente_registrado;


import dinamico.models.entidades.hecho.HechoCrudo;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Contribuyente_registrado {

    private String nombre;
    private String apellido;
    private int edad;
    private String id;
    protected List<HechoCrudo> listaDeHechos = new ArrayList<>();

    public Contribuyente_registrado(String nombre, String apellido, int edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
    }

    public void agregarAListaDeHechos(HechoCrudo unHecho) {
        listaDeHechos.add(unHecho);
    }

    protected void cargarHecho(HechoCrudo hecho) {
        hecho.setFechaCarga(LocalDateTime.now());
        agregarAListaDeHechos(hecho);
    }

    //Un contribuyente sube un hecho de forma anonima, pero conserva la referencia a la misma
    public void cargarHechoAnonimo(HechoCrudo hecho) {
        cargarHecho(hecho);
    }

    //Un contribuyente registrado puede subir un hecho de forma publica
    public void cargarHechoPublico(HechoCrudo hecho) {
        hecho.setContribuyenteID(this.id);
        cargarHecho(hecho);
    }

    //Un contribuyente debería poder borrar su propio Hecho sin pasar por una solicitud de eliminación
    public void eliminarHecho(HechoCrudo hecho) {

        //Debería hacerse una comprobación de que el hecho es del usuario y no de otra persona
        //hecho.marcarComoNoVisible();

        //Un contribuyente no quita sus hechos, al marcarse como no visible bastaria, no?
        //listaDeHechos.remove(hecho);
    }

    public void solicitarModificarHecho(HechoCrudo hechoAModificar, HechoCrudo cambios) {
        /*if (hechoAModificar.puedeSerEditado()) {
            hechoAModificar.editarCon(cambios);
        } else {
            throw new IllegalStateException("El hecho no puede ser editado despues de una semana.");
        }*/
    }
}
