package Persona;
import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.*;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Ubicacion;
import Fuentes.FuenteDinamica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAgregarHechoFuenteDinamica {

    private Ubicacion ubicacion1;
    private Hecho hecho1;
    private Origen.DINAMICA
    //fa(fechaAcontecimiento) | fc(fechaCarga)
    LocalDateTime fa1 = LocalDateTime.of(2025, 1, 1, 12, 0);
    LocalDateTime fc1 = LocalDateTime.of(2025, 12, 1, 12, 15);


    private FuenteDinamica fuentePrueba;
    private Coleccion coleccionPrueba;
    private PorFechaCarga criterioTiempoCarga;
    private PorDescripcion criterioDescripcion;
    private List<CriterioDePertenencia> criterioPrueba;
    private ArrayList<Coleccion> todasLasColecciones;

     @BeforeEach
    public void setUp() {
        ubicacion1 = new Ubicacion(100, 200);

        hecho1 = new Hecho("Incendio en veterinaria",
                "Se produjo un incendio que afectó varias viviendas en la zona, generando gran preocupación entre los vecinos.",
                "Incendios",
                ubicacion1,
                fa1,
                fc1,
                "PRUEBA",
                "Sin multimedia",
                origen,

                );

        fuentePrueba = new FuenteDinamica();
        fuentePrueba.agregarHecho(hecho1);

        /*criterioTiempoCarga = new PorFechaCarga(fa1, fc1);
        criterioDescripcion = new PorDescripcion("generando gran preocupación entre los vecinos");
        criterioPrueba = Arrays.asList(criterioTiempoCarga, criterioDescripcion);

        coleccionPrueba = new Coleccion(fuentePrueba, "Coleccion de prueba", "", criterioPrueba, "1");
        */

     }


         @Test
    public void agregarHechoFuenteDinamica() throws IOException{
        fuentePrueba.agregarHecho(hecho1);

       assertTrue(fuentePrueba.filtrarHechos(List.of()).contains(hecho1));
    }
}
