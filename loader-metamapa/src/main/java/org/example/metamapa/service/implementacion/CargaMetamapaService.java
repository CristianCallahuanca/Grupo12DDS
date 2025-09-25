package org.example.metamapa.service.implementacion;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.metamapa.adapters.IAdapterMetamapa;
import org.example.metamapa.exceptions.ExcepcionConexionMetamapa;
import org.example.metamapa.models.dtos.HechoDTO;
import org.example.metamapa.models.dtos.HechoDTO_IN;
import org.example.metamapa.models.entidades.EstadoConsulta;
import org.example.metamapa.models.repositorio.IEstadoConsultaRepositorio;
import org.example.metamapa.service.ICargaMetamapaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CargaMetamapaService implements ICargaMetamapaService {

    private final IAdapterMetamapa adapter;
    private final IEstadoConsultaRepositorio estadoRepo;

    @Value("${loader.id}")
    private String loaderId;


    //TENER EN CUENTA ESTO PARA UNA POSIBLE MEJORA, comentario ABAJO
    @PostConstruct
    public void validarLoaderIdUnico() {
        if (estadoRepo.existsById(loaderId)) {
            throw new IllegalStateException("Ya existe un loader con ID " + loaderId + ". Cambia la configuración.");
        }
    }


    @Override
    public List<HechoDTO> obtenerHechos() {
        LocalDateTime ultimaConsulta = estadoRepo.findById(loaderId)
                .filter(e -> "OK".equals(e.getEstado()))
                .map(EstadoConsulta::getUltimaConsulta)
                .orElse(null);

        List<HechoDTO> hechosListos;
        try {
            List<HechoDTO_IN> hechosEntrantes = adapter.obtenerHechos(ultimaConsulta);
            hechosListos = hechosEntrantes.stream()
                    .map(this::mapearHecho)
                    .toList();
            registrarEstado(hechosListos, "OK");
        } catch (ExcepcionConexionMetamapa e) {
            registrarEstado(Collections.emptyList(), "ERROR");
            throw e;
        }


        return hechosListos;
    }

    private void registrarEstado(List<HechoDTO> hechosListos, String estado) {
        EstadoConsulta estadoConsulta = new EstadoConsulta(
                loaderId,
                LocalDateTime.now(),
                hechosListos.size(),
                estado
        );
        estadoRepo.save(estadoConsulta);
    }


    private HechoDTO mapearHecho(HechoDTO_IN in) {
        return HechoDTO.builder()
                .titulo(in.getTitulo())
                .descripcion(in.getDescripcion())
                .categoria(in.getCategoria())
                .latitud(in.getLatitud())
                .longitud(in.getLongitud())
                .fechaAcontecimiento(in.getFechaAcontecimiento())
                .etiqueta(in.getEtiqueta())
                .contribuyenteID(in.getContribuyenteID())
                .archivosMultimedia(in.getArchivosMultimedia())
                .sinCategorizar(in.getSinCategorizar())
                .fechaAcontecimientoPosta(in.getFechaAcontecimientoPosta())
                .build();
    }
}


/*El tratamiento de error al levantar una instancia para los mismos servicios de loaderMetama
podemos mejorarlo pensando de esta forma

1)
En la entidad CLASE CONSULTA:
   @Id
    private String loaderId;     // ej: "metamapa-cordoba"

    @Id
    private String instanciaId;  // UUID generado al iniciar

2)
Se crea una clase para serializar la clase y mandarlo al repo

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoConsultaPK implements Serializable {
    private String loaderId;
    private String instanciaId;
}

3)
El repositorio cambia el tipo de PK
public interface IEstadoConsultaRepositorio extends JpaRepository<EstadoConsulta, EstadoConsultaPK> {
}

4)
Y en el service poner generar los ID de esta forma
@Value("${loader.id}")
private String loaderId;

private final String instanciaId = UUID.randomUUID().toString();

PERO dependiendo la escalabilidad del uso podemos usar el instanciaId si es que se requiere en mas instancias.
por el momento solo nos interesa aca.


CREERIA QUE ESTA FORMA ES LA MAS CLEAN y salva de errores de automatizacion y no sobre el control humano
 */
