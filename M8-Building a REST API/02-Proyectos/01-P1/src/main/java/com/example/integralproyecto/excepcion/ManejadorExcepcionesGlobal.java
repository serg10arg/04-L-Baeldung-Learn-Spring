package com.example.integralproyecto.excepcion;

import com.example.integralproyecto.excepcion.RecursoNoEncontradoException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase para el manejo global de excepciones en la API REST.
 * Centraliza la lógica de manejo de errores para proporcionar respuestas consistentes.
 */
@ControllerAdvice
public class ManejadorExcepcionesGlobal extends ResponseEntityExceptionHandler {

    // MEJORA: Añadir un logger para registrar errores internos.
    private static final Logger log = LoggerFactory.getLogger(ManejadorExcepcionesGlobal.class);

    /**
     * Maneja RecursoNoEncontradoException para devolver un 404 Not Found.
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Object> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex, WebRequest request) {
        // MEJORA: Simplificar la creación del ApiError. El mensaje de la excepción es suficiente.
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    /**
     * NUEVO Y CRÍTICO: Maneja RecursoYaExisteException para devolver un 409 Conflict.
     */
    @ExceptionHandler(RecursoYaExisteException.class)
    public ResponseEntity<Object> manejarRecursoYaExiste(RecursoYaExisteException ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Conflicto de recurso", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    /**
     * Maneja MethodArgumentNotValidException para errores de validación en @RequestBody.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Error de validación", errores);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja ConstraintViolationException para errores de validación en @PathVariable o @RequestParam.
     */
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errores = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Error de validación de restricción", errores);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // ... (los otros manejadores como handleHttpRequestMethodNotSupported, etc., están bien como están)
    // ... (puedes mantenerlos o simplificarlos si lo deseas)

    /**
     * Manejador de excepciones genérico para cualquier otra excepción no capturada (500).
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> manejarTodasLasExcepciones(Exception ex, WebRequest request) {
        // MEJORA: Registrar el stack trace completo para la depuración interna.
        log.error("Ocurrió un error inesperado en el servidor:", ex);

        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                "Ocurrió un error inesperado. Por favor, contacte al administrador.");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}