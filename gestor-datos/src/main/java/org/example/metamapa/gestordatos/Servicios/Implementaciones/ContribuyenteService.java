package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IContribuyenteService;
import org.example.metamapa.gestordatos.configs.JwtUtil;
import org.example.metamapa.gestordatos.models.dtos.input.ContribuyenteRegInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.AuthResponse;
import org.example.metamapa.gestordatos.models.entidades.ContribuyenteRegistrado;
import org.example.metamapa.gestordatos.models.entidades.Rol;
import org.example.metamapa.gestordatos.models.repositorios.IContribuyenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ContribuyenteService implements IContribuyenteService {

    private final IContribuyenteRepository contribuyenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    ContribuyenteService(IContribuyenteRepository contribuyenteRepository) {
        this.contribuyenteRepository = contribuyenteRepository;
    }

    public AuthResponse crearContribuyenteRegistrado(ContribuyenteRegInputDTO constribuyenteInputDTO) {

        ContribuyenteRegistrado usuario  = new ContribuyenteRegistrado(constribuyenteInputDTO.getNombre(),
                constribuyenteInputDTO.getApellido(), constribuyenteInputDTO.getFechaNacimiento(), constribuyenteInputDTO.getDni(),
                constribuyenteInputDTO.getEmail(),passwordEncoder.encode(constribuyenteInputDTO.getPassword()));

        contribuyenteRepository.save(usuario);

        String token = jwtUtil.generateToken(usuario);

        AuthResponse response = new AuthResponse(
                token, // Por ahora sin JWT
                "Usuario registrado correctamente",
                usuario.getUserId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getRol()
        );

        return response;
    }

    public AuthResponse login(String email, String password) {
        // Buscar usuario por email
        ContribuyenteRegistrado usuario = contribuyenteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar contraseña
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // Generar token JWT
        String token = jwtUtil.generateToken(usuario);

        // Crear respuesta
        return new AuthResponse(
                token,
                "Login exitoso",
                usuario.getUserId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getRol()
        );
    }

    public Boolean rolAdminService(String email, String password){

        ContribuyenteRegistrado usuario = contribuyenteRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if(password.equals("TerribleContraseniaPRO")){
            usuario.setRol(Rol.ADMIN);
            contribuyenteRepository.save(usuario);
            return true;
        }

        return false;
    }
}
