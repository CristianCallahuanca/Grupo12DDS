/*package SolicitudEliminar;
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

import static org.junit.Assert.*;

public class TestAdministradorRevisar {
    private Ubicacion ubicacion1;
    private Hecho hecho1;
    private SolicitudEliminar solicitud;

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
                "PRUEBA");

        solicitud = new SolicitudEliminar(hecho1, "Violento");
    }

    @Test
    @DisplayName("Un Administrador puede Aceptar una solicitud de eliminacion")
    public void puedeAceptarUnaSolicitud(){
        solicitud.aceptar();
        assertEquals(EstadoEliminar.APROBADA, solicitud.getEstadoEliminar());
        assertEquals(false, hecho1.getVisible());
    }
    @Test
    @DisplayName("Un Administrador puede Rechazar una solicitud de eliminacion")
    public void puedeRechazarUnaSolicitud(){
        solicitud.rechazar();
        assertEquals(EstadoEliminar.RECHAZADA, solicitud.getEstadoEliminar());
        assertTrue(hecho1.getVisible());
    }


}*/
