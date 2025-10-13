package org.example.metamapa.estatico.adapters.parsers.implementaciones;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estatico.adapters.parsers.IParserDeArchivo;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ExcelFileParser implements IParserDeArchivo {

    @Override
    public List<HechoCrudo> parse(String nombreArchivo, byte[] contenido) throws IOException {
        log.warn("Parser Excel aún no implementado. Se omite archivo.");
        return List.of();
    }
}
