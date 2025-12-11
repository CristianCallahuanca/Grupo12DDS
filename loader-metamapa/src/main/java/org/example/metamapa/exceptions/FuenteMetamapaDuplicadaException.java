package org.example.metamapa.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409
public class FuenteMetamapaDuplicadaException extends RuntimeException {
    public FuenteMetamapaDuplicadaException(String nombreFuente) {
        super("Ya existe una fuente MetaMapa con el nombre: " + nombreFuente);
    }
}
