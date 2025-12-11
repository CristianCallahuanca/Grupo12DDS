package org.example.metamapa.models.repositorio;

import org.example.metamapa.models.entidades.FuenteMetamapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFuenteMetamapaRepository extends JpaRepository<FuenteMetamapa, Long> {

    List<FuenteMetamapa> findByActivaTrue();
    boolean existsByNombreFuente(String nombreFuente);

}
