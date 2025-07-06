package com.example.ecommerceproject.modelo;

import java.util.ArrayList;
import java.util.List;

// Esta clase representa un carrito de compras de un usuario.
public class ShoppingCart {

    private final List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        //Logica para manejar duplicados: si el producto ya existe, incrementa cantidad.
        for (CartItem currentItem : items) {
            if (currentItem.getProduct().getId().equals(item.getProduct().getId())) {
                currentItem.setQuantity(currentItem.getQuantity() + item.getQuantity());
                return; // Producto actualizado, no añadir nuevo.
            }
        }
        items.add(item);
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items); // Devuelve una copia para proteger la lista interna
    }

    public double calculateTotal() {
        return items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
}
