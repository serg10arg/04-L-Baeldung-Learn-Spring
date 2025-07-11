package com.example.p10.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ServicioGestionInventario {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioGestionInventario.class);
    private final ServicioAuditoria servicioAuditoria;

    // @Lazy rompe el ciclo de dependencia durante la inicialización.
    // Spring crea un proxy y solo instancia el bean real cuando se usa por primera vez.
    public ServicioGestionInventario(@Lazy ServicioAuditoria servicioAuditoria) {
        this.servicioAuditoria = servicioAuditoria;
        LOG.info("ServicioGestionInventario inicializado (proxy para ServicioAuditoria).");
    }

    public void verificarInventario(Long idProducto) {
        LOG.info("Verificando inventario del producto {}", idProducto);
        // Lógica de verificación...
        servicioAuditoria.registrarEvento("Inventario verificado para producto " + idProducto);
    }
}