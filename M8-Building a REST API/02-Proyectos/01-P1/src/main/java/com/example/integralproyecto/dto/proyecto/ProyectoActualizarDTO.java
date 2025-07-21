package com.example.integralproyecto.dto.proyecto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para la actualización de un Proyecto existente (Entrada de datos).
 * Define la estructura y validaciones para las peticiones PUT.
 */
@Getter
@Setter
public class ProyectoActualizarDTO {

    @NotBlank(message = "El nombre del proyecto no puede estar vacío.")
    @Size(max = 150, message = "El nombre debe tener como máximo 150 caracteres.")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres.")
    private String descripcion;
}
