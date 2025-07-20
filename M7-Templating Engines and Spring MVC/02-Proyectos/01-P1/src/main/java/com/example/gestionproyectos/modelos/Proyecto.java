package com.example.gestionproyectos.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "proyectos")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del proyecto no puede estar en blanco")
    @Column(nullable = false, unique = true)
    private String nombre;

    @NotNull(message = "La fecha de creacion no puede ser nula")
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true, fetch =  FetchType.LAZY)
    private Set<Tarea> tareas = new HashSet<>();

    public Proyecto(String nombre, LocalDate fechaCreacion) {
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
    }

    // Metódo de ayuda para mantener la consistencia de la relación bidireccional
    public void addTarea(Tarea tarea) {
        this.tareas.add(tarea);
        tarea.setProyecto(this);
    }

    // Metódo de ayuda para mantener la consistencia de la relacion bidireccional
    public void removeTarea(Tarea tarea) {
        this.tareas.remove(tarea);
        tarea.setProyecto(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proyecto proyecto = (Proyecto) o;
        return id != null && id.equals(proyecto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
