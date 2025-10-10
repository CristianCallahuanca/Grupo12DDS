package org.example.metamapa.loaderdemo.service.implementaciones;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.metamapa.loaderdemo.models.dto.HechoDTO;
import org.example.metamapa.loaderdemo.models.entidades.HechoCrudo;
import org.example.metamapa.loaderdemo.models.repositorio.IRepositorioHechos;
import org.example.metamapa.loaderdemo.service.IHechosService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class HechosService implements IHechosService {

    private final IRepositorioHechos repositorio;

    @Transactional
    @Override
    public List<HechoDTO> listarHechos() {
        List<HechoCrudo> hechosNoEnviados = repositorio.findByEnviadoFalse();

        if (hechosNoEnviados.isEmpty()) {
            return List.of();
        }

        hechosNoEnviados.forEach(h -> {
            h.setEnviado(true);
            h.setFechaEnvio(LocalDateTime.now());
        });

        repositorio.saveAll(hechosNoEnviados);

        return hechosNoEnviados.stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    private HechoDTO mapearADTO(HechoCrudo h) {
        return new HechoDTO(
                h.getTitulo(),
                h.getDescripcion(),
                h.getCategoria(),
                h.getLatitud(),
                h.getLongitud(),
                h.getEtiqueta(),
                h.getFecha(),
                null,
                List.of(),
                false,
                h.getFecha()
        );
    }
}
