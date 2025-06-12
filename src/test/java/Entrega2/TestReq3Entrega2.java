package Entrega2;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Ubicacion;
import Fuentes.Proxy.AdaptadorMetaMapa;
import Fuentes.Proxy.FuenteMetaMapa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestReq3Entrega2 {

    @Test
    public void testSincronizaFuenteMetaMapaYObtieneHechos() {
        FuenteMetaMapa fuenteMetaMapa = new FuenteMetaMapa(mockAdaptadorMetaMapa());

        fuenteMetaMapa.sincronizar();

        List<Hecho> hechos = fuenteMetaMapa.obtenerHechos();

        Assertions.assertEquals(2, hechos.size(), "Debe haber traído los hechos actuales en tiempo real");
    }

    private AdaptadorMetaMapa mockAdaptadorMetaMapa() {
        return new AdaptadorMetaMapa("http://localhost:7000") {
            @Override
            public List<Hecho> obtenerHechosExternos(Map<String, String> filtros) {
                List<Hecho> hechos = new ArrayList<>();
                hechos.add(new Hecho(
                        "Robo en San Telmo",
                        "Arrebato en vía pública",
                        "Robo",
                        new Ubicacion(-34.62, -58.37),
                        LocalDateTime.now().minusHours(1),
                        "MetaMapa"
                ));

                hechos.add(new Hecho(
                        "Corte de luz en Recoleta",
                        "Falla en el sistema eléctrico",
                        "Corte",
                        new Ubicacion(-34.59, -58.39),
                        LocalDateTime.now().minusHours(2),
                        "MetaMapa"
                ));
                return hechos;

            }
        };
    }


}
