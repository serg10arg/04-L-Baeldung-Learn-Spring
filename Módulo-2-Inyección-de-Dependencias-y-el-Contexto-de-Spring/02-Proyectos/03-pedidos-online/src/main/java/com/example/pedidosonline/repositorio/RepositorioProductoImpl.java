package com.example.pedidosonline.repositorio;

import com.example.pedidosonline.interfaces.RepositorioProducto;
import com.example.pedidosonline.modelo.Producto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RepositorioProductoImpl implements RepositorioProducto {

    private final ConcurrentHashMap<String, Producto> baseDeDatosSimulada = new ConcurrentHashMap<>();

    public RepositorioProductoImpl() {
        // Datos de ejemplo para simiular la base de datos
        baseDeDatosSimulada.put("P001", new Producto("P001", "Laptop Gaming", 1200.00));
        baseDeDatosSimulada.put("P002", new Producto("P002", "Teclado Mecanico", 85.50));
        baseDeDatosSimulada.put("P003", new Producto("P003", "Raton Inalambrico", 45.00));
    }

    @Override
    public List<Producto> buscarTodos(){
        return new ArrayList<>(baseDeDatosSimulada.values());
    }

    @Override
    public Optional<Producto> buscarPorId(String id) {
        return Optional.ofNullable(baseDeDatosSimulada.get(id));
    }

    @Override
    public Producto guardar(Producto producto) {
        baseDeDatosSimulada.put(producto.getId(), producto);
        return producto;
    }


}
