package com.example.ecommerceproject.interfaz;

import com.example.ecommerceproject.modelo.ShoppingCart;

// Interfaz del servicio de carrito de compras (operando sobre un objeto ShoppingCart)
public interface ShoppingCartService {
    /**
     * Añade un producto con una cantidad específica al carrito de compras.
     */
    void addProduct(ShoppingCart cart, String productId, int quantity);

    /**
     * Calcula el precio total de todos los ítems en el carrito.
     */
    double calculateTotal(ShoppingCart cart);
}