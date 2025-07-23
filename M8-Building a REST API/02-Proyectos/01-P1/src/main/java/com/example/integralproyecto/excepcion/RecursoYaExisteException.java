package com.example.integralproyecto.excepcion;

public class RecursoYaExisteException extends RuntimeException {
    public RecursoYaExisteException(String message) {
        super(message);
    }
}
