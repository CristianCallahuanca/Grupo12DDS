package org.example.metamapa.agregador.service;

import org.example.metamapa.agregador.models.entidades.Hecho;

import java.util.List;

public interface IDuplicacionService {
    List<Hecho> eliminarHechosRepetidos(List<Hecho> hechosDeLosLoaders);
}
