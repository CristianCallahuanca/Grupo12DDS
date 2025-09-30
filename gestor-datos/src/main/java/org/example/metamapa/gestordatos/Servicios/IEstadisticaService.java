package org.example.metamapa.gestordatos.Servicios;

public interface IEstadisticaService {

    public void generarEstadisticas();

    public void generarEstadisticaMayorCantHechosProvincia();
    public void generarEstadisticaMayorCantHechosCategoria();
    public void generarEstadisticaMayorCantCategoriaProvincia();
    public void generarEstadisticaHoraDelDia();
    public void generarEstadisticaCantidadSolicitudesSpam();
}
