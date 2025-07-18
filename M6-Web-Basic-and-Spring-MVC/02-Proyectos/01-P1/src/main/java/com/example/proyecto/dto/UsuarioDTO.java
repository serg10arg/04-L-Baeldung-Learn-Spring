// C:/Users/sergg/Programacion/04-L-Baeldung-Learn-Spring/M6-Web-Basic-and-Spring-MVC/02-Proyectos/01-P1/src/main/java/com/example/proyecto/dto/UsuarioDTO.java

package com.example.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// SOLUCIÓN: Convertir el DTO de un 'record' a una clase POJO estándar.
// ModelMapper está diseñado para trabajar con la convención de JavaBeans (getters/setters/constructor vacío).
// Lombok nos permite generar todo esto automáticamente.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String correoElectronico;
}