package com.example.proyectos.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity // Declara esta clase como una entidad de persistencia
@Table(name = "project") // MEJORA: Corresponde con el nombre de la tabla en schema.sql
public class Proyecto {

    @Id // Declara el campo "id" como la clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID por la base de datos
    private Long id;

    private String nombre;
    private String descripcion;
    private LocalDate fechaCreacion;

    // mappedBy indica que la entidad Tarea es la dueña de la relación.
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tarea> tareas = new HashSet<>();

    public Proyecto(String nombre, String descripcion, LocalDate fechaCreacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
    }

    // Método de ayuda para mantener la consistencia de la relación bidireccional
    public void addTarea(Tarea tarea) {
        this.tareas.add(tarea);
        tarea.setProyecto(this);
    }

    // Método de ayuda para mantener la consistencia de la relación bidireccional
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
