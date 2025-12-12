package org.example.metamapa.loaderdemo.models.repositorio;

import org.example.metamapa.loaderdemo.models.entidades.FuenteDemo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFuenteDemoRepositorio extends JpaRepository<FuenteDemo, Long> {
    List<FuenteDemo> findByActivaTrue();
}
