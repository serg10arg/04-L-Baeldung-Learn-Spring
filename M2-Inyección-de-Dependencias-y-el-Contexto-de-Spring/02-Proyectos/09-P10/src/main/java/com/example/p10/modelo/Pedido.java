package com.example.p10.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // Genera getters, setters, toString, equals, hashCode, etc.
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    private Long id;
    private List<Producto> productos;
    private double total;
    private String estado; // "PENDIENTE", "PAGADO", "CANCELADO"
}
