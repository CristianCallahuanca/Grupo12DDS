package Requerimientos;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorFechaCarga;
import AdministracionDeHechos.Hecho;
import Fuentes.Proxy.AdaptadorDemo;
import Fuentes.Proxy.Conexion;
import Fuentes.Proxy.FuenteDemo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class coleccionConFuenteDemoYFiltroDeAntiguedadMaxima {

    @Test
    public void coleccionConFuenteDemoYFiltroDeAntiguedadMaxima() throws MalformedURLException, IOException {
        // Arrange
        FuenteDemo fuenteDemo = new FuenteDemo(mockAdaptadorDemo());
        fuenteDemo.setUltimaActualizacion(LocalDateTime.now().minusHours(2)); // ultima sync hace 2 horas
        fuenteDemo.sincronizar(); // forzamos

        CriterioDePertenencia criterioAntiguedad = new PorFechaCarga(LocalDateTime.now().minusHours(1), LocalDateTime.now()); // 1 hora

        Coleccion coleccion = new Coleccion(fuenteDemo, "Hechos recientes demo",
                "Coleccion con hechos de fuente demo con antigüedad máxima 1 hora",
                List.of(criterioAntiguedad)
        );


        List<Hecho> hechosFiltrados = coleccion.obtenerHechos();

        assertEquals(1, hechosFiltrados.size());
        assertEquals("Incendio en Córdoba", hechosFiltrados.get(0).getTitulo());
    }

    @Test
    public void testFuenteDemoSincronizaSoloSiPasoUnaHora() throws Exception {
        // Caso A
        FuenteDemo fuenteCon2Horas = mockFuenteDemo(LocalDateTime.now().minusHours(2), true);
        fuenteCon2Horas.sincronizar();

        Coleccion coleccionConHechos = new Coleccion(fuenteCon2Horas, "DemoConHechos", "Debería tener hechos", List.of());
        List<Hecho> hechosCasoA = coleccionConHechos.obtenerHechos();

        Assertions.assertFalse(hechosCasoA.isEmpty(), "Deberia haber sincronizado hechos (paso mas de 1 hora)");
        System.out.println("Caso A - Hechos sincronizados: " + hechosCasoA.size());

        // Caso B
        FuenteDemo fuenteCon30Min = mockFuenteDemo(LocalDateTime.now().minusMinutes(30), true);
        fuenteCon30Min.sincronizar();

        Coleccion coleccionSinHechos = new Coleccion(fuenteCon30Min, "DemoSinHechos", "No debería tener hechos", List.of());
        List<Hecho> hechosCasoB = coleccionSinHechos.obtenerHechos();

        Assertions.assertTrue(hechosCasoB.isEmpty(), "No debería sincronizar (no paso 1 hora)");
        System.out.println("Caso B - Hechos NO sincronizados correctamente");
    }


    private AdaptadorDemo mockAdaptadorDemo() throws MalformedURLException {
        return new AdaptadorDemo(new Conexion() {
            private int llamados = 0;

            @Override
            public Map<String, Object> siguienteHecho(URL url, LocalDateTime ultimaConsulta) {
                if (llamados++ >= 1) return null;

                Map<String, Object> hechoMock = new HashMap<>();
                hechoMock.put("titulo", "Incendio en Córdoba");
                hechoMock.put("descripcion", "Fuego en las sierras");
                hechoMock.put("categoria", "Incendio");
                hechoMock.put("latitud", -31.4);
                hechoMock.put("longitud", -64.2);
                hechoMock.put("fechaDeCarga", LocalDateTime.now());
                return hechoMock;
            }
        }, new URL("http://demo.com"));
    }

    private FuenteDemo mockFuenteDemo(LocalDateTime ultimaActualizacion, boolean debeResponderHechos) throws MalformedURLException {
        return new FuenteDemo(new AdaptadorDemo(new Conexion() {
            private boolean respondido = false;

            @Override
            public Map<String, Object> siguienteHecho(URL url, LocalDateTime ultimaConsulta) {
                if (respondido || !debeResponderHechos) return null;
                respondido = true;

                Map<String, Object> hechoMock = new HashMap<>();
                hechoMock.put("titulo", "Mock Incendio");
                hechoMock.put("descripcion", "Fuego en cerros");
                hechoMock.put("categoria", "Fuego");
                hechoMock.put("latitud", -32.4);
                hechoMock.put("longitud", -63.2);
                return hechoMock;
            }
        }, new URL("http://demo.com"))) {{
            setUltimaActualizacion(ultimaActualizacion);
        }};
    }

}
