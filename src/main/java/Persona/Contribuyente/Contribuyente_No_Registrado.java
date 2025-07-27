package Persona.Contribuyente;

import AdministracionDeHechos.EstadoEdicionHecho;
import AdministracionDeHechos.EstadoHecho;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;

import java.time.LocalDateTime;

public class Contribuyente_No_Registrado extends Contribuyente {
    public void cargarHechoAnonimo(Hecho hecho) {
            hecho.setEstadoHecho(EstadoHecho.EN_REVISION);
            hecho.setContribuyente(null);
            hecho.setFechaCarga(LocalDateTime.now());
            hecho.setOrigen(Origen.DINAMICA);
    }

}

