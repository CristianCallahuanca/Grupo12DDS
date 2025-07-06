package Handlers;

import AdministracionDeHechos.CriterioPertenencia.*;
import AdministracionDeHechos.CriterioPertenencia.PorTitulo;
import Fuentes.Fuente;
import Fuentes.FuenteDinamica;
import Fuentes.FuenteEstatica.FuenteEstatica;
import Fuentes.Proxy.FuenteDemo;
import Fuentes.Proxy.FuenteMetaMapa;
import AdministracionDeHechos.Origen;
import AdministracionDeHechos.Ubicacion;
import java.time.LocalDateTime;
import Fuentes.Fuente;
import Fuentes.FuenteDinamica;
import Fuentes.FuenteEstatica.FuenteEstatica;
import Fuentes.Proxy.FuenteDemo;
import Fuentes.Proxy.FuenteMetaMapa;

import java.util.Map;

public class ConversorStringObjetos {

    public static Fuente JsonAFuente(String tipo){

        return switch(tipo.toLowerCase()){
            case "fuenteestatica" -> FuenteEstatica.getInstancia();

            case "fuentedinamica" -> FuenteDinamica.getInstancia();

            case "fuentemetamapa" -> FuenteMetaMapa.getInstancia();

            case "fuentedemo" -> FuenteDemo.getInstancia();

            default -> throw new IllegalArgumentException("Tipo de fuente no valida");
        };
    }

    public static CriterioDePertenencia JsonACriterio(String tipo, Map<String, String> params){

        return switch (tipo.toLowerCase()) {
            case "portitulo" -> new PorTitulo(params.get("tituloBuscado"));

            case "porcategoria" -> new PorCategoria(params.get("categoriaDeseada"));

            case "pordescripcion" -> new PorDescripcion(params.get("fraseClave"));

            case "poretiqueta" -> new AdministracionDeHechos.CriterioPertenencia.PorEtiqueta(params.get("etiquetaDeseada"));

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
