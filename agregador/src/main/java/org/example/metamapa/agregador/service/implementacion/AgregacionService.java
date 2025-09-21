package org.example.metamapa.agregador.service.implementacion;

import jakarta.transaction.Transactional;
import org.example.metamapa.agregador.models.dtos.DTO_IN.HechoDTO_IN;
import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.example.metamapa.agregador.service.INormalizacionService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgregacionService {
    private final RestClient estatico, dinamico, proxy;

    private final NormalizacionService normalizacionService;
    private final DuplicacionService duplicacionService;
    private final IRepositorioHechos hechosRepository;

    public AgregacionService(NormalizacionService normalizacionService,DuplicacionService duplicacionService,RestClient loaderEstatico,
                             RestClient loaderDinamico, RestClient loaderProxy,IRepositorioHechos hechosRepository) {
        this.estatico = loaderEstatico;
        this.dinamico = loaderDinamico;
        this.proxy = loaderProxy;
        this.normalizacionService = normalizacionService;
        this.duplicacionService = duplicacionService;
        this.hechosRepository = hechosRepository;
    }

    public List<HechoDTO_IN> getHechosDTO3FuentesSinLimpiar() {
        List<HechoDTO_IN> all = new ArrayList<>();
        //all.addAll(listar(estatico));
        all.addAll(listar(dinamico));
        //all.addAll(listar(proxy));
        System.out.println("se recibieron de las fuentes: " + all.size() + "hechos");

        return all;
    }

    private List<HechoDTO_IN> listar(RestClient c) {
        return c.get().uri("/hechos")
                .retrieve().body(new ParameterizedTypeReference<List<HechoDTO_IN>>() {});
    }

    @Transactional
    public void integrarHechosFuentes(){

        //obtenermos todos los hechos de las fuentes los normalizamos sacamos duplicados y los almacenamos en BD

         List<Hecho> hechos = normalizacionService.normalizarHechos(this.getHechosDTO3FuentesSinLimpiar());

         hechosRepository.saveAll(hechos);
    }
}