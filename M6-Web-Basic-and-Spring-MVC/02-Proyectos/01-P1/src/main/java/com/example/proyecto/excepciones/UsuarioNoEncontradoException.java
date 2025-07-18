package com.example.proyecto.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// MEJORA: Crear una excepción semántica y específica.
// @ResponseStatus(HttpStatus.NOT_FOUND) hace que Spring devuelva automáticamente un código 404
// cuando esta excepción no es capturada y llega a la capa del controlador.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(String message) {
        super(message);
    }
}