package com.example.p9.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.example.p9.interfaz.IServicioDeInventario;

@Service
public class ServicioDeInventarioImpl implements IServicioDeInventario {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioDeInventarioImpl.class);

    @Override
    public void actualizarStock(String nombreProducto, int cantidad) {
        LOG.info("Servicio Inventario: Actualizando stock para '{}' con {} unidades.", nombreProducto, cantidad);
    }
}
