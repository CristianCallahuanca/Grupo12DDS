package org.example.metamapa.gestordatos.conversores;


import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.*;
import org.example.metamapa.gestordatos.models.entidades.Consenso.Absoluto;
import org.example.metamapa.gestordatos.models.entidades.Consenso.AlgoritmoConsenso;
import org.example.metamapa.gestordatos.models.entidades.Consenso.MayoriaSimple;
import org.example.metamapa.gestordatos.models.entidades.Consenso.MultiplesMenciones;
import org.example.metamapa.gestordatos.models.entidades.ModosNavegacion.Curada;
import org.example.metamapa.gestordatos.models.entidades.ModosNavegacion.Irrestricta;
import org.example.metamapa.gestordatos.models.entidades.ModosNavegacion.ModoNavegacion;
import org.example.metamapa.gestordatos.models.entidades.Ubicacion;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoHecho;
import org.example.metamapa.gestordatos.models.entidades.enums.Origen;

import java.time.LocalDateTime;
import java.util.Map;

public class StringAObjetos{

    public static ModoNavegacion modoNavegacionFactory(String modoNavegacion) {
        return switch (modoNavegacion.toLowerCase()) {
            case "curada" -> new Curada();
            case "irrestricta" -> new Irrestricta();

            default -> throw new IllegalArgumentException("Modo de navegacion no válido: " + modoNavegacion);
        };
    }

    public static CondicionDeFiltrado criterioFactory(CriterioRequest request) {
        String tipo = request.getTipo().toLowerCase();
        Map<String, String> params = request.getParams();

        return switch (tipo) {
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
            case "porfechacargadesde" -> new PorFechaCargaDesde(LocalDateTime.parse(params.get("desde")));
            case "porfechacargahasta" -> new PorFechaCargaHasta(LocalDateTime.parse(params.get("hasta")));

            case "porfechaacontecimientodesde" -> new PorFechaAcontecimientoDesde(LocalDateTime.parse(params.get("desde")));
            case "porfechaacontecimientohasta" -> new PorFechaAcontecimientoHasta(LocalDateTime.parse(params.get("hasta")));

            case "porestado" -> new PorEstado(EstadoHecho.valueOf(params.get("estadoHecho").toUpperCase()));
            case "porsincategorizar" -> new PorSinCategorizar(Boolean.parseBoolean(params.get("sinCategorizar")));
            case "poridcontribuyente" -> new PorIdContribuyente(params.get("idBuscado"));
            case "poridhecho" -> new PorIDHecho(Long.parseLong(params.get("idBuscado")));

            default -> throw new IllegalArgumentException("Tipo de criterio no válido: " + tipo);
        };
    }

    public static AlgoritmoConsenso algoritmoConsensoFactory(String algoritmo) {
        return switch (algoritmo.toLowerCase()) {
            case "mayoriasimple" -> new MayoriaSimple();
            case "absoluto" -> new Absoluto();
            case "multiplesmenciones" -> new MultiplesMenciones();

            default -> throw new IllegalArgumentException("Algoritmo de consenso no válido: " + algoritmo);
        };
    }

}
