package com.example.p1.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Producto {
    private Long id;
    private String nombre;
    private String descripcion;
    private int stock;
    private String codigoInventario;
    private LocalDate fechaCreacion;

    public Producto(String nombre, String descripcion, int stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
    }
}
