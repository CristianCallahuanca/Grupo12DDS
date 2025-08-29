package dinamico.models.repositorios;

import dinamico.models.entidades.hecho.HechoCrudo;

import java.util.List;

public interface IRepositorioHechosCrudos {

   // public void guardar(HechoCrudo hecho);
    public List<HechoCrudo> obtenerHechos();
}

