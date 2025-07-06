package com.example.ecommerceproject.repositorio;

import com.example.ecommerceproject.modelo.Product;
import com.example.ecommerceproject.interfaz.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository // Declara esta clase como un Bean de Spring
public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Product> products = new HashMap<>();

    public InMemoryProductRepository() {
        // Datos de ejemplo en memoria
        products.put("P001", new Product("P001", "Laptop", 1200.00));
        products.put("P002", new Product("P002", "Mouse", 25.00));
        products.put("P003", new Product("P003", "Tablet", 300.00));
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

}
