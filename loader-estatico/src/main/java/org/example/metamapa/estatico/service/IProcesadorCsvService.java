package org.example.metamapa.estatico.service;

import org.example.metamapa.estatico.models.entidades.FuenteEstatica;

import java.io.IOException;

public interface IProcesadorCsvService {
    void procesarFuentesPendientes();
    void procesarFuente(FuenteEstatica fuente) throws IOException;
    void procesarFuenteDesdeBytes(FuenteEstatica fuente, byte[] contenido, String nombreArchivo) throws IOException;
}

