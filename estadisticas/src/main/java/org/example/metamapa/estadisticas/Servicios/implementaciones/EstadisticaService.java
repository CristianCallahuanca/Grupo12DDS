package org.example.metamapa.estadisticas.Servicios.implementaciones;

import org.example.metamapa.estadisticas.Models.entidades.CategoriaMasFrecuente;
import org.example.metamapa.estadisticas.Models.entidades.EstadisticaGeneral;
import org.example.metamapa.estadisticas.Models.entidades.HechosPorProvincia;
import org.example.metamapa.estadisticas.Models.repositorios.ICategoriaMasFrecuente;
import org.example.metamapa.estadisticas.Models.repositorios.IEstadisticasGenerales;
import org.example.metamapa.estadisticas.Models.repositorios.IMayorCantidadHechosProvincia;
import org.example.metamapa.estadisticas.Servicios.IEstadisticaService;
import org.springframework.stereotype.Service;

@Service
public class EstadisticaService implements IEstadisticaService {

    //private final IMayorCantidadCategoriaProvincia repoMCCP;
    private final IMayorCantidadHechosProvincia repoCHP;
    private final ICategoriaMasFrecuente repoCMF;
    private final IEstadisticasGenerales repoEstadisticas;
    //private final IHoraDelDia repoHDD;

    EstadisticaService(//IMayorCantidadCategoriaProvincia repoMCCP,
                       IMayorCantidadHechosProvincia repoCHP,
                       ICategoriaMasFrecuente repoCMF,
                       IEstadisticasGenerales repoEstadisticas
                       //IHoraDelDia repoHDD){
                        ){
        //this.repoMCCP = repoMCCP;
        this.repoEstadisticas = repoEstadisticas;
        this.repoCHP = repoCHP;
        this.repoCMF = repoCMF;
        //this.repoHDD = repoHDD;
    }

    public void generarEstadisticas(){
        HechosPorProvincia CantHechosProvincia = generarEstadisticaMayorCantHechosProvincia();
        CategoriaMasFrecuente cateregoriaFrecuente = generarEstadisticaMayorCantHechosCategoria();
        generarEstadisticaMayorCantCategoriaProvincia();
        generarEstadisticaHoraDelDia();
        generarEstadisticaCantidadSolicitudesSpam();

        EstadisticaGeneral estadisticaAPersistir = new EstadisticaGeneral(CantHechosProvincia, cateregoriaFrecuente);
        repoEstadisticas.save(estadisticaAPersistir);
    }

    public HechosPorProvincia generarEstadisticaMayorCantHechosProvincia(){

        HechosPorProvincia resultado = repoCHP.findTopProvinciaColeccion();

        HechosPorProvincia resultadoAPersistir;
        if (resultado == null){
            resultadoAPersistir = new HechosPorProvincia(null, null, 0);
        }
        else{
            resultadoAPersistir = new HechosPorProvincia(resultado.getTitulo(), resultado.getProvincia(), resultado.getCantidad_hechos());
        }

        System.out.println("id del resultado" + resultado.getId());
        System.out.println("el titulo: " + resultado.getTitulo());
        System.out.println("la provincia: " + resultado.getProvincia());
        System.out.println("cantidad de hechos: " + resultado.getCantidad_hechos());

        return resultadoAPersistir;
    }

    public CategoriaMasFrecuente generarEstadisticaMayorCantHechosCategoria() {

        CategoriaMasFrecuente resultado = repoCMF.findCategoriaMasFrecuente();
        CategoriaMasFrecuente resultadoAPersistir;
        if (resultado == null){
            resultadoAPersistir = new CategoriaMasFrecuente(null, 0);
        }
        else{
            resultadoAPersistir = new CategoriaMasFrecuente(resultado.getCategoria(), resultado.getCantidad());
        }

        return resultadoAPersistir;

        //System.out.println("Categoria" + resultado.getCategoria());
        //System.out.println("Hechos reportados" + resultado.getCantidad());
        //System.out.println("id del resultado" + resultado.getId());
    }

    public void generarEstadisticaMayorCantCategoriaProvincia(){

    }

    public void generarEstadisticaHoraDelDia(){

    }

    public void generarEstadisticaCantidadSolicitudesSpam(){

    }
}
