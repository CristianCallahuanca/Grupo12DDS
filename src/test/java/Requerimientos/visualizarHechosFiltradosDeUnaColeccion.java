package Requerimientos;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorCategoria;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import AdministracionDeHechos.Ubicacion;
import Fuentes.Fuente;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;


public class visualizarHechosFiltradosDeUnaColeccion  {
    @Test
    public void testVisualizacion() throws IOException {
        // Limpiar estado TEST
        HechoRepositoryEnMemoria.getInstancia().limpiar();

        // Crear hechos
        Hecho h1 = new Hecho("Inundación", "Desc", "Clima",
                new Ubicacion(-34.5, -58.4), LocalDateTime.now(), "Manual");
        h1.setOrigen(Origen.DINAMICA);

        Hecho h2 = new Hecho("Incendio", "Desc", "Fuego",
                new Ubicacion(-36.2, -60.1), LocalDateTime.now(), "Manual");
        h2.setOrigen(Origen.DINAMICA);

        Hecho h3 = new Hecho("Granizo", "Desc", "Clima",
                new Ubicacion(-35.1, -58.7), LocalDateTime.now(), "Manual");
        h3.setOrigen(Origen.DINAMICA);


        Fuente fuente = new Fuente() {
            @Override
            public List<Hecho> obtenerHechos() {
                return HechoRepositoryEnMemoria.getInstancia().obtenerTodas();
            }

            @Override
            public List<Hecho> filtrarHechos(List<CriterioDePertenencia> criterios) {
                return obtenerHechos().stream()
                        .filter(h -> h.filtrarHecho(criterios))
                        .toList();
            }
        };

        Coleccion coleccion = new Coleccion(fuente, "Eventos Naturales", "Hechos varios", List.of());

        CriterioDePertenencia criterioClima = new PorCategoria("Clima");

        List<Hecho> filtrados = coleccion.filtrarHechos(List.of(criterioClima));

        assertEquals(2, filtrados.size());
        assertTrue(filtrados.stream().allMatch(h -> h.getCategoria().equals("Clima")));
        assertFalse(filtrados.stream().anyMatch(h -> h.getCategoria().equals("Fuego")));

        filtrados.forEach(h -> System.out.println("Hecho filtrado: " + h.getTitulo()));
    }
}
