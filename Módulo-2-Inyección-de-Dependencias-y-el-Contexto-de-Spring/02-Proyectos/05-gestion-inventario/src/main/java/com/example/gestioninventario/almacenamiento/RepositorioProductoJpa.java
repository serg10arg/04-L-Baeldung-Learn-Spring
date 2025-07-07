package com.example.gestioninventario.almacenamiento;

import com.example.gestioninventario.contratos.RepositorioProducto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary // Marca este bean como la implementación por defecto para RepositorioProducto.
public class RepositorioProductoJpa implements RepositorioProducto {

    @Override
    public String guardar(String detallesProducto) {
        return "PRODUCTO GUARDADO (JPA): '" + detallesProducto +  "'";
    }

    @Override
    public String buscarPorId(String idProducto) {
        return "PRODUCTO ENCONTRADO (JPA): '" + idProducto +  "'";
    }
}
