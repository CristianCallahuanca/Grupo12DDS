package org.example.metamapa.estadisticas.Servicios;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;

public interface IGeneracionCsvService {

    void escribirMayorHechosProvinciaColeccionCsv(PrintWriter writer,
                                                  LocalDate desde,
                                                  LocalDate hasta);

    void escribirCategoriaMasReportadaCsv(OutputStream outputStream,
                                                 LocalDate desde,
                                                 LocalDate hasta)throws IOException;

    public void escribirProvinciaPorCategoriaCsv(OutputStream outputStream,
                                                 LocalDate desde,
                                                 LocalDate hasta) throws IOException;

    public void escribirHoraPorCategoriaCsv(OutputStream outputStream,
                                            LocalDate desde,
                                            LocalDate hasta) throws IOException;

    public void escribirCantidadSolicitudesSpamCsv(OutputStream outputStream,
                                                   LocalDate desde,
                                                   LocalDate hasta) throws IOException;
}
