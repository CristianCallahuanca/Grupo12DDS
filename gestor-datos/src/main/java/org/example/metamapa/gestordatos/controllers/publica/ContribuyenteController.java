package org.example.metamapa.gestordatos.controllers.publica;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.metamapa.gestordatos.Servicios.IContribuyenteService;
import org.example.metamapa.gestordatos.Servicios.Implementaciones.GoogleAuthService;
import org.example.metamapa.gestordatos.models.dtos.input.ContribuyenteRegInputDTO;
import org.example.metamapa.gestordatos.models.dtos.input.DarRolAdminRequest;
import org.example.metamapa.gestordatos.models.dtos.input.LoginRequest;
import org.example.metamapa.gestordatos.models.dtos.output.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/gestordatos/contribuyentes")
public class ContribuyenteController {

    private final IContribuyenteService contribuyenteService;
    private final GoogleAuthService googleAuthService;

    public ContribuyenteController(IContribuyenteService contribuyenteService, GoogleAuthService googleAuthService) {
        this.contribuyenteService = contribuyenteService;
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/registrarse")
    public ResponseEntity<?> registrar(@RequestBody ContribuyenteRegInputDTO inputDTO) {
        var response = contribuyenteService.crearContribuyenteRegistrado(inputDTO);

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = contribuyenteService.login(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/darRolAdmin")
    public ResponseEntity<String> rolAdmin(@RequestBody DarRolAdminRequest request) {
        Boolean seAsignoRol = contribuyenteService.rolAdminService(request.getEmail(), request.getPassword());

        if(seAsignoRol) {
            return ResponseEntity.status(200).body("Se le asignó el rol ADMIN");
        }
        return ResponseEntity.status(400).body("No se le pudo asignar el rol");
    }

    @GetMapping("/google")
    public ResponseEntity<?> iniciarLoginGoogle(HttpServletRequest request) {
        try {
            // Generar state único para seguridad
            String state = UUID.randomUUID().toString();

            // Guardar en sesión
            HttpSession session = request.getSession();
            session.setAttribute("oauth_state", state);
            session.setAttribute("oauth_state_time", System.currentTimeMillis());

            // Obtener URL de Google
            String googleAuthUrl = googleAuthService.getGoogleAuthUrl(state);

            // Redirigir al usuario
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", googleAuthUrl)
                    .build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error iniciando login con Google: " + e.getMessage());
        }
    }

    @GetMapping("/google/callback")
    public ResponseEntity<?> callbackGoogle(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletRequest request) {

        System.out.println("🔵 CALLBACK GOOGLE RECIBIDO");

        try {
            // 1. Validar state (seguridad)
            HttpSession session = request.getSession();
            String savedState = (String) session.getAttribute("oauth_state");

            if (savedState == null || !savedState.equals(state)) {
                return ResponseEntity.status(400)
                        .body("Error de seguridad: state inválido");
            }

            // 2. Intercambiar código por tokens de Google
            Map<String, Object> tokens = googleAuthService.exchangeCodeForTokens(code);

            // 3. Decodificar JWT de Google para obtener datos del usuario
            String idToken = (String) tokens.get("id_token");
            Map<String, Object> googleUser = googleAuthService.decodeGoogleToken(idToken);

            // 4. Usar tu servicio para manejar el usuario (igual que login)
            AuthResponse response = contribuyenteService.loginConGoogle(googleUser);

            System.out.println("🎉 Login Google exitoso para: " + response.getEmail());

            // 5. Devolver MISMA respuesta que login normal
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Manejar errores de negocio (igual que login)
            System.out.println("❌ Error en login Google: " + e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());

        } catch (Exception e) {
            // Manejar errores técnicos
            System.out.println("💥 Error técnico en Google callback: " + e.getMessage());
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }


}

/*
*
*   private String nombre;
    private String apellido;
    private Integer dni;
    private Date fechaNacimiento;
    private String email;
    private String password;*/
