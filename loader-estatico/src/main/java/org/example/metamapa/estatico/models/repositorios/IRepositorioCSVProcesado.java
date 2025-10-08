package org.example.metamapa.estatico.models.repositorios;

import org.example.metamapa.estatico.models.entidades.CsvProcesado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IRepositorioCSVProcesado extends JpaRepository<CsvProcesado, String> {

    // Verifica si existe un archivo con ese nombre
    boolean existsByNombre(String nombreArchivo);

    // Obtiene el hash guardado de un archivo (si existe)
    @Query("SELECT c.hash FROM CsvProcesado c WHERE c.nombreArchivo = :nombreArchivo")
    String obtenerHashPorNombre(String nombreArchivo);

    // Guarda o actualiza en un mismo paso
    @Transactional
    @Modifying
    @Query("UPDATE CsvProcesado c SET c.hash = :hash, c.fechaProcesamiento = CURRENT_TIMESTAMP " +
            "WHERE c.nombreArchivo = :nombreArchivo")
    void actualizarHash(String nombreArchivo, String hash);
}
