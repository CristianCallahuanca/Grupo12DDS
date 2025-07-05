package Handlers;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.*;
import AdministracionDeHechos.Origen;
import AdministracionDeHechos.Ubicacion;
import Fuentes.FuenteEstatica.FuenteEstatica;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import io.javalin.http.Context;
import io.javalin.http.Handler; // ¡ESTE import es clave!
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostColeccionesHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        BodyColeccion datos = ctx.bodyAsClass(BodyColeccion.class);

        List<CriterioDePertenencia> listCriterios = new ArrayList<CriterioDePertenencia>();

        for(CriterioDTO criterio : datos.getCriterios()){

            listCriterios.add(JsonACriterio(criterio.tipo,criterio.parametros));
        }

        //deshardcodear lo de fuente estatica

        //Coleccion coleccion = new Coleccion(FuenteEstatica.getInstancia(),datos.getTitulo(),datos.getDescripcion(),listCriterios,datos.getHandle());

        Coleccion coleccion = new Coleccion(
                List.of(FuenteEstatica.getInstancia()),
                datos.getTitulo(),
                datos.getDescripcion(),
                listCriterios
        );

        ColeccionRepositoryEnMemoria.getInstancia().guardar(coleccion);

        System.out.println("se creo la coleccion correctamente");

        System.out.println("tamanio de la lista:");

        System.out.println(listCriterios.size());

    }

    public CriterioDePertenencia JsonACriterio(String tipo, Map<String, String> params){

        return switch (tipo.toLowerCase()) {
            case "portitulo" -> new PorTitulo(params.get("tituloBuscado"));

            case "porcategoria" -> new PorCategoria(params.get("categoriaDeseada"));

            case "pordescripcion" -> new PorDescripcion(params.get("fraseClave"));

            case "poretiqueta" -> new PorEtiqueta(params.get("etiquetaDeseada"));

            case "pororigen" -> new PorOrigen(Origen.valueOf(params.get("unOrigen").toUpperCase()));

            case "porubicacion" -> new PorUbicacion(
                    new Ubicacion(
                            Double.parseDouble(params.get("latitud")),
                            Double.parseDouble(params.get("longitud"))
                    )
            );

            case "porfechacarga" -> new PorFechaCarga(
                    LocalDateTime.parse(params.get("desde")),
                    LocalDateTime.parse(params.get("hasta"))
            );

            case "porfechaacontecimiento" -> new PorFechaAcontecimiento(
                    LocalDateTime.parse(params.get("desde")),
                    LocalDateTime.parse(params.get("hasta"))
            );

            default -> throw new IllegalArgumentException("Tipo de criterio no válido: ");
        };
    }

}