package org.example.metamapa.estatico.models.repositorios.implementaciones;

import org.example.metamapa.estatico.models.entidades.ElementoCSV;
import org.example.metamapa.estatico.models.repositorios.AdapterFS;
import org.example.metamapa.estatico.models.repositorios.IRepositoryCSVProcesado;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositoryCSVProcesado implements IRepositoryCSVProcesado {

    private final List<ElementoCSV> csv = new ArrayList<>();

    @Override
    public ElementoCSV csvALeer(AdapterFS fileServer) {
        if (csv.isEmpty()) {
            String archivoCSV = fileServer.obtenerNuevoCSV(null);
            if (archivoCSV == null) return null;
            ElementoCSV nuevoElemento = new ElementoCSV(archivoCSV, 0);
            csv.add(nuevoElemento);
            return nuevoElemento;
        }

        if (csv.stream().allMatch(ElementoCSV::getProcesado)) {
            String archivoCSV = fileServer.obtenerNuevoCSV(
                    csv.stream().map(ElementoCSV::getArchivoCSV).toList());
            if (archivoCSV == null) return null;
            ElementoCSV nuevoElemento = new ElementoCSV(archivoCSV, 0);
            csv.add(nuevoElemento);
            return nuevoElemento;
        }

        return csv.stream().filter(c -> !c.getProcesado()).findFirst().orElse(null);
    }

    @Override
    public void actualizarArchivoCSV(ElementoCSV elementoCSV) {
        for (int i = 0; i < csv.size(); i++) {
            if (csv.get(i).getArchivoCSV().equals(elementoCSV.getArchivoCSV())) {
                csv.set(i, elementoCSV);
                break;
            }
        }
    }
}
