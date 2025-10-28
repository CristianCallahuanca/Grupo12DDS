package org.example.metamapa.gestordatos.models.repositorios;

import org.example.metamapa.gestordatos.models.entidades.ContribuyenteRegistrado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IContribuyenteRepository extends JpaRepository<ContribuyenteRegistrado, Long> {

    Optional<ContribuyenteRegistrado> findByEmail(String email);

    boolean existsByEmail(String email);
}
