package org.example.metamapa.estadisticas.Servicios.implementaciones;

import org.example.metamapa.estadisticas.Models.entidades.CategoriaMasFrecuente;
import org.example.metamapa.estadisticas.Models.entidades.EstadisticaGeneral;
import org.example.metamapa.estadisticas.Models.entidades.HechosPorProvincia;
import org.example.metamapa.estadisticas.Models.entidades.ProvinciaMasFrecuentePorCategoria;
import org.example.metamapa.estadisticas.Models.repositorios.ICategoriaMasFrecuente;
import org.example.metamapa.estadisticas.Models.repositorios.IEstadisticasGenerales;
import org.example.metamapa.estadisticas.Models.repositorios.IMayorCantidadHechosProvincia;
import org.example.metamapa.estadisticas.Models.repositorios.IProvinciaMasFrecuentePorCategoria;
import org.example.metamapa.estadisticas.Servicios.IEstadisticaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstadisticaService implements IEstadisticaService {

    //private final IMayorCantidadCategoriaProvincia repoMCCP;
    private final IMayorCantidadHechosProvincia repoCHP;
    private final ICategoriaMasFrecuente repoCMF;
    private final IEstadisticasGenerales repoEstadisticas;
    private final IProvinciaMasFrecuentePorCategoria repoPMFPC;
    //private final IHoraDelDia repoHDD;

    EstadisticaService(
                        IMayorCantidadHechosProvincia repoCHP,
                        IProvinciaMasFrecuentePorCategoria repoPMFPC,
                        ICategoriaMasFrecuente repoCMF,
                        IEstadisticasGenerales repoEstadisticas
                       //IHoraDelDia repoHDD){
                        ){
        //this.repoMCCP = repoMCCP;
        this.repoEstadisticas = repoEstadisticas;
        this.repoCHP = repoCHP;
        this.repoCMF = repoCMF;
        this.repoPMFPC = repoPMFPC;
        //this.repoHDD = repoHDD;
    }

    public void generarEstadisticas(){
        HechosPorProvincia CantHechosProvincia = generarEstadisticaMayorCantHechosProvincia();
        CategoriaMasFrecuente cateregoriaFrecuente = generarEstadisticaMayorCantHechosCategoria();
        ProvinciaMasFrecuentePorCategoria categoriasFrecuentesEnProvincia = generarEstadisticaProvinciaMasFrecuentePorCategoria();
        generarEstadisticaHoraDelDia();
        generarEstadisticaCantidadSolicitudesSpam();

        //EstadisticaGeneral estadisticaAPersistir = new EstadisticaGeneral(CantHechosProvincia, cateregoriaFrecuente, categoriasFrecuentesEnProvincia);
        //repoEstadisticas.save(estadisticaAPersistir);
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
            return new CategoriaMasFrecuente(null, 0);
        }
        else{
            return new CategoriaMasFrecuente(resultado.getCategoria(), resultado.getCantidad());
        }

        //System.out.println("Categoria" + resultado.getCategoria());
        //System.out.println("Hechos reportados" + resultado.getCantidad());
        //System.out.println("id del resultado" + resultado.getId());
    }

    public ProvinciaMasFrecuentePorCategoria generarEstadisticaProvinciaMasFrecuentePorCategoria(){
        ProvinciaMasFrecuentePorCategoria resultado = repoPMFPC.findTopProvinciaPorCategoria("buenos aires");
        List<ProvinciaMasFrecuentePorCategoria> resultadoAPersistir = new ArrayList<>();
        /*if (resultado.isEmpty()){
            return resultadoAPersistir.stream().map( e -> {
                e.setProvincia(null);
                e.setCantidad(0);
                return e;
            }).toList();
        }
        else{
            resultadoAPersistir.addAll(resultado);
            return resultadoAPersistir;
        }*/
        return resultado;
    }

    public void generarEstadisticaHoraDelDia(){

    }

    public void generarEstadisticaCantidadSolicitudesSpam(){

    }
}
