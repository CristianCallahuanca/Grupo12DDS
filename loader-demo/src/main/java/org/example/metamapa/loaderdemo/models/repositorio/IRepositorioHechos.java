package org.example.metamapa.loaderdemo.models.repositorio;

import org.example.metamapa.loaderdemo.models.entidades.HechoCrudo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepositorioHechos extends CrudRepository<HechoCrudo, Long> {

}