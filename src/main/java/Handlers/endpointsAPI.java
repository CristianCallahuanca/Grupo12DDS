package Handlers;

import Handlers.NavegarHandle.NavegarHandle;
import Handlers.ReportarHechoHandle.ReportarHechoHandle;
import Handlers.handleAlgoritmoConsenso.CambiarAlgoritmoHandle;
import Handlers.handleAprobarDesaprobarSolicitudHandle.AprobarDesaprobarSolicitudHandle;
import Handlers.handlerColeccion.*;
import Handlers.handlerHechos.GetHechosColeccionHandler;
import Handlers.handlerHechos.GetHechosHandler;
import Handlers.handlerSolicitudEliminacion.PostSolicitudesHandler;
import io.javalin.Javalin;

public class endpointsAPI {

    public void iniciarEndpoints(){

        Javalin app = Javalin.create().start(7000);

        /*API administrativa de MetaMapa*/

        //Operaciones CRUD sobre las colecciones
        app.post("/colecciones", new PostColeccionesHandler());

        app.get("/colecciones",new GetTodasColeccionesHandler());

        app.delete("/colecciones/{handle}",new DeleteColeccionesHandler());

        app.put("/colecciones/{handle}",new UpdateColeccionesHandler());

        // Modificación del algoritmo de consenso
        app.put("/algoritmoConsenso/{handle}", new CambiarAlgoritmoHandle());

        // Agregar o quitar fuentes de hechos de una colección
        app.put("/colecciones/{handle}/fuente",new ModificarFuentesColeccionHandle()); //una vez agregada la fuente o removida hay q actualizar la lista NO IMPLEMENTADO

        //Aprobar o denegar una solicitud de eliminación de un hecho
        app.put("/solicitud/{id}",new AprobarDesaprobarSolicitudHandle());


        /*API pública para otras instancias de MetaMapa*/

        //Consulta de hechos dentro de una colección
        //Navegación filtrada sobre una colección
        app.get("/colecciones/{identificador}/hechos", new GetHechosColeccionHandler()); //cumple las 2 funciones

        //Generar una solicitud de eliminación a un hecho.
        app.post("/solicitudes",new PostSolicitudesHandler());

        //obtener hechos
        app.get("/hechos", new GetHechosHandler());

        //Navegación curada o irrestricta sobre una colección
        app.get("/navegacion/{handle}", new NavegarHandle()); //esta bien el navegacion como ruta? o se podria mejorar

        //Reportar un hecho (subir hecho)
        app.post("/hecho",new ReportarHechoHandle());
    }
}
