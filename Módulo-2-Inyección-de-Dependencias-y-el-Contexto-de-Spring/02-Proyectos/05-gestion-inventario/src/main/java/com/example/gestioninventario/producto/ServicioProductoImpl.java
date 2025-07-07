package com.example.gestioninventario.producto;

import com.example.gestioninventario.contratos.RegistradorInventario;
import com.example.gestioninventario.contratos.RepositorioProducto;
import com.example.gestioninventario.contratos.ServicioProducto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicioProductoImpl implements ServicioProducto {

    private final RepositorioProducto repositorioProducto;
    private final Optional<RegistradorInventario> registradorOpcional;

    /**
     * Inyeccion por constructor, el metodo preferido para dependencias.
     * @param repositorioProducto Dependencia obligatoria, seleccionada explicitamente con @Qualifier.
     * @param registradorOpcional Dependencia opcional. Spring inyectara un Optional.empty() si no encuentra bean
     */
    // SUGGESTION: Use standard camelCase for the bean name in @Qualifier.
    public ServicioProductoImpl(@Qualifier("repositorioProductoJpa") RepositorioProducto repositorioProducto,
                                Optional<RegistradorInventario> registradorOpcional) {
        this.repositorioProducto = repositorioProducto;
        this.registradorOpcional = registradorOpcional;
    }

    @Override
    public String agregarProducto(String detallesProducto) {
        String resultado = repositorioProducto.guardar(detallesProducto);
        // Usamos el estilo funcional de Optional para registrar el evento si el logger esta presente.
        // FIX: Changed method call from 'registrar' to 'guardar' to match the interface.
        registradorOpcional.ifPresent(registrador -> registrador.guardar("Producto añadido: " + detallesProducto));
        return resultado;
    }

    @Override
    public String obtenerProducto(String idProducto) {
        String resultado = repositorioProducto.buscarPorId(idProducto);
        // FIX: Changed method call from 'registrar' to 'guardar' to match the interface.
        registradorOpcional.ifPresent(registrador -> registrador.guardar("Busqueda de producto: " + idProducto));
        return resultado;
    }
}