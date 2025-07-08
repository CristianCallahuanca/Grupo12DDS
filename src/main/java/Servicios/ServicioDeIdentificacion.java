package Servicios;

import java.util.UUID;

public class ServicioDeIdentificacion {

    private static final ServicioDeIdentificacion instance = new ServicioDeIdentificacion();
    public static ServicioDeIdentificacion getInstancia() {return instance;}

    private int contadorIdHechos = 0;
    private int contadorIdSolicitudEliminar = 0;

    public int generarIDHecho() { return contadorIdHechos++;}

    public int generarIDSolicitudEliminacion() {return contadorIdSolicitudEliminar++;}

    public String generarHandle() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
