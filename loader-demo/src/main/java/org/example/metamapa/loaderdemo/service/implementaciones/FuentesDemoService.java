package org.example.metamapa.loaderdemo.service.implementaciones;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.loaderdemo.models.dto.FuenteDemoDTO;
import org.example.metamapa.loaderdemo.models.entidades.FuenteDemo;
import org.example.metamapa.loaderdemo.models.repositorio.IFuenteDemoRepositorio;
import org.example.metamapa.loaderdemo.service.IFuentesDemoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuentesDemoService implements IFuentesDemoService {

    private final IFuenteDemoRepositorio repo;

    @Override
    public FuenteDemoDTO registrarFuenteDemo(String nombreFuente,
                                             String url,
                                             String pathApi,
                                             String email,
                                             String password) {

        FuenteDemo fuente = FuenteDemo.builder()
                .nombre(nombreFuente)
                .urlBase(url)
                .pathApi(pathApi)
                .paginaActual(1)
                .activa(true)
                .authEmail(email)
                .authPassword(password)
                .ultimaConsulta(null)
                .build();

        FuenteDemo guardada = repo.save(fuente);

        return new FuenteDemoDTO(
                guardada.getId(),
                guardada.getNombre(),
                guardada.getUrlBase(),
                guardada.getPathApi(),
                guardada.getActiva(),
                guardada.getNombreDetectado(),
                guardada.getEtiquetaDetectada()
        );
    }


    @Override
    public List<FuenteDemoDTO> listarFuentesDemo() {
        return repo.findAll().stream()
                .map(f -> new FuenteDemoDTO(
                        f.getId(),
                        f.getNombre(),
                        f.getUrlBase(),
                        f.getPathApi(),
                        f.getActiva(),
                        f.getNombreDetectado(),
                        f.getEtiquetaDetectada()
                ))
                .toList();
    }

    @Override
    public List<FuenteDemo> obtenerFuentesActivas() {
        return repo.findByActivaTrue();
    }
}

