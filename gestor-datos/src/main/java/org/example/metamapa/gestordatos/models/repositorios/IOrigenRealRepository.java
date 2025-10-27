package org.example.metamapa.gestordatos.models.repositorios;

import org.example.metamapa.gestordatos.models.entidades.OrigenReal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrigenRealRepository extends JpaRepository<OrigenReal, Long> {

}
