package com.example.gestionproyectos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProyectoVerDTO {

    private Long id;
    private String nombre;
    private LocalDate fechaCreacion;
    private Set<TareaVerDTO> tareas;
}
