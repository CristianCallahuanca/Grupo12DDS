package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.example.metamapa.gestordatos.Servicios.IEstadisticaService;
import org.example.metamapa.gestordatos.models.entidades.consultas.HechosPorProvincia;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.example.metamapa.gestordatos.models.repositorios.consultas.*;
import org.springframework.stereotype.Service;

@Service
public class EstadisticaService implements IEstadisticaService {
    
    private final IMayorCantidadCategoriaProvincia repoMCCP;
    private final IMayorCantidadHechosProvincia repoCHP;
    private final IMayorCantidadHechosCategoria repoCHC;
    private final IHoraDelDia repoHDD;
    private final ICantSolicitudesSpam repoCSS;
    private final IColeccionesService repoColecciones;
    private final IHechosRepository repoHecho;

    EstadisticaService(IMayorCantidadCategoriaProvincia repoMCCP,
                       IMayorCantidadHechosProvincia repoCHP,
                       IMayorCantidadHechosCategoria repoCHC,
                       IHoraDelDia repoHDD,
                       ICantSolicitudesSpam repoCSS,
                       IColeccionesService repoColecciones,
                       IHechosRepository repoHecho){
        this.repoMCCP = repoMCCP;
        this.repoCHP = repoCHP;
        this.repoCHC = repoCHC;
        this.repoHDD = repoHDD;
        this.repoCSS = repoCSS;
        this.repoColecciones = repoColecciones;
        this.repoHecho = repoHecho;
    }

    public void generarEstadisticas(){
        generarEstadisticaMayorCantHechosProvincia();
        generarEstadisticaMayorCantHechosCategoria();
        generarEstadisticaMayorCantCategoriaProvincia();
        generarEstadisticaHoraDelDia();
        generarEstadisticaCantidadSolicitudesSpam();

    }

    public void generarEstadisticaMayorCantHechosProvincia(){

        HechosPorProvincia resultado = repoCHP.findTopProvinciaColeccion();

        System.out.println("Colección: " + resultado.getTitulo());
        System.out.println("Provincia: " + resultado.getProvincia());
        System.out.println("Cantidad: " + resultado.getCantidad_hechos());

    }

    public void generarEstadisticaMayorCantHechosCategoria(){

    }

    public void generarEstadisticaMayorCantCategoriaProvincia(){

    }

    public void generarEstadisticaHoraDelDia(){

    }

    public void generarEstadisticaCantidadSolicitudesSpam(){

    }
}
