package org.example.metamapa.estatico.adapters;

import org.example.metamapa.estatico.models.dtos.ArchivoCsv;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;

import java.io.IOException;
import java.util.List;

public interface IAdapterFileServer {
    // Devuelve la lista de archivos CSV disponibles (solo metadata)
    List<ArchivoCsv> obtenerArchivosDisponibles();

    // Lee un archivo completo desde el File Server como array de bytes
    byte[] descargarArchivo(String nombreArchivo) throws IOException;

    // Lee y parsea un CSV desde bytes en memoria convierte a HechoCrudo
    List<HechoCrudo> leerArchivoDesdeBytes(byte[] contenido) throws IOException;

}

