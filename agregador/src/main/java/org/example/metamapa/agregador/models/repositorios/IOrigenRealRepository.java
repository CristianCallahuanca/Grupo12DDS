package org.example.metamapa.agregador.models.repositorios;

import org.example.metamapa.agregador.models.entidades.OrigenReal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IOrigenRealRepository extends JpaRepository<OrigenReal, Long> {
    Optional<OrigenReal> findByNombre(String nombre);
    Optional<OrigenReal> findByNombreIgnoreCase(String nombre);

}
