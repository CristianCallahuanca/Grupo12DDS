package Persona.Contribuyente;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;

import java.time.LocalDateTime;


public class Contribuyente_No_Registrado extends Contribuyente {
    public void subirHechoAnonimo(Hecho hecho) {
        hecho.setVisible(true);
        hecho.setContribuyente(null);
        hecho.setFechaCarga(LocalDateTime.now());
        hecho.setOrigen(Origen.DINAMICA);
    }

}

