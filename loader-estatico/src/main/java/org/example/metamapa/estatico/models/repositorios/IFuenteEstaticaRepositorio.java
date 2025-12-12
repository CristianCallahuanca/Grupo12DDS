package org.example.metamapa.estatico.models.repositorios;

import org.example.metamapa.estatico.models.entidades.FuenteEstatica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IFuenteEstaticaRepositorio extends JpaRepository<FuenteEstatica, Long> {

    List<FuenteEstatica> findByPendienteProcesarTrue();

    Optional<FuenteEstatica> findByNombreFuente(String nombreFuente);
}

