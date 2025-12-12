package org.example.metamapa.estatico.adapters;

import org.example.metamapa.estatico.models.entidades.HechoCrudo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface IAdapterFileServer {

    byte[] leerArchivo(Path ruta) throws IOException;

    List<HechoCrudo> parsearArchivo(String nombreArchivo, byte[] contenido) throws IOException;
}


