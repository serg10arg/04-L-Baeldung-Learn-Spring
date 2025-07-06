package com.example.ecommerceproject;

import com.example.ecommerceproject.modelo.ShoppingCart;
import com.example.ecommerceproject.interfaz.ShoppingCartService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component // Spring detectará y ejecutará este componente al arrancar
public class AppRunner implements CommandLineRunner {

    private final ShoppingCartService cartService;

    // Inyectamos el servicio que necesitamos
    public AppRunner(ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n--- Sistema de Carrito de Compras (Demo) ---");

        // Creamos una instancia del carrito para esta "sesión"
        ShoppingCart myCart = new ShoppingCart();

        // Usamos el servicio para operar sobre nuestro carrito
        cartService.addProduct(myCart, "P001", 1); // Añadir Laptop
        cartService.addProduct(myCart, "P002", 2); // Añadir 2 Mouses
        cartService.addProduct(myCart, "P001", 1); // Añadir otra Laptop (actualizará la cantidad)
        cartService.addProduct(myCart, "P999", 1); // Producto no existente

        System.out.println("\n--- Resumen del Carrito ---");
        myCart.getItems().forEach(item ->
                System.out.println(
                        String.format("- %s (Cantidad: %d) - Subtotal: $%.2f",
                                item.getProduct().getName(),
                                item.getQuantity(),
                                item.getTotalPrice()
                        )
                )
        );

        System.out.println("\nTOTAL DEL CARRITO: $" + String.format("%.2f", cartService.calculateTotal(myCart)));
        System.out.println("---------------------------\n");
    }
}