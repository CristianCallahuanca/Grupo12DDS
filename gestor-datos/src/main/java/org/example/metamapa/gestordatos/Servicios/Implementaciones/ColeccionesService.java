package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.example.metamapa.gestordatos.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.gestordatos.models.dtos.input.FuenteInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.Coleccion;
import org.example.metamapa.gestordatos.models.repositorios.IColeccionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColeccionesService implements IColeccionesService {

    @Autowired
    private IColeccionesRepository coleccionesRepository;


    public void aplicarConsensoATodas(){
        List<Coleccion> colecciones = coleccionesRepository.findAll();
        for(Coleccion coleccion : colecciones){
            if(coleccion.getAlgoritmo() != null){ //no sé si esta parte es necesaria
                coleccion.aplicarConsenso();
                coleccionesRepository.save(coleccion);
            }
        }
    }

    //List<ColeccionOutputDTO> obtenerColecciones();

    ColeccionOutputDTO crearColeccion(ColeccionInputDTO dto);{

    }



    //ColeccionOutputDTO editarColeccion(Long id, ColeccionInputDTO dto);

    //void eliminarColeccion(Long id);

    //ColeccionOutputDTO cambiarAlgoritmo(Long id, String nuevoAlgoritmo);

    //ColeccionOutputDTO agregarFuente(Long idColeccion, FuenteInputDTO fuente);

    //ColeccionOutputDTO quitarFuente(Long idColeccion, Long idFuente);

}
