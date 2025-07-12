package com.example.tiendaonline.inventario;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.util.HashMap;
import java.util.Map;

public class GestorInventario {
    private Map<String, Integer> stock;

    public GestorInventario() {
        System.out.println(" [GestorInventario]: Constructor invocado. ");
    }

    @PostConstruct
    public void cargarInventarioInicial() {
        System.out.println(" [GestorInventario]: Hook @PostConstruct - Cargando inventario inicial... ");
        this.stock = new HashMap<>();
        stock.put("Laptop Gamer", 5);
        stock.put("Teclado Mecanico", 15);
        stock.put("Monitor Curvo", 0);
        System.out.println(" [GestorInventario]: Inventario inicial cargado: " + stock);
    }

    public boolean verificarStock(String producto, int cantidad) {
        return stock.getOrDefault(producto, 0) >= cantidad;
    }


    public void reducirStock(String producto, int cantidad) {
        if (verificarStock(producto, cantidad)) {
            stock.put(producto, stock.get(producto) - cantidad);
            System.out.println(" [GestorInventario]: Stock de '" + producto + "' actualizado a " + stock.get(producto));
        }
    }

    @PreDestroy
    public void guardarInventarioRestante() {
        System.out.println(" [GestorInventario]: Hook @PreDestroy - Guardando inventario restante...");
        // Aqui iria la logica real para persistir el inventario (ej: una base de datos)
        System.out.println(" [GestorInventario]: Inventario final guardado: " + stock);
    }
}
