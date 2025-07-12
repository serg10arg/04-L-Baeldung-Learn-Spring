package com.example.pf.componente;

import com.example.pf.contrato.ServicioDeNotificacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class EmisorDeNotificaciones {

    private static final Logger LOG = LoggerFactory.getLogger(EmisorDeNotificaciones.class);
    private final ServicioDeNotificacion servicioPrincipal;
    private final ServicioDeNotificacion servicioSecundario;

    public EmisorDeNotificaciones(ServicioDeNotificacion servicioPrincipal,
                                  @Qualifier("servicioSMS") ServicioDeNotificacion servicioSecundario) {
        this.servicioPrincipal = servicioPrincipal;
        this.servicioSecundario = servicioSecundario;
        LOG.info("Creando instancia de EmisorDeNotificaciones");

    }

    public void enviarNotificacionPrincipal(String mensaje) {
        LOG.info("Usando servicio principal (@Primary: {})", servicioPrincipal.getClass().getSimpleName());
        servicioPrincipal.enviar(mensaje);
    }

    public void enviarNotificacionSecundaria(String mensaje) {
        LOG.info("Usando servicio secundario (@Qualifier: {})", servicioSecundario.getClass().getSimpleName());
        servicioSecundario.enviar(mensaje);
    }
}
