package com.example.p9.repositorio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.example.p9.interfaz.IRepositorioDeProducto;

@Repository("repositorioProductoPrincipal")
public class RepositorioDeProductoImplPrincipal implements IRepositorioDeProducto {

    private static final Logger LOG = LoggerFactory.getLogger(RepositorioDeProductoImplPrincipal.class);

    @Override
    public void guardarProducto(String nombreProducto) {
        LOG.info("Repositorio Principal: Guardando producto -> {}", nombreProducto);
    }

}
