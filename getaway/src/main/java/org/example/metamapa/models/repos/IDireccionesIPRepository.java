package org.example.metamapa.models.repos;

import org.example.metamapa.models.entidades.DireccionesIP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDireccionesIPRepository extends JpaRepository<DireccionesIP, Long> {
    boolean existsByDireccionIp(String direccionIp);
}
