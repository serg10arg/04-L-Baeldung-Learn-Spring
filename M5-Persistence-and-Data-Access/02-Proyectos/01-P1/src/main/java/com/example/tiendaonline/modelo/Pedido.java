package com.example.tiendaonline.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity // Marca esta clase como una entidad JPA
@Table(name = "pedidos") // // Nombre de la tabla en la base de datos
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private LocalDate fechaPedido;

    // mappedBy para indicar que la clase producto es la dueña de la relación
    // "orphanRemoval = true" elimina productos de la DB si se quitan de la colección del pedido
    @OneToMany(mappedBy = "pedido",
               cascade = CascadeType.ALL,
               orphanRemoval = true
    )
    @ToString.Exclude // Evita bucles infinitos en el toString()
    private Set<Producto> productos = new HashSet<>();

    public Pedido(String descripcion, LocalDate fechaPedido) {
        this.descripcion = descripcion;
        this.fechaPedido = fechaPedido;
    }

    public void addProducto(Producto producto) {
        this.productos.add(producto);
        producto.setPedido(this);
    }

    public void removeProducto(Producto producto) {
        this.productos.remove(producto);
        producto.setPedido(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return id != null && id.equals(pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
