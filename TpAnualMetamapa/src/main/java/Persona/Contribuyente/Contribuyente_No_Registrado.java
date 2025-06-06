package Persona.Contribuyente;

import AdministracionDeHechos.Hecho;


public class Contribuyente_No_Registrado extends Contribuyente {

    public void subirHechoAnonimo(Hecho hecho) {
        //hecho.setVisible(true);
        //hecho.setContribuyente(this);
        listaDeHechos.add(hecho);
    }
}

