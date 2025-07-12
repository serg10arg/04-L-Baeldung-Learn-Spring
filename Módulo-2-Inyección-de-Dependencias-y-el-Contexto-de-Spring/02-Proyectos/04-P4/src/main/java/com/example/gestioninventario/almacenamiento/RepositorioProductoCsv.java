package com.example.gestioninventario.almacenamiento;

import com.example.gestioninventario.contratos.RepositorioProducto;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioProductoCsv implements RepositorioProducto {

    @Override
    public String guardar(String detallesProducto) {
        return "PRODUCTO GUARDADO (CSV): '" + detallesProducto + "'";
    }

    @Override
    public String buscarPorId(String idProducto) {
        return "PRODUCTO ENCONTRADO (CSV): '" + idProducto + "'";
    }
}
