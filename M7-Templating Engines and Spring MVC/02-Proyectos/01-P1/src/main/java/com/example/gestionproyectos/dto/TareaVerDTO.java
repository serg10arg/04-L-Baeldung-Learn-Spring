package com.example.gestionproyectos.dto;

import com.example.gestionproyectos.modelos.EstadoTarea;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TareaVerDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private EstadoTarea estado;
}
