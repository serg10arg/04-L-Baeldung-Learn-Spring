package com.example.integralproyecto.dto.proyecto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * DTO para mostra la información detallada de un Proyecto (Salida de datos).
 * Se utiliza como respuesta en peticiones GET para un recurso específico.
 */
@Getter
@Setter
public class ProyectoDetalleDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaCreacion;
}
