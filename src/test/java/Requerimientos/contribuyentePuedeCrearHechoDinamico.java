package Requerimientos;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import AdministracionDeHechos.Ubicacion;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;
import Persona.Contribuyente.Contribuyente;
import Persona.Contribuyente.Contribuyente_No_Registrado;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class contribuyentePuedeCrearHechoDinamico {

    @Test
    public void contribuyentePuedeCrearHechoDinamicoTest() {

        HechoRepositoryEnMemoria.getInstancia().limpiar();

        Contribuyente_No_Registrado contribuyente = new Contribuyente_No_Registrado();

        Hecho hechoCreado = new Hecho("Rotura de caño", "Perdida de agua importante", "Infraestructura",
                new Ubicacion(-34.6, -58.3), LocalDateTime.now(), "Laura");

        hechoCreado.setOrigen(Origen.DINAMICA);  // Esto también guarda el hecho automáticamente

        List<Hecho> hechosEnRepo = HechoRepositoryEnMemoria.getInstancia().obtenerTodas();

        assertEquals(1, hechosEnRepo.size());
        assertEquals("Rotura de caño", hechosEnRepo.get(0).getTitulo());
        assertEquals(Origen.DINAMICA, hechosEnRepo.get(0).getOrigen());
        assertTrue(hechosEnRepo.get(0).getVisible());
    }

}
