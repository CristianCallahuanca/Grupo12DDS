package org.example.metamapa.admin.Controladores;

import org.example.metamapa.admin.Servicios.IColeccionesService;
import org.example.metamapa.admin.Servicios.Implementaciones.ColeccionesService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("apiadministrativa")
public class ColeccionesController {

    private final IColeccionesService colecciones;

    public ColeccionesController(IColeccionesService coleccionesService){
        this.colecciones = coleccionesService;
    }

    @GetMapping("/saludo")
    public String saludo() {
        return colecciones.obtenerSaludo();
    }

}
