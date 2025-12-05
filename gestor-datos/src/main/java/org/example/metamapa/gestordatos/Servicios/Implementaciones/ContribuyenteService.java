package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IContribuyenteService;
import org.example.metamapa.gestordatos.configs.JwtUtil;
import org.example.metamapa.gestordatos.models.dtos.input.ContribuyenteRegInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.AuthResponse;
import org.example.metamapa.gestordatos.models.entidades.ContribuyenteRegistrado;
import org.example.metamapa.gestordatos.models.entidades.Rol;
import org.example.metamapa.gestordatos.models.entidades.enums.Provider;
import org.example.metamapa.gestordatos.models.repositorios.IContribuyenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

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

        Optional<ContribuyenteRegistrado> usuarioExistente = contribuyenteRepository.findByEmail(constribuyenteInputDTO.getEmail());

        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }

        ContribuyenteRegistrado usuario  = new ContribuyenteRegistrado(constribuyenteInputDTO.getNombre(),
                constribuyenteInputDTO.getApellido(),
                constribuyenteInputDTO.getEmail(),passwordEncoder.encode(constribuyenteInputDTO.getPassword()),"",Provider.LOCAL);


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

    public AuthResponse loginConGoogle(Map<String, Object> googleUserData) {
        System.out.println("🔐 LOGIN CON GOOGLE");

        String email = (String) googleUserData.get("email");
        String googleId = (String) googleUserData.get("sub");

        System.out.println("   Email: " + email);
        System.out.println("   Google ID: " + googleId);

        // 1. Buscar usuario por Google ID primero
        Optional<ContribuyenteRegistrado> usuarioOpt =
                contribuyenteRepository.findByGoogleId(googleId);

        if (usuarioOpt.isEmpty()) {
            // 2. Si no existe por Google ID, buscar por email
            usuarioOpt = contribuyenteRepository.findByEmail(email);

            if (usuarioOpt.isPresent()) {
                // Usuario existe por email pero no tiene Google ID
                ContribuyenteRegistrado usuario = usuarioOpt.get();

                // Si es usuario LOCAL, agregar Google ID (fusionar cuentas)
                if (usuario.getProvider() == Provider.LOCAL) {
                    System.out.println("🔄 Fusionando cuenta LOCAL con Google");
                    usuario.setGoogleId(googleId);
                    usuario.setProvider(Provider.GOOGLE);
                    contribuyenteRepository.save(usuario);
                }
            } else {
                // 3. Crear NUEVO usuario Google
                System.out.println("➕ Creando nuevo usuario Google");
                usuarioOpt = Optional.of(crearUsuarioGoogle(googleUserData));
            }
        }

        // 4. Obtener usuario (ya existe o fue creado)
        ContribuyenteRegistrado usuario = usuarioOpt.get();

        // 5. Generar token JWT (¡EL MISMO MÉTODO que login normal!)
        String token = jwtUtil.generateToken(usuario);

        System.out.println("✅ Token generado para usuario: " + usuario.getEmail());

        // 6. Crear respuesta (¡EXACTAMENTE IGUAL que login normal!)
        return new AuthResponse(
                token,
                "Login con Google exitoso",
                usuario.getUserId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getRol()
        );
    }

    public ContribuyenteRegistrado crearUsuarioGoogle(Map<String, Object> googleUserData) {
        ContribuyenteRegistrado usuario = new ContribuyenteRegistrado();

        // Datos básicos del usuario
        usuario.setEmail((String) googleUserData.get("email"));
        usuario.setGoogleId((String) googleUserData.get("sub"));
        usuario.setProvider(Provider.GOOGLE);

        // Nombre y apellido
        String nombreCompleto = (String) googleUserData.get("name");
        if (nombreCompleto != null && !nombreCompleto.isEmpty()) {
            String[] partes = nombreCompleto.split(" ");
            usuario.setNombre(partes.length > 0 ? partes[0] : "Usuario");
            usuario.setApellido(partes.length > 1 ? partes[partes.length - 1] : "Google");
        }


        // Rol por defecto
        usuario.setRol(Rol.USER);

        // Guardar en BD
        return contribuyenteRepository.save(usuario);
    }
}
