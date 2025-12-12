package org.example.metamapa.estatico.adapters.implementaciones;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estatico.adapters.IAdapterFileServer;
import org.example.metamapa.estatico.adapters.parsers.IParserDeArchivo;
import org.example.metamapa.estatico.adapters.parsers.ParserFactory;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
@Slf4j
public class AdapterFileServerLocal implements IAdapterFileServer {

    private final ParserFactory parserFactory;

    public AdapterFileServerLocal(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }

    @Override
    public byte[] leerArchivo(Path ruta) throws IOException {
        return Files.readAllBytes(ruta);
    }

    @Override
    public List<HechoCrudo> parsearArchivo(String nombreArchivo, byte[] contenido) throws IOException {
        IParserDeArchivo parser = parserFactory.obtenerParser(nombreArchivo);
        return parser.parse(nombreArchivo, contenido);
    }
}
