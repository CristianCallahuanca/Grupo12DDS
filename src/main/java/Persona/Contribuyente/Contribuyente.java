package Persona.Contribuyente;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import Persona.UsuarioPublico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Contribuyente extends UsuarioPublico {

    protected List<Hecho> listaDeHechos = new ArrayList<>();

    // En las listas solo se puede hacer un get de la clase no es necesario un set, eso es labor interno
    public List<Hecho> getMisHechos() {
        return listaDeHechos;
    }

    public void agregarAListaDeHechos(Hecho unHecho) {
        listaDeHechos.add(unHecho);
    }
}
