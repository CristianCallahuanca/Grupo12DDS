package org.example.metamapa.admin.Controladores;

import org.example.metamapa.admin.models.dtos.output.ColeccionOutputDto;
import org.example.metamapa.admin.servicios.impl.IColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("apiadministrativa/colecciones")
public class ColeccionesController {

    @Autowired
    private IColeccionService coleccionService;

    @GetMapping
    public List<ColeccionOutputDto> buscarTodasLasColecciones() {
        return this.coleccionService.buscarTodas(); //implementar buscarTodas
    }
}
