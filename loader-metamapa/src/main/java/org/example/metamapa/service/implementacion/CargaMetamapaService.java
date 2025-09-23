package org.example.metamapa.service.implementacion;
import lombok.RequiredArgsConstructor;

import org.example.metamapa.adapters.IAdapterMetamapa;
import org.example.metamapa.exceptions.ExcepcionConexionMetamapa;
import org.example.metamapa.models.dtos.HechoDTO;
import org.example.metamapa.models.dtos.HechoDTO_IN;
import org.example.metamapa.service.ICargaMetamapaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CargaMetamapaService implements ICargaMetamapaService {

    private final IAdapterMetamapa adapter;

    @Override
    public ResponseEntity<List<HechoDTO>> obtenerHechos() {
        try {
            List<HechoDTO_IN> hechosEntrantes = adapter.obtenerHechos();

            List<HechoDTO> hechosListos = hechosEntrantes.stream()
                    .map(this::mapearHecho)
                    .toList();

            return ResponseEntity.ok(hechosListos);
        } catch (ExcepcionConexionMetamapa e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
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
