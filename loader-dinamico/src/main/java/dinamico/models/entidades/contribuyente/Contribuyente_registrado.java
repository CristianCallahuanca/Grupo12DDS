package dinamico.models.entidades.contribuyente;

import dinamico.models.entidades.hecho.HechoCrudo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Contribuyente_registrado extends Contribuyente {
    private String nombre;
    private String apellido;
    private int edad;
    private String id;

    public Contribuyente_registrado(String nombre, String apellido, int edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
    }

    //Un contribuyente registrado puede subir un hecho de forma publica
    public void cargarHechoPublico(HechoCrudo hecho) {
        hecho.setContribuyenteID(this.id);
        cargarHecho(hecho);
    }

    //Un contribuyente debería poder borrar su propio Hecho sin pasar por una solicitud de eliminación
    public void eliminarHecho(HechoCrudo hecho) {
        hecho.marcarComoNoVisible();

        //Un contribuyente no quita sus hechos, al marcarse como no visible bastaria, no?
        //listaDeHechos.remove(hecho);
    }

    public void solicitarModificarHecho(HechoCrudo hechoAModificar, HechoCrudo cambios) {
        if (hechoAModificar.puedeSerEditado()) {
            hechoAModificar.editarCon(cambios);
        } else {
            throw new IllegalStateException("El hecho no puede ser editado despues de una semana.");
        }
    }
}