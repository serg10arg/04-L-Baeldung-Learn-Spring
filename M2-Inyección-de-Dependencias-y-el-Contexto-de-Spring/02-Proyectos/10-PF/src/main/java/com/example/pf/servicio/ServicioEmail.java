package com.example.pf.servicio;

import com.example.pf.contrato.ServicioDeNotificacion;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("servicioEmail")
@Primary
public class ServicioEmail implements ServicioDeNotificacion {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioEmail.class);

    @PostConstruct
    public void inicializar() {
        LOG.info("ServicioEmail: @PostConstruct - Inicializando conexion al servidor de correo...");
    }

    @Override
    public void enviar(String mensaje) {
        LOG.info("ServicioEmail: Enviando correo: " + mensaje);
    }

    @PreDestroy
    public void limpiar(){
        LOG.info("ServicioEmail: @PreDestroy - Desconectando el servidor de correo...");
    }
}
