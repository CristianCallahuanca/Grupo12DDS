package org.example.metamapa.estadisticas.Servicios;

import java.io.PrintWriter;
import java.time.LocalDate;

public interface IGeneracionCsvService {

    void escribirMayorHechosProvinciaColeccionCsv(PrintWriter writer,
                                                  LocalDate desde,
                                                  LocalDate hasta);

    void escribirCategoriaMasReportadaCsv(PrintWriter writer,
                                          LocalDate desde,
                                          LocalDate hasta);

    void escribirProvinciaPorCategoriaCsv(PrintWriter writer,
                                          LocalDate desde,
                                          LocalDate hasta);

    void escribirHoraPorCategoriaCsv(PrintWriter writer,
                                     LocalDate desde,
                                     LocalDate hasta);

    void escribirCantidadSolicitudesSpamCsv(PrintWriter writer,
                                            LocalDate desde,
                                            LocalDate hasta);
}
