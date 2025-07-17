package com.example.proyectos.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity // Declara esta clase como una entidad de persistencia
@Table(name = "task") // MEJORA: Corresponde con el nombre de la tabla en schema.sql
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;
    private LocalDate fechaCreacion;
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    private EstadoTarea estado;

    // CORRECCIÓN: Una Tarea pertenece a un Proyecto, por lo que la relación es @ManyToOne.
    // Este es el "lado dueño" de la relación, por lo que define el @JoinColumn.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id") // Apunta a la columna de clave foránea en la tabla 'task'
    @ToString.Exclude
    private Proyecto proyecto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarea tarea = (Tarea) o;
        return id != null && id.equals(tarea.id);
    }

    @Override
    public int hashCode() { // CORRECCIÓN: El nombre del método debe ser hashCode() para sobreescribir correctamente.
        // Se usa getClass() para asegurar que el hash es consistente a través de la jerarquía de clases de proxy de Hibernate.
        return getClass().hashCode();
    }
}
