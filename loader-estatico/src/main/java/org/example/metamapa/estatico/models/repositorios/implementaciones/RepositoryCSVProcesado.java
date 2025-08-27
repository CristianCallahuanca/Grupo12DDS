package org.example.metamapa.estatico.models.repositorios.implementaciones;

import org.example.metamapa.estatico.models.repositorios.AdapterFS;
import org.example.metamapa.estatico.models.entidades.ElementoCSV;

import java.util.ArrayList;
import java.util.List;

public class RepositoryCSVProcesado {
    private static RepositoryCSVProcesado instance = null;
    private final List<ElementoCSV> csv = new ArrayList<>();

    public RepositoryCSVProcesado() {
    }

    public static RepositoryCSVProcesado getInstancia() {
        if (instance == null) {
            instance = new RepositoryCSVProcesado();
        }
        return instance;
    }

    public ElementoCSV csvALeer(AdapterFS fileServer) {

        if (csv.isEmpty()) {
            String archivoCSV = fileServer.obtenerNuevoCSV(null);
            ElementoCSV nuevoElemento = new ElementoCSV(archivoCSV, 0);
            csv.add(nuevoElemento);
            return nuevoElemento;
        }

        if (csv.stream().allMatch(ElementoCSV::getProcesado)) {
            String archivoCSV = fileServer.obtenerNuevoCSV(csv.stream().map(ElementoCSV::getArchivoCSV).toList());

            if (archivoCSV == null) {
                return null;
            }
            ElementoCSV nuevoElemento = new ElementoCSV(archivoCSV, 0);
            csv.add(nuevoElemento);
            return nuevoElemento;
        }

        return csv.stream().filter(c -> !(c.getProcesado())).findFirst().orElse(null);
    }

    public void actualizarArchivoCSV(ElementoCSV elementoCSV){
        for (int i = 0; i < csv.size(); i++) {
            if (csv.get(i).getArchivoCSV().equals(elementoCSV.getArchivoCSV())) {
                csv.set(i, elementoCSV);
                break;
            }
        }
    }
}
