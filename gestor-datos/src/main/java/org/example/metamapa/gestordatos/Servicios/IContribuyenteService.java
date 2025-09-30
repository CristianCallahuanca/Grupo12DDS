package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.dtos.input.ContribuyenteRegInputDTO;
import org.example.metamapa.gestordatos.models.entidades.ContribuyenteRegistrado;

public interface IContribuyenteService {

    public ContribuyenteRegistrado crearContribuyenteRegistrado(ContribuyenteRegInputDTO constribuyenteInputDTO);
}
