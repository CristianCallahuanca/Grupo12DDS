package Requerimientos;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import AdministracionDeHechos.Ubicacion;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;
import Infraestructura.Repositorios.SolicitudRepositoryEnMemoria;
import Persona.Contribuyente.Contribuyente_Registrado;
import SolicitudEliminar.SolicitudEliminar;
import org.junit.jupiter.api.Test;
import SolicitudEliminar.EstadoEliminar;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class contribuyentePuedeSolicitarEliminacionDeHecho {

    @Test
    public void contribuyentePuedeSolicitarEliminacionDeHechoTest() {

        Hecho hecho = new Hecho("Granizo extremo", "Descripción", "Clima",
                new Ubicacion(-34.5, -58.4), LocalDateTime.now(), "Manual");
        hecho.setOrigen(Origen.DINAMICA);


        Contribuyente_Registrado usuario = new Contribuyente_Registrado("Juan", "ramos", 24);

        String motivo = "Informacion incorrecta";
        usuario.solicitarEliminarHecho(hecho, motivo);


        List<SolicitudEliminar> solicitudes = SolicitudRepositoryEnMemoria.getInstancia().obtenerTodas();
        assertEquals(1, solicitudes.size());

        SolicitudEliminar solicitud = solicitudes.get(0);
        assertEquals(hecho, solicitud.getHecho());
        assertEquals(motivo, solicitud.getJustificacion());
        assertEquals(EstadoEliminar.PENDIENTE, solicitud.getEstadoEliminar());

    }

}
