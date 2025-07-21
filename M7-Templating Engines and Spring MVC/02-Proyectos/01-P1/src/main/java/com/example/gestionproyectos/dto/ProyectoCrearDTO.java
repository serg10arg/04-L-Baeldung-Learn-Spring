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
public class ProyectoCrearDTO { // Contiene solo los campos necesarios para la entrada del usuario al crear un proyecto

    @NotBlank(message = "El nombre del proyecto no puede estar en blanco")
    private String nombre;
}
