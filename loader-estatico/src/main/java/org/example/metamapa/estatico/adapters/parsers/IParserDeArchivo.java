package org.example.metamapa.estatico.adapters.parsers;

import org.example.metamapa.estatico.models.entidades.HechoCrudo;

import java.io.IOException;
import java.util.List;

public interface IParserDeArchivo {

    List<HechoCrudo> parse(byte[] contenido) throws IOException;
}

/**
 * En caso de que los hechos vengan en distinto formato (.csv, .xls, etc.).
 */
