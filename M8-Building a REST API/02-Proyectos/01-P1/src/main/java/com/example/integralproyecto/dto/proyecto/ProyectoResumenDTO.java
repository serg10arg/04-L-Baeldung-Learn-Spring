package com.example.integralproyecto.dto.proyecto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para mostrar una vista resumida de un proyecto (Salida de datos).
 * Ideal para ser usados en listas, manteniendo los payloads ligeros y eficientes.
 */
@Getter
@Setter
public class ProyectoResumenDTO {

    private Long id;
    private String nombre;
}
