package Persona;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorDescripcion;
import AdministracionDeHechos.CriterioPertenencia.PorFechaCarga;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.TestCriteriosPertenencia;
import AdministracionDeHechos.Ubicacion;
import Fuentes.Fuente;
import Fuentes.FuenteDinamica;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestAdministradorColeccion{
    private Ubicacion ubicacion1;
    private Ubicacion ubicacion2;
    private Hecho hecho1;
    private Hecho hecho2;
    private Hecho hecho3;
    //fa(fechaAcontecimiento) | fc(fechaCarga)
    LocalDateTime fa1 = LocalDateTime.of(2025, 1, 1, 12, 0);
    LocalDateTime fa2 = LocalDateTime.of(2025, 1, 1, 12, 0);
    LocalDateTime fa3 = LocalDateTime.of(2025, 12, 1, 12, 0);
    LocalDateTime fc1 = LocalDateTime.of(2025, 12, 1, 12, 15);
    LocalDateTime fc2 = LocalDateTime.of(2025, 12, 1, 12, 15);
    LocalDateTime fc3 = LocalDateTime.of(2025, 12, 1, 12, 20);
    private FuenteDinamica fuentePrueba;
    private Coleccion coleccionPrueba;
    private PorFechaCarga criterioTiempoCarga;
    private PorDescripcion criterioDescripcion;
    private List<CriterioDePertenencia> criterioPrueba;
    private ArrayList<Coleccion> todasLasColecciones;


    @BeforeEach
    public void setUp() {
        ubicacion1 = new Ubicacion(100, 200);
        ubicacion2 = new Ubicacion(250, 480);

        hecho1 = new Hecho("Incendio en veterinaria",
                "Se produjo un incendio que afectó varias viviendas en la zona, generando gran preocupación entre los vecinos.",
                "Incendios",
                ubicacion1,
                fa1,
                fc1,
                "PRUEBA");
        hecho2 = new Hecho("Corte de luz",
                "Un corte de luz afectó varias viviendas en la zona," +
                        "generando gran preocupación entre los vecinos.",
                "Cortes",
                ubicacion2,
                fa2,
                fc2,
                "PRUEBA");
        hecho3 = new Hecho("Choque de autos en Plaza Central",
                "2 autos chocaron cerca de la plaza",
                "Incendios",
                ubicacion1,
                fa3,
                fc3,
                "trabajo");

        fuentePrueba = new FuenteDinamica();
        fuentePrueba.agregarHecho(hecho1);
        fuentePrueba.agregarHecho(hecho2);
        fuentePrueba.agregarHecho(hecho3);

        criterioTiempoCarga = new PorFechaCarga(fa1, fc1);
        criterioDescripcion = new PorDescripcion("generando gran preocupación entre los vecinos");
        criterioPrueba = Arrays.asList(criterioTiempoCarga, criterioDescripcion);

        coleccionPrueba = new Coleccion(fuentePrueba, "Coleccion de prueba", "", criterioPrueba, "1");

        ColeccionRepositoryEnMemoria repositorio = ColeccionRepositoryEnMemoria.getInstancia();
        todasLasColecciones = repositorio.obtenerTodas();
    }

    @Test
    public void puedeCrearUnaColecciony() throws IOException {
        assertEquals("Coleccion de prueba", coleccionPrueba.getTitulo());
        assertEquals("", coleccionPrueba.getDescripcion());

    }

    @Test // Me fijo si la coleccion se carga automaticamente al ColeccionRepository
    public void elColeccionRepositoryFunciona() throws IOException {
        assertEquals(1, todasLasColecciones.size());
    }

}
