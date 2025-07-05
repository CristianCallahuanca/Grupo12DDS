package Handlers.handlerColeccion;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.*;
import AdministracionDeHechos.Origen;
import AdministracionDeHechos.Ubicacion;
import Fuentes.Fuente;
import Fuentes.FuenteDinamica;
import Fuentes.FuenteEstatica.FuenteEstatica;
import Fuentes.Proxy.FuenteDemo;
import Fuentes.Proxy.FuenteMetaMapa;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateColeccionesHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        String handle = ctx.pathParam("handle");
        BodyColeccion datos = ctx.bodyAsClass(BodyColeccion.class);
        List<Fuente> listFuentes = new ArrayList<Fuente>();

        Coleccion coleccion = ColeccionRepositoryEnMemoria.getInstancia().buscarPorHandle(handle);

        if (datos.getTitulo() != null) {
            coleccion.setTitulo(datos.getTitulo());
        }

        if (datos.getDescripcion() != null) {
            coleccion.setDescripcion(datos.getDescripcion());
        }

        if (datos.getFuentes() != null) {

            for(String fuente: datos.getFuentes()){

                listFuentes.add(JsonAFuente(fuente));
            }

            coleccion.setFuentes(listFuentes);
        }

        if (datos.getCriterios() != null) {

            List<CriterioDePertenencia> nuevosCriterios = new ArrayList<CriterioDePertenencia>();

            for (CriterioDTO criterio : datos.getCriterios()) {

                nuevosCriterios.add(crearCriterio(criterio.tipo, criterio.parametros));
            }
            coleccion.setCriterios(nuevosCriterios);
        }

    }

    public Fuente JsonAFuente(String tipo){

        return switch(tipo.toLowerCase()){
            case "fuenteestatica" -> FuenteEstatica.getInstancia();

            case "fuentedinamica" -> FuenteDinamica.getInstancia();

            case "fuentemetamapa" -> FuenteMetaMapa.getInstancia();

            case "fuentedemo" -> FuenteDemo.getInstancia();

            default -> throw new IllegalArgumentException("Tipo de fuente no valida");
        };
    }

    public CriterioDePertenencia crearCriterio(String tipo, Map<String, String> params) {
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
            default -> throw new IllegalArgumentException("Tipo de criterio no válido: " + tipo);
        };
    }
}
