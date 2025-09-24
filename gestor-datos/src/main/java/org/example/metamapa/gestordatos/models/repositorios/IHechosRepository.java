package org.example.metamapa.gestordatos.models.repositorios;

import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long>, JpaSpecificationExecutor<Hecho> {
}
