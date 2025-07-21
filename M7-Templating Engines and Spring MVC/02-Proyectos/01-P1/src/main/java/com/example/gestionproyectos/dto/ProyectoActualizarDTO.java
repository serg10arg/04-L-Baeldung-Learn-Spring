package com.example.gestionproyectos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoActualizarDTO {

    @NotBlank(message = "El nombre del proyecto no puede estar en blanco")
    private String nombre;
}
