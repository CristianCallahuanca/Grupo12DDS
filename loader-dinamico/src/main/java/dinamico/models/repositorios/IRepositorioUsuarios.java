package dinamico.models.repositorios;

import dinamico.models.entidades.contribuyente_registrado.Contribuyente_registrado;

public interface IRepositorioUsuarios {
    public void guardar(Contribuyente_registrado contribuyente);
}
