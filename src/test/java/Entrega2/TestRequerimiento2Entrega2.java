package Entrega2;

import Fuentes.Proxy.AdaptadorDemo;
import Fuentes.Proxy.Conexion;
import Fuentes.Proxy.FuenteDemo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TestRequerimiento2Entrega2 {

    @Test
    public void testSincronizaFuenteDemoSiPasoUnaHora() throws MalformedURLException {
        FuenteDemo fuenteDemo = new FuenteDemo(mockAdaptadorDemo());
        fuenteDemo.setUltimaActualizacion(LocalDateTime.now().minusHours(2)); // simula último sync hace 2 horas

        fuenteDemo.sincronizar();

        Assertions.assertFalse(fuenteDemo.obtenerHechos().isEmpty(), "Debería haber sincronizado hechos nuevos");
    }


    @Test
    public void testNoSincronizaFuenteDemoSiEsReciente() throws MalformedURLException {
        FuenteDemo fuenteDemo = new FuenteDemo(mockAdaptadorDemo());
        fuenteDemo.setUltimaActualizacion(LocalDateTime.now().minusMinutes(30)); // solo 30 minutos atrás

        fuenteDemo.sincronizar();

        Assertions.assertTrue(fuenteDemo.obtenerHechos().isEmpty(), "No debería sincronizar si no pasó 1 hora");
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
                return hechoMock;
            }
        }, new URL("http://demo-fake-source.com"));
    }


}
