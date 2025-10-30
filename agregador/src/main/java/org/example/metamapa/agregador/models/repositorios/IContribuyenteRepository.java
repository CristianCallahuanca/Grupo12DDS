package org.example.metamapa.agregador.models.repositorios;

import org.example.metamapa.agregador.models.entidades.ContribuyenteRegistrado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IContribuyenteRepository extends JpaRepository<ContribuyenteRegistrado, Long> {
}
