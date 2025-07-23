package com.example.integralproyecto.dto.persona;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para mostrar la información detallada de una Persona (Salida de datos)
 * Se utiliza como respuesta en peticiones GET para un recurso específico
 */
@Getter
@Setter
public class PersonaDetalleDTO {

    private Long id;
    private String nombre;
    private int edad;
    private String correoElectronico;
}
