package Requerimientos;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import AdministracionDeHechos.Ubicacion;
import Fuentes.Fuente;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VisualizadorNavegaHechosDeColeccionTest {

    @Test
    public void visualizarHechosDesdeFuenteDinamica() throws IOException {

        HechoRepositoryEnMemoria.getInstancia().limpiar();//TEST
        System.out.println("Hechos al iniciar test: " + HechoRepositoryEnMemoria.getInstancia().obtenerTodas().size());


        Hecho h1 = new Hecho("Inundacion", "Descripcion 1", "Clima",
                new Ubicacion(-34.5, -58.4), LocalDateTime.now(), "Manual");
        h1.setOrigen(Origen.DINAMICA);

        Hecho h2 = new Hecho("Incendio", "Descripcion 2", "Fuego",
                new Ubicacion(-36.2, -60.1), LocalDateTime.now(), "Manual");
        h2.setOrigen(Origen.DINAMICA);


        System.out.println("Hechos despues de guardar: " + HechoRepositoryEnMemoria.getInstancia().obtenerTodas().size());

        Fuente fuenteDinamica = new Fuente() {
            @Override
            public List<Hecho> obtenerHechos() {
                return HechoRepositoryEnMemoria.getInstancia().obtenerTodas();
            }

            @Override
            public List<Hecho> filtrarHechos(List<CriterioDePertenencia> criterios) {
                return HechoRepositoryEnMemoria.getInstancia().obtenerTodas().stream()
                        .filter(h -> h.filtrarHecho(criterios))
                        .toList();
            }
        };


        Coleccion coleccion = new Coleccion(fuenteDinamica, "Eventos Dinamicos",
                "Ejemplo test fuente dinamica", List.of());
        System.out.println("Hechos después de crear colección: " + coleccion.obtenerHechos().size());

        List<Hecho> hechosDeLaColeccion = coleccion.obtenerHechos();

        assertEquals(2, hechosDeLaColeccion.size());
        assertTrue(hechosDeLaColeccion.stream().anyMatch(h -> h.getTitulo().equals("Inundación")));
        assertTrue(hechosDeLaColeccion.stream().anyMatch(h -> h.getTitulo().equals("Incendio")));

        hechosDeLaColeccion.forEach(h -> System.out.println("Hecho: " + h.getTitulo()));
    }
}
