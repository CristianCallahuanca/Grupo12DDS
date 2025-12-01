package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.gestordatos.configs.JwtUtil;
import org.example.metamapa.gestordatos.models.dtos.output.AuthResponse;
import org.example.metamapa.gestordatos.models.dtos.output.UsuarioDTO;
import org.example.metamapa.gestordatos.models.entidades.ContribuyenteRegistrado;
import org.example.metamapa.gestordatos.models.entidades.enums.Provider;
import org.example.metamapa.gestordatos.models.repositorios.IContribuyenteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class GoogleAuthService {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private final IContribuyenteRepository contribuyenteRepository;
    private final JwtUtil jwtUtil;

    GoogleAuthService(IContribuyenteRepository contribuyenteRepository,JwtUtil jwtUtil){
        this.contribuyenteRepository = contribuyenteRepository;
        this.jwtUtil = jwtUtil;
    }

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Generar URL para iniciar login con Google
     */
    public String getGoogleAuthUrl(String state) {
        try {
            String encodedRedirectUri = URLEncoder.encode(
                    redirectUri,
                    StandardCharsets.UTF_8.toString()
            );

            return "https://accounts.google.com/o/oauth2/v2/auth" +
                    "?client_id=" + clientId +
                    "&redirect_uri=" + encodedRedirectUri +
                    "&response_type=code" +
                    "&scope=openid%20email%20profile" +
                    "&state=" + state +
                    "&access_type=online" +
                    "&prompt=select_account";

        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar la URL de Google", e);
        }
    }

    /**
     * Intercambiar código por tokens de Google
     */
    public Map<String, Object> exchangeCodeForTokens(String code) {
        System.out.println("════════════════════════════════════════");
        System.out.println("🔄 LLAMANDO A exchangeCodeForTokens");
        System.out.println("   Código recibido: " + (code != null ? "SÍ (" + code.length() + " chars)" : "NO"));

        try {
            // DEBUG: Verificar propiedades cargadas
            System.out.println("📋 Propiedades cargadas:");
            System.out.println("   clientId: " + (clientId != null ? clientId.substring(0, Math.min(20, clientId.length())) + "..." : "NULL"));
            System.out.println("   clientSecret: " + (clientSecret != null ? "CONFIGURADO (" + clientSecret.length() + " chars)" : "NULL"));
            System.out.println("   redirectUri: " + redirectUri);

            if (clientSecret == null || clientSecret.isEmpty()) {
                System.out.println("❌ ERROR: clientSecret no configurado");
                System.out.println("   Verifica application.properties");
                throw new RuntimeException("Client secret no configurado");
            }

            // 1. Preparar la solicitud
            String requestBody = "code=" + code +
                    "&client_id=" + clientId +
                    "&client_secret=" + clientSecret +
                    "&redirect_uri=" + redirectUri +
                    "&grant_type=authorization_code";

            System.out.println("📤 Cuerpo de solicitud (sin secret):");
            System.out.println("   " + requestBody.replace(clientSecret, "[HIDDEN]"));

            // 2. Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // 3. Crear solicitud HTTP
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // 4. Hacer petición POST
            System.out.println("🌐 Enviando POST a Google OAuth...");

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://oauth2.googleapis.com/token",
                    request,
                    String.class
            );

            System.out.println("📥 Respuesta recibida:");
            System.out.println("   Status: " + response.getStatusCode());
            System.out.println("   Body length: " + response.getBody().length());

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> tokens = objectMapper.readValue(
                        response.getBody(),
                        HashMap.class
                );

                System.out.println("🎉 ¡ÉXITO! Tokens obtenidos:");
                for (String key : tokens.keySet()) {
                    Object value = tokens.get(key);
                    if (value instanceof String && ((String) value).length() > 30) {
                        System.out.println("   " + key + ": " + ((String) value).substring(0, 30) + "...");
                    } else {
                        System.out.println("   " + key + ": " + value);
                    }
                }

                System.out.println("════════════════════════════════════════");
                return tokens;

            } else {
                System.out.println("❌ Error HTTP de Google:");
                System.out.println("   " + response.getBody());
                System.out.println("════════════════════════════════════════");
                throw new RuntimeException("Error de Google: " + response.getBody());
            }

        } catch (Exception e) {
            System.out.println("💥 EXCEPCIÓN en exchangeCodeForTokens:");
            System.out.println("   Tipo: " + e.getClass().getSimpleName());
            System.out.println("   Mensaje: " + e.getMessage());
            System.out.println("════════════════════════════════════════");
            e.printStackTrace();
            throw new RuntimeException("Error intercambiando tokens", e);
        }
    }

    public Map<String, Object> decodeGoogleToken(String idToken) {
        System.out.println("🔓 DECODIFICANDO JWT DE GOOGLE");
        System.out.println("   Token length: " + idToken.length());

        try {
            // 1. Separar el JWT en sus 3 partes
            String[] parts = idToken.split("\\.");

            if (parts.length < 3) {
                throw new RuntimeException("Token JWT inválido");
            }

            // 2. Decodificar el payload (segunda parte)
            String payload = parts[1];
            System.out.println("   Payload codificado: " + payload.substring(0, 50) + "...");

            // 3. Decodificar Base64URL
            byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
            String decodedPayload = new String(decodedBytes);
            System.out.println("   Payload decodificado: " + decodedPayload.substring(0, 100) + "...");

            // 4. Convertir JSON a Map
            Map<String, Object> userInfo = objectMapper.readValue(decodedPayload, HashMap.class);

            // 5. Mostrar información importante
            System.out.println("👤 DATOS DEL USUARIO GOOGLE:");
            System.out.println("   Email: " + userInfo.get("email"));
            System.out.println("   Nombre: " + userInfo.get("name"));
            System.out.println("   Google ID (sub): " + userInfo.get("sub"));
            System.out.println("   Email verificado: " + userInfo.get("email_verified"));

            return userInfo;

        } catch (Exception e) {
            System.out.println("❌ Error decodificando JWT: " + e.getMessage());
            throw new RuntimeException("Error al decodificar token de Google", e);
        }
    }

    public AuthResponse handleGoogleUser(Map<String, Object> googleUserData) {
        System.out.println("👤 MANEJANDO USUARIO GOOGLE (versión simple)");

        String email = (String) googleUserData.get("email");
        String googleId = (String) googleUserData.get("sub");
        String nombreCompleto = (String) googleUserData.get("name");

        System.out.println("   Email: " + email);
        System.out.println("   Google ID: " + googleId);

        try {
            // 1. Buscar por Google ID
            Optional<ContribuyenteRegistrado> porGoogleId =
                    contribuyenteRepository.findByGoogleId(googleId);

            if (porGoogleId.isPresent()) {
                System.out.println("✅ Usuario ya existe (por Google ID)");
                return generarToken(porGoogleId.get());
            }

            // 2. Buscar por email
            Optional<ContribuyenteRegistrado> porEmail =
                    contribuyenteRepository.findByEmail(email);

            if (porEmail.isPresent()) {
                ContribuyenteRegistrado usuario = porEmail.get();
                System.out.println("⚠️  Email ya registrado");

                // Si es usuario LOCAL, agregar Google ID
                if (usuario.getProvider() == Provider.LOCAL) {
                    System.out.println("   Agregando Google ID a usuario existente");
                    usuario.setGoogleId(googleId);
                    usuario.setProvider(Provider.HYBRID);
                    usuario = contribuyenteRepository.save(usuario);
                }

                return generarToken(usuario);
            }

            // 3. Crear NUEVO usuario (SOLO con lo básico)
            System.out.println("➕ Creando NUEVO usuario Google");

            ContribuyenteRegistrado nuevoUsuario = new ContribuyenteRegistrado();

            // Solo estos 3 campos obligatorios:
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setGoogleId(googleId);
            nuevoUsuario.setProvider(Provider.GOOGLE);

            // Extraer nombre y apellido del nombre completo
            if (nombreCompleto != null && !nombreCompleto.isEmpty()) {
                String[] partes = nombreCompleto.split(" ");
                if (partes.length > 0) {
                    nuevoUsuario.setNombre(partes[0]);
                }
                if (partes.length > 1) {
                    nuevoUsuario.setApellido(partes[partes.length - 1]);
                }
            } else {
                nuevoUsuario.setNombre("Usuario");
                nuevoUsuario.setApellido("Google");
            }

            // Opcional: si quieres guardar la foto
            // nuevoUsuario.setFotoUrl((String) googleUserData.get("picture"));

            // Los demás campos (dni, fecha_nacimiento, password) quedan NULL

            nuevoUsuario = contribuyenteRepository.save(nuevoUsuario);

            System.out.println("✅ Usuario creado con ID: " + nuevoUsuario.getUserId());

            return generarToken(nuevoUsuario);

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            throw new RuntimeException("Error con usuario Google", e);
        }
    }

    private AuthResponse generarToken(ContribuyenteRegistrado usuario) {
        // ¡USA EL MISMO JwtUtil que tu login normal!
        String token = jwtUtil.generateToken(usuario);

        System.out.println("🔑 Token generado: " + token.substring(0, 30) + "...");

        // Crea la respuesta IGUAL que en tu login normal
        return new AuthResponse(
                token,
                "Login con Google exitoso",  // Mensaje similar
                usuario.getUserId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getRol()
        );
    }

}