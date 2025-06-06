package Persona.Contribuyente;

import AdministracionDeHechos.Hecho;
import Persona.UsuarioPublico;

import java.util.ArrayList;
import java.util.List;

public abstract class Contribuyente extends UsuarioPublico {

    protected List<Hecho> listaDeHechos = new ArrayList<>();

    public List<Hecho> getMisHechos() {
        return listaDeHechos;
    }
}
