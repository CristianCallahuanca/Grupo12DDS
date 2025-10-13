package org.example.metamapa.loaderdemo.models.repositorio;

import org.example.metamapa.loaderdemo.models.entidades.EstadoLoaderDemo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadoLoaderDemoRepositorio extends JpaRepository<EstadoLoaderDemo, String> {}
