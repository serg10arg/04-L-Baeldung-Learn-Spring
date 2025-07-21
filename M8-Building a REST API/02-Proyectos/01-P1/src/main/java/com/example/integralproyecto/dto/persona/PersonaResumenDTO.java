package com.example.integralproyecto.dto.persona;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para mostrar una vista resumida de una Persona (Salida de datos).
 * Ideal para ser usado en listas, manteniendo los payloads ligeros y eficientes.
 */
@Getter
@Setter
public class PersonaResumenDTO {

    private Long id;
    private String nombre;
    private String correoElectronico;
}
