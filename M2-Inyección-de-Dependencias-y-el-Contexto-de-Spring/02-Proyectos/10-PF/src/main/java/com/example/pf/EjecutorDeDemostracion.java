package com.example.pf;

import com.example.pf.componente.EmisorDeNotificaciones;
import com.example.pf.contrato.ServicioDeNotificacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class EjecutorDeDemostracion implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(EjecutorDeDemostracion.class);
    private final EmisorDeNotificaciones emisor;
    private final ApplicationContext contexto;

    public EjecutorDeDemostracion(EmisorDeNotificaciones emisor, ApplicationContext contexto) {
        this.emisor = emisor;
        this.contexto = contexto;
    }

    @Override
    public void run(String ... args) throws Exception {
        // 1. Demostracion de inyeccion por defecto
        LOG.info("\n[1. Probando inyección por defecto (@Primary)]");
        emisor.enviarNotificacionPrincipal("Este es un mensaje de bienvenida");

        // 2. Demostracion de inyeccion explicita (@Qualifier)
        LOG.info("\n[2. Probando inyección explicita (@Qualifier)]");
        emisor.enviarNotificacionSecundaria("Este es un aviso urgente por SMS");

        // 3. Demostracion de scope 'prototype'
        LOG.info("\n[3. Probando el scope 'prototype']");
        LOG.info("Solicitando la primera instancia de ServicioSMS...");
        ServicioDeNotificacion sms1 = contexto.getBean("servicioSMS", ServicioDeNotificacion.class);
        sms1.enviar("Primer mensaje de prueba");

        LOG.info("Solicitando la segunda instancia de ServicioSMS...");
        ServicioDeNotificacion sms2 = contexto.getBean("servicioSMS", ServicioDeNotificacion.class);
        sms2.enviar("Segunda mensaje de prueba");

        LOG.info("¿Son las dos instancias de SMS el mismos objeto? --> {}", (sms1 == sms2));
        LOG.info("\n--- FIN DE LA DEMOSTRACIÓN ---");    }
}
