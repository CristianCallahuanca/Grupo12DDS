package org.example.metamapa.adapters;

import org.example.metamapa.models.dtos.HechoDTO_IN;

import java.time.LocalDateTime;
import java.util.List;

public interface IAdapterMetamapa {

    List<HechoDTO_IN> obtenerHechos(String baseUrl, LocalDateTime fechaDesde);
}
