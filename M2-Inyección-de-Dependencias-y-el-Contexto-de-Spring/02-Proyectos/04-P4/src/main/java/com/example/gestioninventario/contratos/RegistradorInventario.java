package com.example.gestioninventario.contratos;

public interface RegistradorInventario {
    String guardar(String detallesProducto);
    String buscarPorID(String idProducto);

}
