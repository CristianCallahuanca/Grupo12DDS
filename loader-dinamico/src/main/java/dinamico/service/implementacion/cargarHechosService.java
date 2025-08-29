package dinamico.service.implementacion;

import dinamico.models.dtos.input.HechoCrudoDTO_IN;
import dinamico.models.entidades.hecho.HechoCrudo;
import dinamico.models.repositorios.IRepositorioHechosCrudos;
import org.springframework.stereotype.Service;

@Service
public class cargarHechosService {

    //Aca debemos tener en cuenta de validar permisos (anonimo/registrado)
    //Llamar a los repositorios para persistencia
    //Preparar los DTOs

    private final IRepositorioHechosCrudos hechosRepository;
    private Object List;

    public cargarHechosService(IRepositorioHechosCrudos hechosRepository){
        this.hechosRepository = hechosRepository;
    }

    /*
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;
    private String etiqueta;
    private String contribuyenteID;
    private List<String> archivosMultimedia;
    */

    public void cargarHecho(HechoCrudoDTO_IN hecho){

        HechoCrudo hechoCrudo = new HechoCrudo(
                hecho.getTitulo(),
                hecho.getDescripcion(),
                hecho.getCategoria(),
                hecho.getLatitud(),
                hecho.getLongitud(),
                hecho.getFechaAcontecimiento(),
                hecho.getEtiqueta(),
                "id a agregar",
                hecho.getArchivosMultimedia()
        )
    }

    /*
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;
    private String etiqueta;
    private List<String> archivosMultimedia;
    */
}




