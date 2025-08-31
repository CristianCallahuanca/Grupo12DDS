package org.example.metamapa.estatico.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estatico.models.dtos.HechoCrudoDTO;
import org.example.metamapa.estatico.service.IRecopiladorHechos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/hechos-crudos")
@Slf4j
public class HechosCrudosController {


    private final IRecopiladorHechos recopiladorHechos;

    @Autowired
    public HechosCrudosController(IRecopiladorHechos recopiladorHechos) {

        this.recopiladorHechos = recopiladorHechos;
    }

    @GetMapping
    public List<HechoCrudoDTO> obtenerHechosCrudos(@RequestParam(name = "cantidad", defaultValue = "100") int cantidad) throws IOException {
        log.info("Llego una peticion para obtener HechosCrudos");
        return recopiladorHechos.obtenerHechosCrudos(cantidad);
    }
}
