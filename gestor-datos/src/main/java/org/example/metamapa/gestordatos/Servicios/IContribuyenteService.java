package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.dtos.input.ContribuyenteRegInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.AuthResponse;
import org.example.metamapa.gestordatos.models.entidades.ContribuyenteRegistrado;

import java.util.Map;

public interface IContribuyenteService {

    public AuthResponse crearContribuyenteRegistrado(ContribuyenteRegInputDTO constribuyenteInputDTO);
    public AuthResponse login(String email, String password);
    public Boolean rolAdminService(String email, String password);
    public ContribuyenteRegistrado crearUsuarioGoogle(Map<String, Object> googleUserData);
    public AuthResponse loginConGoogle(Map<String, Object> googleUserData);

}
