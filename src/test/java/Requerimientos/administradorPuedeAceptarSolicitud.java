package Requerimientos;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Ubicacion;
import Persona.Contribuyente.Contribuyente;
import Persona.Contribuyente.Contribuyente_Registrado;
import SolicitudEliminar.SolicitudEliminar;
import SolicitudEliminar.EstadoEliminar;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class administradorPuedeAceptarSolicitud {

    @Test
    public void administradorPuedeAceptarSolicitudTest() {

        Hecho hecho = new Hecho("Contaminacion", "Descripcion", "Ambiental",
                new Ubicacion(-35.0, -58.0), LocalDateTime.now(), "Manual");
        Contribuyente_Registrado contribuyente = new Contribuyente_Registrado("Juan", "Pérez", 24);

        SolicitudEliminar solicitud = new SolicitudEliminar(hecho, "Es falsa");


        solicitud.aceptar(); // administrador

        assertEquals(EstadoEliminar.APROBADA, solicitud.getEstadoEliminar());
        assertFalse(hecho.getVisible());
    }

    @Test
    public void administradorPuedeRechazarSolicitud() {

        Hecho hecho = new Hecho("Incidente menor", "Descripcion", "Otro",
                new Ubicacion(-35.0, -58.0), LocalDateTime.now(), "Manual");
        Contribuyente_Registrado contribuyente = new Contribuyente_Registrado("Lucía", "Gómez", 28);

        SolicitudEliminar solicitud = new SolicitudEliminar(hecho, "Informacion irrelevante");

        solicitud.rechazar();

        assertEquals(EstadoEliminar.RECHAZADA, solicitud.getEstadoEliminar());
        assertTrue(hecho.getVisible());  // Sigue visible
    }


}
