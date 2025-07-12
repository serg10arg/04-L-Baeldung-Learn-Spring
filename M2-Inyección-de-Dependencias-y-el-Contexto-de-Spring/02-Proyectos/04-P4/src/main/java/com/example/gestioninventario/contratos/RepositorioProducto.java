package com.example.gestioninventario.contratos;

public interface RepositorioProducto {
    String guardar(String detallesProducto);
    String buscarPorId(String idProducto);
}
