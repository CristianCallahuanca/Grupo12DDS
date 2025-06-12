package Requerimientos;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import Fuentes.FuenteEstatica.Dataset;
import Fuentes.FuenteEstatica.FuenteEstatica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ImportarHechosDesdeCSVTest {

    private FuenteEstatica fuente;

    @BeforeEach
    public void setUp() {
        fuente = FuenteEstatica.getInstancia();
        fuente.getListaDeDatasets().clear(); // Limpiar datos previeos
    }

    @Test
    public void importarHechosDesdeCSV() throws IOException {
        // Arrange
        String pathCSV = "datos/desastres_naturales_argentina.csv";

        Dataset dataset = new Dataset(pathCSV);


        List<Hecho> hechos = fuente.procesarHechosDesdeCSV();

        assertNotNull(hechos);
        assertFalse(hechos.isEmpty(), "No se importaron hechos desde el CSV");

        Hecho primerHecho = hechos.get(0);
        assertNotNull(primerHecho.getTitulo());
        assertNotNull(primerHecho.getDescripcion());
        assertNotNull(primerHecho.getUbicacion());
        assertNotNull(primerHecho.getOrigen());
        assertNotNull(primerHecho.getFechaAcontecimiento());

        assertEquals(Origen.ESTATICA, primerHecho.getOrigen());


    }
}

/*Verificar si los hechos tambien se guardan automaticamente en HechoRepositoryEnMemoria

Probar que pasa con un archivo que no llega a 10.000 lineas

Testear errores: archivos inexistentes, campos mal formateados, fechas invalidas.*/
