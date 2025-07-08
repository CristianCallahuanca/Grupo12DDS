package Requerimientos;

public class FuenteMetaMapaSincronizaEnTiempoReal {
/*
    @Test
    public void testSincronizaFuenteMetaMapaYObtieneHechos() throws IOException {
        FuenteMetaMapa fuenteMetaMapa = new FuenteMetaMapa(mockAdaptadorMetaMapa());

        fuenteMetaMapa.sincronizar();

        List<Hecho> hechos = fuenteMetaMapa.obtenerHechos();

        Assertions.assertEquals(2, hechos.size(), "Debe haber traído los hechos actuales en tiempo real");
    }

    private MetaMapaAdaptada mockAdaptadorMetaMapa() {
        return new MetaMapaAdaptada("http://localhost:7000") {
            @Override
            public List<Hecho> obtenerHechosExternos(Map<String, String> filtros) {
                List<Hecho> hechos = new ArrayList<>();
                hechos.add(new Hecho(
                        "Robo en San Telmo",
                        "Arrebato en vía publica",
                        "Robo",
                        new Ubicacion(-34.62, -58.37),
                        LocalDateTime.now().minusHours(1),
                        "MetaMapa"
                ));

                hechos.add(new Hecho(
                        "Corte de luz en Recoleta",
                        "Falla en el sistema electrico",
                        "Corte",
                        new Ubicacion(-34.59, -58.39),
                        LocalDateTime.now().minusHours(2),
                        "MetaMapa"
                ));
                return hechos;

            }
        };
    }

*/
}

/*Probar SERVIDOR - CLIENTE para una prueba integral correcta*/