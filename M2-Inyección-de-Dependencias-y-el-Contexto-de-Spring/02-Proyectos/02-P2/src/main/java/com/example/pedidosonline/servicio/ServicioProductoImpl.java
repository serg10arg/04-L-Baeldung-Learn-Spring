package com.example.pedidosonline.servicio;

import com.example.pedidosonline.interfaces.RepositorioProducto;
import com.example.pedidosonline.interfaces.ServicioProducto;
import com.example.pedidosonline.modelo.Producto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioProductoImpl implements ServicioProducto {

    private final RepositorioProducto repositorioProducto;

    //Inyeccion de dependencia de RepositorioProducto via constructor
    public ServicioProductoImpl(RepositorioProducto repositorioProducto) {
        this.repositorioProducto = repositorioProducto;
    }

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return repositorioProducto.buscarTodos();
    }

    @Override
    public Optional<Producto> obtenerProductoPorId(String id) {
        return repositorioProducto.buscarPorId(id);
    }

    public Producto guardarProducto(Producto producto) {
        return repositorioProducto.guardar(producto);
    }
}
