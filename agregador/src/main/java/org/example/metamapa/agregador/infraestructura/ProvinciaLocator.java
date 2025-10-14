package org.example.metamapa.agregador.infraestructura;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.*;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Slf4j
public class ProvinciaLocator {

    private static final GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
    private static final Map<String, Geometry> provincias = new ConcurrentHashMap<>();

    static {
        try {
            // Cargar el shapefile desde resources
            URL shpUrl = ProvinciaLocator.class.getResource("/geo/ign_provincia.shp");
            if (shpUrl == null) {
                throw new IllegalStateException("No se encontró el archivo ign_provincia.shp en resources/geo");
            }

            FileDataStore store = FileDataStoreFinder.getDataStore(new File(shpUrl.toURI()));
            SimpleFeatureSource featureSource = store.getFeatureSource();

            try (SimpleFeatureIterator it = featureSource.getFeatures().features()) {
                while (it.hasNext()) {
                    SimpleFeature feature = it.next();
                    Geometry geom = (Geometry) feature.getDefaultGeometry();
                    // El campo puede variar, usualmente "NAM" o "PROVINCIA" o "NOMBRE"
                    String nombre = (String) feature.getAttribute("NAM");
                    if (nombre == null)
                        nombre = (String) feature.getAttribute("PROVINCIA");
                    if (nombre == null)
                        nombre = (String) feature.getAttribute("NOMBRE");
                    if (nombre != null) {
                        provincias.put(nombre.toUpperCase(), geom);
                    }
                }
            }

            log.info("Cargadas {} provincias desde shapefile IGN.", provincias.size());
        } catch (Exception e) {
            log.error("Error cargando shapefile de provincias: {}", e.getMessage(), e);
        }
    }

    /**
     * Retorna el nombre de la provincia correspondiente a las coordenadas.
     */
    public static String obtenerProvincia(double lat, double lon) {
        Point punto = geometryFactory.createPoint(new Coordinate(lon, lat));
        for (Map.Entry<String, Geometry> entry : provincias.entrySet()) {
            if (entry.getValue().contains(punto)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
