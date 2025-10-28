package dinamico.controllers;

import dinamico.models.entidades.hecho.HechoCrudo;
import dinamico.models.repositorios.IRepositorioHechosCrudos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fuenteDinamica")
@CrossOrigin(origins = "*")
public class HechoCrudoController {

    @Autowired
    private IRepositorioHechosCrudos hechoCrudoRepository;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    // ✅ SOLO los 7 campos obligatorios + archivos opcionales
    //@PostMapping(value = "/hecho", consumes = {"multipart/form-data"})
    public ResponseEntity<?> crearHechoCrudo(
            // ✅ 7 CAMPOS OBLIGATORIOS (exactamente como tu JSON)
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam String categoria,
            @RequestParam String latitud,
            @RequestParam String longitud,
            @RequestParam String fechaAcontecimiento,
            @RequestParam String etiqueta,

            // ✅ CAMPOS OPCIONALES
            @RequestParam(required = false) String contribuyenteID,
            @RequestParam(required = false) List<MultipartFile> archivos) {

        try {
            System.out.println("=== 📨 RECIBIENDO HECHO ===");
            System.out.println("Título: " + titulo);
            System.out.println("Descripción: " + descripcion);
            System.out.println("Categoría: " + categoria);
            System.out.println("Latitud: " + latitud);
            System.out.println("Longitud: " + longitud);
            System.out.println("Fecha Acontecimiento: " + fechaAcontecimiento);
            System.out.println("Etiqueta: " + etiqueta);
            System.out.println("Contribuyente ID: " + contribuyenteID);
            System.out.println("Archivos recibidos: " + (archivos != null ? archivos.size() : 0));

            // PASO 1: Procesar archivos si existen (OPCIONAL)
            List<String> pathsArchivos = new ArrayList<>();

            if (archivos != null && !archivos.isEmpty()) {
                System.out.println("🖼️ Procesando archivos...");
                for (MultipartFile archivo : archivos) {
                    if (!archivo.isEmpty()) {
                        String pathArchivo = guardarArchivo(archivo);
                        pathsArchivos.add(pathArchivo);
                        System.out.println("✅ Archivo guardado: " + pathArchivo);
                    }
                }
            }

            // PASO 2: Crear HechoCrudo con los 7 campos obligatorios
            HechoCrudo hechoCrudo = new HechoCrudo(
                    titulo,
                    descripcion,
                    categoria,
                    latitud,
                    longitud,
                    fechaAcontecimiento,
                    etiqueta,
                    contribuyenteID,  // Puede ser null
                    pathsArchivos     // Puede estar vacía
            );

            // PASO 3: Guardar en base de datos
            HechoCrudo hechoGuardado = hechoCrudoRepository.save(hechoCrudo);
            System.out.println("💾 Hecho guardado en BD con ID: " + hechoGuardado.getId());

            return ResponseEntity.ok(hechoGuardado);

        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al crear hecho: " + e.getMessage());
        }
    }

    // ✅ Método auxiliar para guardar archivos
    private String guardarArchivo(MultipartFile archivo) throws IOException {
        // Crear directorio si no existe
        Path directorioUpload = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(directorioUpload);

        // Generar nombre único
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = nombreOriginal != null && nombreOriginal.contains(".")
                ? nombreOriginal.substring(nombreOriginal.lastIndexOf("."))
                : ".jpg";

        String nombreUnico = "hecho_" + System.currentTimeMillis() +
                "_" + UUID.randomUUID().toString() + extension;

        // Guardar archivo
        Path rutaDestino = directorioUpload.resolve(nombreUnico);
        Files.copy(archivo.getInputStream(), rutaDestino);

        return nombreUnico;
    }
}