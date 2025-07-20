package com.example.gestionproyectos.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la tarea no puede estar en blanco")
    @Column(nullable = false, unique = true)
    private String nombre;

    private String descripcion;

    @NotNull(message = "El estado de la tarea no puede ser nulo")
    @Enumerated(EnumType.STRING) // Almacena el nombre del enum como String en BD
    @Column(nullable = false)
    private EstadoTarea estado;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarea tarea = (Tarea) o;
        return id != null && id.equals(tarea.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
