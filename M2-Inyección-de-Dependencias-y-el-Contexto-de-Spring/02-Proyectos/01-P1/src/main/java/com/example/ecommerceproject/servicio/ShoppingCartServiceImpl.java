package com.example.ecommerceproject.servicio;

import com.example.ecommerceproject.interfaz.ProductRepository;
import com.example.ecommerceproject.interfaz.ShoppingCartService;
import com.example.ecommerceproject.modelo.CartItem;
import com.example.ecommerceproject.modelo.ShoppingCart;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ProductRepository productRepository;

    // Inyeccion de Dependencias via Contructor
    public ShoppingCartServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Los metodo ahora reciben el carrito sobre el que deben operar
    @Override
    public void addProduct(ShoppingCart cart, String productId, int quantity) {
        productRepository.findById(productId).ifPresentOrElse(
                product -> {
                    cart.addItem(new CartItem(product, quantity));
                    System.out.println("Añadiendo al carrito: " + product.getName() + "(Cantidad: " + quantity + ")");
                },
                () -> System.out.println("Producto con ID " + productId + " no encontrado")
        );
    }

    @Override
    public double calculateTotal(ShoppingCart cart){
        return cart.calculateTotal();
    }
}
