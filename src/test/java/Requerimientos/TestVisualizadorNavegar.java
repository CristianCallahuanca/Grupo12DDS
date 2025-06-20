/*package Persona;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorDescripcion;
import AdministracionDeHechos.CriterioPertenencia.PorFechaCarga;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Ubicacion;
import AdministracionDeHechos.Coleccion;
import Fuentes.FuenteDinamica;
import Fuentes.Fuente;
import Persona.Contribuyente.Contribuyente;
import Persona.Contribuyente.Contribuyente_No_Registrado;
import Persona.Contribuyente.Contribuyente_Registrado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestVisualizadorNavegar {

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
        }

    @Test
    public void navegarColeccion() throws IOException{
        coleccionPrueba.navegarHechos();

        List<Hecho> hechosColeccion = coleccionPrueba.obtenerHechos();

        assertEquals(2, hechosColeccion.size());
        assertTrue(hechosColeccion.contains(hecho1));
        assertTrue(hechosColeccion.contains(hecho2));
    }




}*/
