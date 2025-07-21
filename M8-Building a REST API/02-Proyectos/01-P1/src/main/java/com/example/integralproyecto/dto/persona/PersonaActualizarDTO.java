package com.example.integralproyecto.dto.persona;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para la actualización de una Persona existente (Entrada de datos).
 * Define la estructura y validaciones para las peticiones PUT.
 */
@Getter
@Setter
public class PersonaActualizarDTO {

    @NotBlank(message = "El nombre de la persona no puede estar vacío.")
    @Size(max =100, message = "El nombre debe tener como máximo 100 caracteres.")
    private String nombre;

    @Min(value = 0, message = "La edad no puede ser negativa.")
    @Max(value = 120, message = "La edad no puede ser superior a 120 años.")
    private int edad;

    @NotBlank(message = "El correo electrónico no puede estar vacío.")
    @Email(message = "El formato del correo electrónico no es válido.")
    private String correoElectronico;
}
