package com.example.p9.repositorio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.example.p9.interfaz.IRepositorioDeProducto;

@Repository("repositorioProductoSecundario")
public class RepositorioDeProductoImplSecundario implements IRepositorioDeProducto {

    private static final Logger LOG = LoggerFactory.getLogger(RepositorioDeProductoImplSecundario.class);

    @Override
    public void guardarProducto(String nombreProducto) {
        LOG.info("Repositorio Secundario: Guardando producto secundario --> {}", nombreProducto);
    }
}
