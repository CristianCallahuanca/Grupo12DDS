package org.example.metamapa.estatico.service.implementaciones;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estatico.models.dtos.HechoCrudoDTO;
import org.example.metamapa.estatico.models.entidades.ElementoCSV;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;
import org.example.metamapa.estatico.models.repositorios.AdapterFS;
import org.example.metamapa.estatico.models.repositorios.IRepositoryCSVProcesado;
import org.example.metamapa.estatico.service.IRecopiladorHechos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RecopiladorHechosService implements IRecopiladorHechos {

    private final AdapterFS fileServer;
    private final IRepositoryCSVProcesado repositorio;

    @Autowired
    public RecopiladorHechosService(AdapterFS fileServer, IRepositoryCSVProcesado repositorio) {
        this.fileServer = fileServer;
        this.repositorio = repositorio;
    }

    //TO DO: comparar cantidadFilasALeer <= ElementoCSV.ultimaFilaLeida

    @Override
    public List<HechoCrudoDTO> obtenerHechosCrudos(int cantidad) throws IOException {
        ElementoCSV archivo = repositorio.csvALeer(fileServer);

        if (archivo == null) return List.of(); // No hay archivos nuevos

        List<HechoCrudo> hechos = leerCSVPorArchivo(archivo, cantidad);
        repositorio.actualizarArchivoCSV(archivo);

        // Transformamos los HechoCrudo a DTO
        List<HechoCrudoDTO> resultado = new ArrayList<>();
        for (HechoCrudo h : hechos) {
            resultado.add(new HechoCrudoDTO(h));
        }
        return resultado;
    }

    private List<HechoCrudo> leerCSVPorArchivo(ElementoCSV csvAProcesar, int filasAProcesar) throws IOException {
        List<HechoCrudo> hechosCrudos = new ArrayList<>();

        log.info("Procesando archivo: {}", csvAProcesar.getArchivoCSV());
        log.info("Última fila leída: {}", csvAProcesar.getUltimaFilaLeida());


        try(CSVReader csvReader = new CSVReader(new FileReader(csvAProcesar.getArchivoCSV()))) {
            String[] parts;
            int filaActual = 0;
            int ultimaFilaLeida = csvAProcesar.getUltimaFilaLeida();

            // Saltar hasta la ultima fila leída previamente
            while (filaActual < ultimaFilaLeida && (parts = csvReader.readNext()) != null) {
                filaActual++;
            }

            // Si es la primera lectura del archivo, saltear la cabecera
            if (ultimaFilaLeida == 0) {
                csvReader.readNext(); // salta header
            }

            // Leer nuevas filas
            int filasProcesadas = 0;
            while (filasProcesadas < filasAProcesar && (parts = csvReader.readNext()) != null) {
                HechoCrudo hecho = new HechoCrudo(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                hechosCrudos.add(hecho);

                csvAProcesar.actualizarUltimaFilaLeida();
                filasProcesadas++;
            }

            // Marcar como procesado si llego al final del archivo
            if ((parts = csvReader.readNext()) == null) {
                csvAProcesar.setProcesado(true);
            }

        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return hechosCrudos;
    }

}
