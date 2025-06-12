package SolicitudEliminar;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Ubicacion;
import Handlers.PostSolicitudesHandler;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;
import Infraestructura.Repositorios.SolicitudRepositoryEnMemoria;
import io.javalin.Javalin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestPostearSolicitud {
    /* se testea que se crea una solicitud de eliminacion y que es guardada en el repository" */

    @BeforeAll
    static void startServer() {
        Javalin app = Javalin.create().start(7000);

        app.post("/solicitudes",new PostSolicitudesHandler());
    }

    LocalDateTime fa1 = LocalDateTime.of(2025, 1, 1, 12, 0);
    LocalDateTime fc1 = LocalDateTime.of(2025, 1, 1, 12, 5);
    private Ubicacion ubicacion1;
    private Hecho hecho1;


    @Test
    public void testPostSolicitud() throws Exception {
        String json = """
        {
            "titulo": "incendio",
            "justificacion": "motivo válido"
        }
    """;

        ubicacion1 = new Ubicacion(100, 200);

        hecho1 = new Hecho("incendio",
                "Se produjo un incendio que afectó varias viviendas en la zona, generando gran preocupación entre los vecinos.",
                "Incendios",
                ubicacion1,
                fa1,
                "PRUEBA");

        HechoRepositoryEnMemoria.getInstancia().guardar(hecho1);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:7000/solicitudes"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<SolicitudEliminar> solicitudes = SolicitudRepositoryEnMemoria.getInstancia().obtenerTodas();

        assertEquals(1, solicitudes.size());

        assertEquals(200, response.statusCode());
    }
}
