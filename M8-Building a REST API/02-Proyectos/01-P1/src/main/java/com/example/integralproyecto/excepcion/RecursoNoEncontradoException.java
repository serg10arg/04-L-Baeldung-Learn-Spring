package com.example.integralproyecto.excepcion;

// NO se necesita ninguna anotación de Spring aquí.
// Esta clase es ahora un objeto Java puro (POJO).
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}