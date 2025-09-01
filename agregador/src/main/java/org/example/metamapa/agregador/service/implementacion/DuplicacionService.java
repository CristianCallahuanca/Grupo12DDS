package org.example.metamapa.agregador.service.implementacion;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.entidades.filtros.FilterCondition;
import org.example.metamapa.agregador.models.entidades.filtros.PorFechaCarga;
import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DuplicacionService {
    /*
    * Si un varios hechos ocurrieron en el mismo lugar  && fecha de acontecimiento son candidatos a ser duplicado
    *   si además tienen el mismo (titulo && categoria) || id_contribuyente podemos asegurarlo
    * */

    @Autowired
    private IRepositorioHechos  repositorioHechos;

    private List<Hecho> obtenerLosUltimosHechos(){
        LocalDateTime horaActual = LocalDateTime.now();
        FilterCondition ultimaHora = new PorFechaCarga(horaActual.minusHours(1), horaActual);

        return repositorioHechos.obtenerTodosLosHechosDelSistema().stream().
                filter(ultimaHora::cumpleUno).toList();
    }

    private double[] obtenerBoundingBox(Hecho unHecho) {
        return calcularBoundingBox(unHecho.getUbicacion().getLatitud(), unHecho.getUbicacion().getLongitud(), 100.0);
    }

    private double[] calcularBoundingBox(double lat, double lon, double radioMetros) {
        double metrosPorGradoLat = 111320.0;
        double metrosPorGradoLon = 111320.0 * Math.cos(Math.toRadians(lat));

        double deltaLat = radioMetros / metrosPorGradoLat;
        double deltaLon = radioMetros / metrosPorGradoLon;

        double minLat = lat - deltaLat;
        double maxLat = lat + deltaLat;
        double minLon = lon - deltaLon;
        double maxLon = lon + deltaLon;

        return new double[]{minLat, maxLat, minLon, maxLon};
    }

    private boolean between(double var, double min, double max) {
        return min <= var && var <= max;
    }

    private boolean hechoEnBoundingBox(double latitud, double longitud, double[] box){
        return between(latitud, box[0], box[1]) &&
                between(longitud, box[2], box[3]);
    }

    private boolean estaEnBoundingBox(Hecho unHecho, double[] box){
        return hechoEnBoundingBox(unHecho.getUbicacion().getLatitud(), unHecho.getUbicacion().getLongitud(), box);
    }

    private boolean sonHechosDistintos(Hecho h1, Hecho h2){
        return !h1.getId_hecho().equals(h2.getId_hecho());
    }

    private boolean ocurrieronMismoDia(Hecho h1, Hecho h2){
        return h1.getFechaAcontecimiento().toLocalDate().equals(h2.getFechaAcontecimiento().toLocalDate());
    }

    private boolean estaRepetido(Hecho h1, Hecho h2){
        return (h1.getTitulo().equals(h2.getTitulo()) && h1.getCategoria().equals(h2.getCategoria())) || h1.getContribuyente_id().equals(h2.getContribuyente_id());
    }

    private List<Hecho> obtenerHechosMismoLugar(List<Hecho> hechosDelPeriodo){
        List<Hecho> hechosMismaUbicacion = new ArrayList<>();

        for(int i=0; i <hechosDelPeriodo.size(); i++){
            double[] boxDelHecho = obtenerBoundingBox(hechosDelPeriodo.get(i));
            Hecho hechoDeLaIteracion = hechosDelPeriodo.get(i);

            List<Hecho> hs = hechosDelPeriodo.stream()
                    .filter(h -> estaEnBoundingBox(h, boxDelHecho)
                            && sonHechosDistintos(h, hechoDeLaIteracion)).toList();
            hechosMismaUbicacion.addAll(hs);
        }

        return hechosMismaUbicacion;
    }

    private List<Hecho> obtenerHechosMismaFechaAcontecimiento(List<Hecho> hechosDelPeriodo){
        List<Hecho> hechosMismaFecha = new ArrayList<>();

        for(int i=0; i <hechosDelPeriodo.size(); i++){
            Hecho hechoDeLaIteracion = hechosDelPeriodo.get(i);

            List<Hecho> hs = hechosDelPeriodo.stream()
                    .filter(h -> ocurrieronMismoDia(h, hechoDeLaIteracion)
                            && sonHechosDistintos(h, hechoDeLaIteracion)).toList();
            hechosMismaFecha.addAll(hs);
        }

        return hechosMismaFecha;
    }

    private List<Hecho> obtenerHechosRepetidos(List<Hecho> hechosDelPeriodo){
        List<Hecho> hechosRepetidos = new ArrayList<>();

        for(int i=0; i <hechosDelPeriodo.size(); i++){
            Hecho hechoDeLaIteracion = hechosDelPeriodo.get(i);

            List<Hecho> hs = hechosDelPeriodo.stream()
                    .filter(h -> estaRepetido(h, hechoDeLaIteracion)
                            && sonHechosDistintos(h, hechoDeLaIteracion)).toList();
            hechosRepetidos.addAll(hs);
        }

        return hechosRepetidos;
    }

    //Devuelve True si se borraron con exito
    public boolean eliminarHechosRepetidos(List <Hecho> hechosDeLosLoaders){
        List <Hecho> candidatosAEliminar = obtenerHechosMismaFechaAcontecimiento(obtenerHechosMismoLugar(hechosDeLosLoaders));

        return hechosDeLosLoaders.removeAll(candidatosAEliminar);
    }
}