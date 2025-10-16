package org.example.metamapa.models.repositories;

import org.example.metamapa.models.Hechos.Hecho;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class HechoRepository {

    // Usamos una lista para simular la base de datos
    private final List<Hecho> hechos = new ArrayList<>();
    // Usamos AtomicLong para generar IDs de forma segura
    private final AtomicLong idCounter = new AtomicLong(1);

    // Constructor para llenar con datos de prueba
    public HechoRepository() {
        inicializarDatos();
    }

    public List<Hecho> findAll() {
        return this.hechos;
    }

    public Optional<Hecho> findById(Long id) {
        return hechos.stream()
                .filter(hecho -> hecho.getId().equals(id))
                .findFirst();
    }

    public Hecho save(Hecho hecho) {
        if (hecho.getId() == null) {
            // Es un hecho nuevo (CREATE)
            hecho.setId(idCounter.getAndIncrement()); // Asignamos un nuevo ID
            hechos.add(hecho);
        } else {
            // Es un hecho existente (UPDATE)
            Optional<Hecho> existingHecho = findById(hecho.getId());
            if (existingHecho.isPresent()) {
                int index = hechos.indexOf(existingHecho.get());
                hechos.set(index, hecho);
            } else {
                // Opcional: lanzar una excepción si se intenta actualizar un ID que no existe
                throw new RuntimeException("No se puede actualizar. Hecho no encontrado con ID: " + hecho.getId());
            }
        }
        return hecho;
    }

    public void deleteById(Long id) {
        hechos.removeIf(hecho -> hecho.getId().equals(id));
    }

    private void inicializarDatos() {
        Hecho hecho1 = new Hecho();
        hecho1.setId(idCounter.getAndIncrement());
        hecho1.setTitulo("Colisión en Av. Santa Fe y Av. Callao");
        hecho1.setDescripcion("Un colectivo y un auto particular colisionaron. No hay heridos graves.");
        hecho1.setCategoria("Accidente de Tránsito");
        hecho1.setLatitud("-34.5959");
        hecho1.setLongitud("-58.3934");
        hecho1.setFechaAcontecimiento(LocalDateTime.now().minusHours(2));
        hecho1.setEtiqueta("TRÁNSITO");
        hecho1.setContribuyenteID("admin");
        hecho1.setOrigen("WEB");
        hechos.add(hecho1);

        Hecho hecho2 = new Hecho();
        hecho2.setId(idCounter.getAndIncrement());
        hecho2.setTitulo("Poste de luz caído en Palermo");
        hecho2.setDescripcion("Debido al viento, un poste de luz cayó sobre la calle. Zona sin electricidad.");
        hecho2.setCategoria("Infraestructura");
        hecho2.setLatitud("-34.5801");
        hecho2.setLongitud("-58.4239");
        hecho2.setFechaAcontecimiento(LocalDateTime.now().minusDays(1));
        hecho2.setEtiqueta("PELIGRO");
        hecho2.setContribuyenteID("docente");
        hecho2.setOrigen("WEB");
        hechos.add(hecho2);
    }
}