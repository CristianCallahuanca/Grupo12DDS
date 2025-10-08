package org.example.metamapa.estatico.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;

@Data
@AllArgsConstructor
public class ArchivoCsv {
    private final String nombre;
    private final InputStream contenido;

    public byte[] leerComoBytes() throws IOException {
        return contenido.readAllBytes();
    }
}


