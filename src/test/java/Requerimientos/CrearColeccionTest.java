package Requerimientos;

import AdministracionDeHechos.Coleccion;
import Fuentes.FuenteEstatica.FuenteEstatica;
import Infraestructura.Repositorios.ColeccionRepositorio;
import AdministracionDeHechos.CriterioPertenencia.PorDescripcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CrearColeccionTest {
/*
    private ColeccionRepositorio repo;
    private FuenteEstatica fuente;
    private Coleccion coleccion;

    @BeforeEach
    public void setUp() {
        repo = ColeccionRepositorio.getInstancia();
        fuente = FuenteEstatica.getInstancia();
        repo.obtenerTodas().clear(); // Limpiamos por si había datos previos
    }

    @Test
    public void administradorPuedeCrearUnaColeccion() throws IOException {

        String titulo = "Colección de Noticias";
        PorDescripcion criterio = new PorDescripcion("crimen");

        coleccion = new Coleccion(fuente, titulo, "colección sobre crímenes", List.of(criterio), datos.getHandle());

        repo.guardar(coleccion);

        Coleccion encontrada = repo.buscarPorTitulo("Colección de Noticias");
        assertNotNull(encontrada);
        assertEquals("Colección de Noticias", encontrada.getTitulo());

        assertEquals("coleccion-de-noticias", encontrada.getHandle());

        assertEquals(1, encontrada.getCriterios().size());
        assertEquals(fuente, encontrada.getFuente());
    }*/
}
/*- no se pueda crear una coleccion con null en los criterios (manejo de errores).

 -se pueda acceder a los hechos de esa colección.

 -el handle sea unico si hay otra con el mismo título.

 -la colección sea visible para un visualizador (en otro test más orientado al uso).*/