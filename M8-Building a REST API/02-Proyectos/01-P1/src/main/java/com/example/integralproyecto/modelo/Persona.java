package com.example.integralproyecto.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "personas")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la persona no puede estar vacío.")
    @Size(max = 100, message = "El nombre debe tener como máximo 100 caracteres.")
    @Column(nullable = false, length = 150)
    private String nombre;

    @Min(value = 0, message = "La edad no puede ser negativa.")
    @Max(value = 120, message = "La edad no puede ser superior a 120 años.")
    private int edad;

    @NotBlank(message = "El correo electronico no puede estar vacio.")
    @Email(message = "El formato del correo electronico no es valido.")
    private String correoElectronico;
}
