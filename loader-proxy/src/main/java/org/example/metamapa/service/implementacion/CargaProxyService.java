package org.example.metamapa.service.implementacion;

import org.example.metamapa.models.dtos.HechoDTO;
import org.example.metamapa.models.entidades.HechoCrudo;
import org.example.metamapa.models.entidades.FuenteConfigurada;
import org.example.metamapa.models.repositorios.IRepositorioFuenteProxy;
import org.example.metamapa.service.ICargaProxyService;
import org.example.metamapa.service.fuentes.IFuenteProxy;
import org.example.metamapa.service.fuentes.FuenteProxyFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CargaProxyService implements ICargaProxyService {

    private final IRepositorioFuenteProxy repositorioFuenteProxy;
    private final FuenteProxyFactory fuenteProxyFactory;

    public CargaProxyService(IRepositorioFuenteProxy repositorioFuenteProxy,
                             FuenteProxyFactory fuenteProxyFactory) {
        this.repositorioFuenteProxy = repositorioFuenteProxy;
        this.fuenteProxyFactory = fuenteProxyFactory;
    }

    @Override
    public List<HechoDTO> cargarHechos() {
        List<HechoCrudo> hechosTotales = new ArrayList<>();

        // 1. Obtener todas las fuentes registradas
        List<FuenteConfigurada> fuentes = repositorioFuenteProxy.obtenerTodas();

        // 2. Para cada fuente, construir una instancia real y traer los hechos
        for (FuenteConfigurada fuenteConf : fuentes) {
            try {
                IFuenteProxy  fuente = fuenteProxyFactory.construirFuente(fuenteConf);
                hechosTotales.addAll(fuente.cargarHechosExternos());
            } catch (Exception e) {
                System.err.println("⚠️ No se pudo cargar la fuente: " + fuenteConf.getNombre() + " -> " + e.getMessage());
            }
        }

        // 3. Transformar a DTO
        return hechosTotales.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private HechoDTO convertirADTO(HechoCrudo hecho) {
        HechoDTO dto = new HechoDTO();
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setLatitud(hecho.getLatitud());
        dto.setLongitud(hecho.getLongitud());
        dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
        dto.setEtiqueta(hecho.getEtiqueta());
        dto.setContribuyenteID(hecho.getContribuyenteID());
        dto.setArchivosMultimedia(hecho.getArchivosMultimediaComoLista());
        dto.setSinCategorizar(hecho.getCategoria() == null || hecho.getCategoria().isBlank());
        dto.setFechaAcontecimientoPosta(parseFechaAcontecimiento(hecho.getFechaAcontecimiento()));
        return dto;
    }

    private LocalDateTime parseFechaAcontecimiento(String fecha) {
        try {
            return LocalDateTime.parse(fecha); // ISO 8601: "2024-09-01T10:15:30"
        } catch (Exception e) {
            return null;
        }
    }
}
