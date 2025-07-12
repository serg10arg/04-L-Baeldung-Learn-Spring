package com.example.pf.servicio;

import com.example.pf.contrato.ServicioDeNotificacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service("servicioSMS")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ServicioSMS implements  ServicioDeNotificacion {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioSMS.class);
    private static final AtomicInteger contadorDeInstancias = new AtomicInteger(0);
    private final int idDeInstancia;

    public ServicioSMS() {
        this.idDeInstancia = contadorDeInstancias.incrementAndGet();
        LOG.info("Creando instancia de ServicioSMS #{}", this.idDeInstancia);
    }

    @Override
    public void enviar(String mensaje) {
        LOG.info("(Instancia SMS #{}) Enviando SMS: {}", this.idDeInstancia, mensaje);    }
}
