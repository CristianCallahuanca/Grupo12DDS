package org.example.metamapa.exceptions;

import org.springframework.http.HttpStatusCode;

public class ExcepcionConexionMetamapa extends RuntimeException {
    public ExcepcionConexionMetamapa(String mensaje, HttpStatusCode statusCode) {
        super(mensaje + " - Código: " + statusCode.value());
    }

    public ExcepcionConexionMetamapa(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

