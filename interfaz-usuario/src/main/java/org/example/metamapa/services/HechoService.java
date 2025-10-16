package org.example.metamapa.services;

import org.springframework.stereotype.Service;

import org.example.metamapa.exceptions.NotFoundException;
import org.example.metamapa.models.dto.HechoDTO;
import org.example.metamapa.models.Hechos.Hecho;
import org.example.metamapa.models.repositories.HechoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Inyección de dependencias por constructor con Lombok
public class HechoService {

    private final HechoRepository hechoRepository;

    public List<HechoDTO> obtenerTodosLosHechos() {
        return hechoRepository.findAll().stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
    }

    public HechoDTO obtenerHechoPorId(Long id) {
        Hecho hecho = intentarRecuperarHechoPorId(id);
        return convertirEntidadADTO(hecho);
    }

    public HechoDTO crearHecho(HechoDTO hechoFormDTO) {
        // La validación de campos vacíos ya la hicimos en el Controller con @Valid.
        // Aquí podrían ir validaciones más complejas (ej: verificar que las coordenadas sean válidas).

        Hecho nuevoHecho = convertirFormDTOAEntidad(hechoFormDTO);

        // Seteamos los campos que no vienen del formulario
        nuevoHecho.setOrigen("DINÁMICO");

        Hecho hechoGuardado = hechoRepository.save(nuevoHecho);
        return convertirEntidadADTO(hechoGuardado);
    }

    public HechoDTO actualizarHecho(Long id, HechoDTO hechoFormDTO) {
        // 1. Verificar que el hecho que se quiere actualizar realmente existe.
        Hecho hechoExistente = intentarRecuperarHechoPorId(id);

        // 2. Actualizamos los campos del objeto existente con los datos del DTO.
        hechoExistente.setTitulo(hechoFormDTO.getTitulo());
        hechoExistente.setDescripcion(hechoFormDTO.getDescripcion());
        hechoExistente.setCategoria(hechoFormDTO.getCategoria());
        hechoExistente.setLatitud(hechoFormDTO.getLatitud());
        hechoExistente.setLongitud(hechoFormDTO.getLongitud());
        hechoExistente.setFechaAcontecimiento(LocalDateTime.parse(hechoFormDTO.getFechaAcontecimiento()));
        hechoExistente.setEtiqueta(hechoFormDTO.getEtiqueta());

        // 3. Guardamos la entidad actualizada.
        Hecho hechoActualizado = hechoRepository.save(hechoExistente);
        return convertirEntidadADTO(hechoActualizado);
    }

    public void eliminarHecho(Long id) {
        // Verificamos que exista antes de intentar borrarlo.
        intentarRecuperarHechoPorId(id);
        hechoRepository.deleteById(id);
    }

    // --- MÉTODOS PRIVADOS DE AYUDA ---

    private Hecho intentarRecuperarHechoPorId(Long id) {
        return hechoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hecho", id.toString()));
    }

    private Hecho convertirFormDTOAEntidad(HechoDTO dto) {
        Hecho hecho = new Hecho();
        hecho.setTitulo(dto.getTitulo());
        hecho.setDescripcion(dto.getDescripcion());
        hecho.setCategoria(dto.getCategoria());
        hecho.setLatitud(dto.getLatitud());
        hecho.setLongitud(dto.getLongitud());
        hecho.setFechaAcontecimiento(LocalDateTime.parse(dto.getFechaAcontecimiento()));
        hecho.setEtiqueta(dto.getEtiqueta());
        hecho.setContribuyenteID(dto.getContribuyenteID());
        return hecho;
    }

    private HechoDTO convertirEntidadADTO(Hecho hecho) {
        HechoDTO dto = new HechoDTO();
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setLatitud(hecho.getLatitud());
        dto.setLongitud(hecho.getLongitud());
        dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento().toString());
        dto.setEtiqueta(hecho.getEtiqueta());
        dto.setContribuyenteID(hecho.getContribuyenteID());
        dto.setOrigen("DINAMICA");
        return dto;
    }
}