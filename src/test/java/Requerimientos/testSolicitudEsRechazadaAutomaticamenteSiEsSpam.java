package Requerimientos;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Ubicacion;
import SolicitudEliminar.DetectorDeSpam;
import SolicitudEliminar.DetectorDeSpamSingleton;
import SolicitudEliminar.SolicitudEliminar;
import SolicitudEliminar.EstadoEliminar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class testSolicitudEsRechazadaAutomaticamenteSiEsSpam {
    @Test
    public void testSolicitudEsRechazadaSiContieneSpam() {
        Hecho hecho = new Hecho("Estafa", "contenido trucho", "Engaño",
                new Ubicacion(-34.5, -58.3), LocalDateTime.now(), "usuario");

        SolicitudEliminar solicitud = new SolicitudEliminar(hecho, "GANA DINERO YA $$$");

        assertEquals(EstadoEliminar.RECHAZADA, solicitud.getEstadoEliminar());
    }


}
