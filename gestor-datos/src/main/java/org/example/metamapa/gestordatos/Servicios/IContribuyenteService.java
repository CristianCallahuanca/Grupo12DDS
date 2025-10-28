package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.dtos.input.ContribuyenteRegInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.AuthResponse;
import org.example.metamapa.gestordatos.models.entidades.ContribuyenteRegistrado;

public interface IContribuyenteService {

    public AuthResponse crearContribuyenteRegistrado(ContribuyenteRegInputDTO constribuyenteInputDTO);
    public AuthResponse login(String email, String password);
}
