package org.example.metamapa.gestordatos.models.repositorios;

import org.example.metamapa.gestordatos.models.entidades.ContribuyenteRegistrado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IContribuyenteRepository extends JpaRepository<ContribuyenteRegistrado, Long> {

    // Métodos existentes:
    Optional<ContribuyenteRegistrado> findByEmail(String email);
    boolean existsByEmail(String email);

    // MÉTODOS NUEVOS para Google:
    Optional<ContribuyenteRegistrado> findByGoogleId(String googleId);

    // Buscar por email o Google ID
    @Query("SELECT c FROM ContribuyenteRegistrado c WHERE c.email = :identifier OR c.googleId = :identifier")
    Optional<ContribuyenteRegistrado> findByEmailOrGoogleId(String identifier);


}
