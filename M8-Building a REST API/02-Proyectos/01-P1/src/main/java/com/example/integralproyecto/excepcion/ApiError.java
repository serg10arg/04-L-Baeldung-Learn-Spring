package com.example.integralproyecto.excepcion;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase DTO inmutable y estandarizada para las respuestas de error de la API REST.
 */
@Getter
// MEJORA: No incluir campos nulos en la respuesta JSON, la hace más limpia.
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss") // HH para formato 24h
    private final LocalDateTime fechaHora;

    // MEJORA: Código de estado numérico, el estándar en APIs REST.
    private final int estado;

    private final String mensaje;
    private final List<String> errores;

    public ApiError(HttpStatus estado, String mensaje, List<String> errores) {
        this.fechaHora = LocalDateTime.now();
        this.estado = estado.value(); // Obtenemos el valor numérico
        this.mensaje = mensaje;
        this.errores = errores;
    }

    public ApiError(HttpStatus estado, String mensaje, String error) {
        this.fechaHora = LocalDateTime.now();
        this.estado = estado.value(); // Obtenemos el valor numérico
        this.mensaje = mensaje;
        this.errores = List.of(error);
    }
}