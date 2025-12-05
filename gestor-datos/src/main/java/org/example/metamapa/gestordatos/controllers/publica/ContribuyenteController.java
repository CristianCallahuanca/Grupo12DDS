package org.example.metamapa.gestordatos.controllers.publica;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/gestordatos/contribuyentes")
public class ContribuyenteController {

    private final IContribuyenteService contribuyenteService;
    private final GoogleAuthService googleAuthService;
    private final ObjectMapper objectMapper;

    public ContribuyenteController(IContribuyenteService contribuyenteService,
                                   GoogleAuthService googleAuthService,
                                   ObjectMapper objectMapper) {
        this.contribuyenteService = contribuyenteService;
        this.googleAuthService = googleAuthService;
        this.objectMapper = objectMapper;
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
    public void callbackGoogle(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "error", required = false) String error,
            HttpServletRequest request,
            HttpServletResponse httpResponse) throws IOException {

        System.out.println("🎯 GOOGLE CALLBACK - CORS PERMITIENDO TODO");

        // Headers para permitir popups
        httpResponse.setContentType("text/html;charset=UTF-8");
        httpResponse.setHeader("Cross-Origin-Opener-Policy", "unsafe-none");
        httpResponse.setHeader("Cross-Origin-Embedder-Policy", "unsafe-none");

        try {
            // Si hay error
            if (error != null) {
                String html = """
                <!DOCTYPE html>
                <html>
                <script>
                    window.opener.postMessage({
                        type: 'GOOGLE_LOGIN_ERROR',
                        error: 'Error de Google: %s'
                    }, '*');
                    setTimeout(() => window.close(), 100);
                </script>
                </html>
                """.formatted(error);
                httpResponse.getWriter().write(html);
                return;
            }

            // Validar code y state
            if (code == null || state == null) {
                String html = """
                <!DOCTYPE html>
                <html>
                <script>
                    window.opener.postMessage({
                        type: 'GOOGLE_LOGIN_ERROR',
                        error: 'Faltan parámetros'
                    }, '*');
                    setTimeout(() => window.close(), 100);
                </script>
                </html>
                """;
                httpResponse.getWriter().write(html);
                return;
            }

            // 1. Validar state
            HttpSession session = request.getSession();
            String savedState = (String) session.getAttribute("oauth_state");

            if (savedState == null || !savedState.equals(state)) {
                String html = """
                <!DOCTYPE html>
                <html>
                <script>
                    window.opener.postMessage({
                        type: 'GOOGLE_LOGIN_ERROR',
                        error: 'State inválido'
                    }, '*');
                    setTimeout(() => window.close(), 100);
                </script>
                </html>
                """;
                httpResponse.getWriter().write(html);
                return;
            }

            // Limpiar state
            session.removeAttribute("oauth_state");
            session.removeAttribute("oauth_state_time");

            // 2. Obtener tokens de Google
            Map<String, Object> tokens = googleAuthService.exchangeCodeForTokens(code);

            // 3. Decodificar token
            String idToken = (String) tokens.get("id_token");
            Map<String, Object> googleUser = googleAuthService.decodeGoogleToken(idToken);

            // 4. Login con Google
            AuthResponse authResponse = contribuyenteService.loginConGoogle(googleUser);

            System.out.println("✅ Login exitoso para: " + authResponse.getEmail());

            // 5. Crear respuesta
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("type", "GOOGLE_LOGIN_SUCCESS");
            responseData.put("token", authResponse.getToken());
            responseData.put("userId", authResponse.getUserId());
            responseData.put("email", authResponse.getEmail());
            responseData.put("nombre", authResponse.getNombre());
            responseData.put("apellido", authResponse.getApellido());
            responseData.put("rol", authResponse.getRol());

            // Convertir a JSON
            String jsonData = objectMapper.writeValueAsString(responseData);

            // 6. HTML que envía datos y cierra ventana
            String html = """
            <!DOCTYPE html>
            <html>
            <script>
                try {
                    const data = %s;
                    window.opener.postMessage(data, '*');
                } catch(e) {
                    window.opener.postMessage({
                        type: 'GOOGLE_LOGIN_ERROR',
                        error: 'Error: ' + e.message
                    }, '*');
                }
                setTimeout(() => window.close(), 100);
            </script>
            </html>
            """.formatted(jsonData);

            httpResponse.getWriter().write(html);

        } catch (Exception e) {
            System.out.println("💥 ERROR: " + e.getMessage());
            String html = """
            <!DOCTYPE html>
            <html>
            <script>
                window.opener.postMessage({
                    type: 'GOOGLE_LOGIN_ERROR',
                    error: 'Error del servidor'
                }, '*');
                setTimeout(() => window.close(), 100);
            </script>
            </html>
            """;
            httpResponse.getWriter().write(html);
        }
    }
}