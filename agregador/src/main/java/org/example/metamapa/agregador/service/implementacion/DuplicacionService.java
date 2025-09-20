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
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class DuplicacionService {
    /*
    * Si un varios hechos ocurrieron en el mismo lugar  && fecha de acontecimiento son candidatos a ser duplicado
    *   si además tienen el mismo (titulo && categoria) || id_contribuyente podemos asegurarlo
    * */

    @Autowired
    private IRepositorioHechos  repositorioHechos;

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
        return !(h1.getId() == h2.getId());
    }

    private boolean ocurrieronMismoDia(Hecho h1, Hecho h2){
        return h1.getFechaAcontecimiento().toLocalDate().equals(h2.getFechaAcontecimiento().toLocalDate());
    }

    private boolean estaRepetido(Hecho h1, Hecho h2){
        return (h1.getTitulo().equals(h2.getTitulo()) && h1.getCategoria().equals(h2.getCategoria())) &&
                h1.getContribuyente().getId() == h2.getContribuyente().getId();
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

    //Devuelve True si se borraron con exito METODO PRINCIPAL
    // Comentario 2, fue modificado y ahora devuelve la lista sin los repetidos. Falta testear

    // es medio una porquería esto pero no sabía cómo hacer ---> agrupa según duplicados
    // y se queda con un representante de cada grupo, sino se estaban borrando TODOS los hechos
    public List<Hecho> eliminarHechosRepetidos(List<Hecho> hechosDeLosLoaders) {
        List<Hecho> resultado = new ArrayList<>();
        Set<Long> idsProcesados = new HashSet<>();

        for (Hecho hecho : hechosDeLosLoaders) {
            if (idsProcesados.contains(hecho.getId())) {
                // ya lo procesamos en algún grupo
                continue;
            }

            // Buscar duplicados del hecho actual
            List<Hecho> grupo = hechosDeLosLoaders.stream()
                    .filter(h -> sonHechosDistintos(h, hecho))
                    .filter(h -> ocurrieronMismoDia(h, hecho))
                    .filter(h -> estaEnBoundingBox(h, obtenerBoundingBox(hecho))) // 100m de radio, ajustar
                    .filter(h -> estaRepetido(h, hecho))
                    .collect(Collectors.toList());

            // Agregar también el "hecho base" al grupo
            grupo.add(hecho);

            // Marcar todos como procesados ---> para no tener que volver a analizarlo
            grupo.forEach(h -> idsProcesados.add(h.getId()));

            // Elegir representante y agregar al resultado
            Hecho representante = elegirRepresentante(grupo);
            asociarTodosLosOrigenes(grupo, representante);
            resultado.add(representante);
        }

        return resultado;
    }

    private Hecho elegirRepresentante(List<Hecho> grupo) {
        return grupo.get(0); //tomo el primero
    }

    private void asociarTodosLosOrigenes(List<Hecho> grupo, Hecho representante){
        // Agrego los orígenes de los otros duplicados
        grupo.stream()
                .filter(h -> h != representante)
                .forEach(h -> representante.getOrigenes().addAll(h.getOrigenes()));

        // Evitar orígenes repetidos
        representante.setOrigenes(
                representante.getOrigenes().stream().distinct().toList()
        );
    }

}