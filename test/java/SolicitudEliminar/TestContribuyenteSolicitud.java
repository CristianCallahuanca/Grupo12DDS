package SolicitudEliminar;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Ubicacion;
import Fuentes.FuenteDinamica;
import Persona.Contribuyente.Contribuyente;
import Persona.Contribuyente.Contribuyente_No_Registrado;
import Persona.Contribuyente.Contribuyente_Registrado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class TestContribuyenteSolicitud {
    private Ubicacion ubicacion1;
    private Hecho hecho1;
    private Contribuyente_No_Registrado contriNoRegistrado;
    private Contribuyente_Registrado contriRegistrado;

    //fa(fechaAcontecimiento) | fc(fechaCarga)
    LocalDateTime fa1 = LocalDateTime.of(2025, 1, 1, 12, 0);
    LocalDateTime fc1 = LocalDateTime.of(2025, 1, 1, 12, 5);


    @BeforeEach
    public void setUp() {
        ubicacion1 = new Ubicacion(100, 200);

        hecho1 = new Hecho("Incendio en veterinaria",
                "Se produjo un incendio que afectó varias viviendas en la zona, generando gran preocupación entre los vecinos.",
                "Incendios",
                ubicacion1,
                fa1,
                fc1,
                "PRUEBA");

        contriNoRegistrado = new Contribuyente_No_Registrado();
        contriRegistrado = new Contribuyente_Registrado("mariano","luna", 17);
    }

    @Test
    @DisplayName("Un Contribuyente puede hacer una solicitud de eliminacion")
    public void puedeSolicitarEliminacionHecho(){
        SolicitudEliminar solicitud = new SolicitudEliminar(hecho1, "Violento");
        assertEquals(EstadoEliminar.PENDIENTE, solicitud.getEstadoEliminar());
    }


}
