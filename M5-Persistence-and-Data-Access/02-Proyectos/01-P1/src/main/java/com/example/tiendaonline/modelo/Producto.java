package com.example.tiendaonline.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity // Marca esta clase como una entidad JPA
@Table(name = "productos") // Nombre de la tabla en la base de datos
public class Producto {

    @Id // Marca el campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // // ID auto-generado por la DB
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(precision = 10, scale = 2)
    private BigDecimal precio;

    private LocalDate fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @ToString.Exclude
    private Pedido pedido;

    public Producto(String nombre, BigDecimal precio, LocalDate fechaCreacion) {
        this.nombre = nombre;
        this.precio = precio;
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return id != null && id.equals(producto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
