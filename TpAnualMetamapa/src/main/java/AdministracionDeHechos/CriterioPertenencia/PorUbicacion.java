package AdministracionDeHechos.CriterioPertenencia;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Ubicacion;


public class PorUbicacion implements CriterioDePertenencia {
    private Ubicacion ubicacionBuscada;

    //Constructor
    public PorUbicacion(Ubicacion ubicacionBuscada) {
        this.ubicacionBuscada = ubicacionBuscada;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        double EPSILON = 0.000001;

        double lat1 = unHecho.getUbicacion().getLatitud();
        double lon1 = unHecho.getUbicacion().getLongitud();

        double lat2 = ubicacionBuscada.getLatitud();
        double lon2 = ubicacionBuscada.getLongitud();

        return Math.abs(lat1 - lat2) < EPSILON && Math.abs(lon1 - lon2) < EPSILON;
    }
    //Al ser double es un dato muy preciso, por lo que hay que darle un margen de error.
    //Dejo el double porque nos puede servir para despues hacer calculos.

    //return unHecho.getUbicacion().equals(ubicacionBuscada);
}
