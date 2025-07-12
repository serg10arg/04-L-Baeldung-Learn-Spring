package com.example.ecommerceproject.interfaz;

import com.example.ecommerceproject.modelo.Product;

import java.util.List;
import java.util.Optional;

// Interfaz que define las operaciones del repositorio de productos
public interface ProductRepository {
    Optional<Product> findById(String id);
    List<Product> findAll();
}
