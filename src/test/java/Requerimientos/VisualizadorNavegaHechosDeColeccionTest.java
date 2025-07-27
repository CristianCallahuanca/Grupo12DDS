package Requerimientos;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import AdministracionDeHechos.Ubicacion;
import Fuentes.Fuente;
import Infraestructura.Repositorios.HechoRepositorio;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VisualizadorNavegaHechosDeColeccionTest {

    @Test
    public void visualizarHechosDesdeFuenteDinamica() throws IOException {

        HechoRepositorio.getInstancia().limpiar();//TEST
        System.out.println("Hechos al iniciar test: " + HechoRepositorio.getInstancia().obtenerTodas().size());


        Hecho h1 = new Hecho("Inundacion", "Descripcion 1", "Clima",
                new Ubicacion(-34.5, -58.4), LocalDateTime.now(), "Manual");
        h1.setOrigen(Origen.DINAMICA);

        Hecho h2 = new Hecho("Incendio", "Descripcion 2", "Fuego",
                new Ubicacion(-36.2, -60.1), LocalDateTime.now(), "Manual");
        h2.setOrigen(Origen.DINAMICA);


        System.out.println("Hechos despues de guardar: " + HechoRepositorio.getInstancia().obtenerTodas().size());

        Fuente fuenteDinamica = new Fuente() {
            @Override
            public List<Hecho> obtenerHechos() {
                return HechoRepositorio.getInstancia().obtenerTodas();
            }

            /* No se porque esto estaba overrideado, imagino que el test quedó viejo.
            Si querés filtrar para hacer andar el test (que no entiendo), usa el servicio
            @Override
            public List<Hecho> filtrarHechos(List<CriterioDePertenencia> criterios) {
                return HechoRepositorio.getInstancia().obtenerTodas().stream()
                        .filter(h -> h.filtrarHecho(criterios))
                        .toList();
            }
            */
        };

/*
        Coleccion coleccion = new Coleccion(fuenteDinamica, "Eventos Dinamicos",
                "Ejemplo test fuente dinamica", List.of(), datos.getHandle());
        System.out.println("Hechos después de crear colección: " + coleccion.obtenerHechos().size());

        List<Hecho> hechosDeLaColeccion = coleccion.obtenerHechos();

        assertEquals(2, hechosDeLaColeccion.size());
        assertTrue(hechosDeLaColeccion.stream().anyMatch(h -> h.getTitulo().equals("Inundacion")));
        assertTrue(hechosDeLaColeccion.stream().anyMatch(h -> h.getTitulo().equals("Incendio")));
        hechosDeLaColeccion.forEach(h -> System.out.println("Hecho: " + h.getTitulo()));
*/
    }
}
