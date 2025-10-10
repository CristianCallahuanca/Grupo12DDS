package org.example.metamapa.estatico.models.repositorios;

import org.example.metamapa.estatico.models.entidades.CsvProcesado;
import org.example.metamapa.estatico.models.entidades.CsvProcesadoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IRepositorioCSVProcesado extends JpaRepository<CsvProcesado, CsvProcesadoId> {

    boolean existsById_LoaderIdAndId_NombreArchivo(String loaderId, String nombreArchivo);

    @Query("SELECT c.hash FROM CsvProcesado c WHERE c.id.loaderId = :loaderId AND c.id.nombreArchivo = :nombreArchivo")
    String obtenerHashPorNombre(String loaderId, String nombreArchivo);

    @Transactional
    @Modifying
    @Query("UPDATE CsvProcesado c SET c.hash = :hash, c.fechaProcesamiento = CURRENT_TIMESTAMP " +
            "WHERE c.id.loaderId = :loaderId AND c.id.nombreArchivo = :nombreArchivo")
    void actualizarHash(String loaderId, String nombreArchivo, String hash);
}

