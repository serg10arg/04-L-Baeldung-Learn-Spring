package com.example.p10.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ServicioAuditoria {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioAuditoria.class);

    // En un diseño ideal, este servicio no dependería de otros servicios de negocio.
    // Se mantiene la dependencia circular con ServicioGestionInventario solo para demostrar @Lazy.
    private final ServicioGestionInventario servicioInventario;

    public ServicioAuditoria(@Lazy ServicioGestionInventario servicioInventario) {
        this.servicioInventario = servicioInventario;
        LOG.info("ServicioAuditoria inicializado (proxy para ServicioGestionInventario).");
    }

    public void registrarEvento(String evento) {
        LOG.warn("AUDITORIA: {}", evento);
    }
}