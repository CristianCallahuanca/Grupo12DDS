/*package AdministracionDeHechos;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorDescripcion;
import AdministracionDeHechos.CriterioPertenencia.PorFechaCarga;
import AdministracionDeHechos.CriterioPertenencia.PorUbicacion;
import Fuentes.FuenteDinamica;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.Assert.assertEquals;


public class TestCriteriosPertenencia {
    private Ubicacion ubicacion1;
    private Ubicacion ubicacion2;
    private Hecho hecho1;
    private Hecho hecho2;
    private Hecho hecho3;
    //fa(fechaAcontecimiento) | fc(fechaCarga)
    LocalDateTime fa1 = LocalDateTime.of(2025, 1, 1, 12, 0);
    LocalDateTime fa2 = LocalDateTime.of(2025, 1, 1, 12, 5);
    LocalDateTime fa3 = LocalDateTime.of(2025, 12, 1, 12, 0);
    LocalDateTime fc1 = LocalDateTime.of(2025, 12, 1, 12, 15);
    LocalDateTime fc2 = LocalDateTime.of(2025, 12, 1, 12, 10);
    LocalDateTime fc3 = LocalDateTime.of(2025, 12, 1, 12, 20);
    private FuenteDinamica fuentePrueba ;


    @BeforeEach
    public void setUp() {
        ubicacion1 = new Ubicacion(100, 200);
        ubicacion2 = new Ubicacion(250, 480);

        hecho1 = new Hecho("Incendio en veterinaria",
                "Se produjo un incendio que afectó varias viviendas en la zona, generando gran preocupación entre los vecinos.",
                "Incendios",
                ubicacion1,
                fa1,
                "PRUEBA");
        hecho2 = new Hecho("Corte de luz",
                "Un corte de luz afectó varias viviendas en la zona," +
                        "generando gran preocupación entre los vecinos.",
                "Cortes",
                ubicacion2,
                fa2,
                "PRUEBA");
        hecho3 = new Hecho("Choque de autos en Plaza Central",
                "2 autos chocaron cerca de la plaza",
                "Incendios",
                ubicacion1,
                fa3,
                "trabajo");

        HechoRepositoryEnMemoria.getInstancia().guardar(hecho1);
        HechoRepositoryEnMemoria.getInstancia().guardar(hecho2);
        HechoRepositoryEnMemoria.getInstancia().guardar(hecho3);



    }
    //Cambiar los prints por loggers, deshardcodear el origen en los hechos


    @Test
    public void puedeFiltrarFA(){
        //Test fechas
        PorFechaCarga tiempoCargaPrueba = new PorFechaCarga(fa1, fc1);
        List<CriterioDePertenencia> criterioPrueba = Arrays.asList(tiempoCargaPrueba);

        List<Hecho> hechosFiltrados = fuentePrueba.filtrarHechos(criterioPrueba);
        for (Hecho hecho : hechosFiltrados) {
            assertTrue(tiempoCargaPrueba.cumpleUno((hecho)));
            System.out.println("Fecha Acontecimiento: " + hecho.getFechaAcontecimiento());
            System.out.println("-------------");
        }
    }
    @Test
    public void puedeFiltrarUbicacion(){
        //Test Ubicación
        PorUbicacion criterio = new PorUbicacion(ubicacion1);
        List<CriterioDePertenencia> criterioPrueba = Arrays.asList(criterio);

        List<Hecho> hechosFiltrados = fuentePrueba.filtrarHechos(criterioPrueba);
        for (Hecho hecho : hechosFiltrados) {
            assertTrue(criterio.cumpleUno((hecho)));
            System.out.println("Ubicación: (" + hecho.getUbicacion().getLatitud() + ", " + hecho.getUbicacion().getLongitud() + ")");
            System.out.println("-------------");
        }
    }
    @Test
    public void puedeFiltrarStrings(){
        //Test Descripcion
        PorDescripcion criterioDescripcion = new PorDescripcion("generando gran preocupación entre los vecinos");
        List<CriterioDePertenencia> criterioPrueba = Arrays.asList(criterioDescripcion);

        List<Hecho> hechosFiltrados = fuentePrueba.filtrarHechos(criterioPrueba);
        for (Hecho hecho : hechosFiltrados) {
            assertTrue(hecho.getDescripcion().toLowerCase().contains("generando gran preocupación entre los vecinos"));
            System.out.println("Descripción: " + hecho.getDescripcion());
            System.out.println("-------------");
        }
    }
}*/