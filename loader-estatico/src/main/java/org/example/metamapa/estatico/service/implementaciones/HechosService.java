package org.example.metamapa.estatico.service.implementaciones;

import jakarta.transaction.Transactional;
import org.example.metamapa.estatico.models.dtos.HechoDTO;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;
import org.example.metamapa.estatico.models.repositorios.IRepositorioHechos;
import org.example.metamapa.estatico.service.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HechosService implements IHechosService {

    private final IRepositorioHechos repositorioHechos;

    @Value("${loader.self.id}")
    private String loaderId;

    @Autowired
    public HechosService(IRepositorioHechos repositorioHechos) {
        this.repositorioHechos = repositorioHechos;
    }

    @Override
    @Transactional
    public List<HechoDTO> obtenerHechos() {
        List<HechoCrudo> hechosNoEnviados = repositorioHechos.findByEnviadoFalse(loaderId);

        if (hechosNoEnviados.isEmpty()) {
            return List.of();
        }

        hechosNoEnviados.forEach(hecho -> {
            hecho.setEnviado(true);
            hecho.setFechaEnvio(LocalDateTime.now());
        });

        repositorioHechos.saveAll(hechosNoEnviados);

        return hechosNoEnviados.stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    private HechoDTO mapearADTO(HechoCrudo hecho) {
        return new HechoDTO(
                hecho.getTitulo(),
                hecho.getDescripcion(),
                hecho.getCategoria(),
                hecho.getLatitud(),
                hecho.getLongitud(),
                hecho.getFechaAcontecimiento(),
                null,
                null,
                Collections.emptyList(),
                null,
                null
        );
    }
}

