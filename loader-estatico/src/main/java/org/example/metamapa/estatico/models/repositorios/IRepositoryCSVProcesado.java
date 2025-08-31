package org.example.metamapa.estatico.models.repositorios;

import org.example.metamapa.estatico.models.entidades.ElementoCSV;

public interface IRepositoryCSVProcesado {
    ElementoCSV csvALeer(AdapterFS fileServer);
    void actualizarArchivoCSV(ElementoCSV elementoCSV);
}
