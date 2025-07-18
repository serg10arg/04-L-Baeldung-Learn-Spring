package com.example.proyecto.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
// CORRECCIÓN: La anotación @Table requiere que el nombre de la tabla se especifique
// con el atributo 'name'. El atajo @Table("...") no es válido para esta anotación porque
// no tiene un atributo 'value()'.
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Email // MEJORA: Añade validación a nivel de entidad.
    @Column(name = "correo_electronico", unique = true) // MEJORA: Nombres de columna explícitos y restricción de unicidad.
    private String correoElectronico; // CORRECCIÓN: Corregido a camelCase estándar.

    public Usuario(String nombre, String correoElectronico) {
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
    }

}
