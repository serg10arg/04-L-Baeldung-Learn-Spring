package com.example.tiendaonline.config;

import com.example.tiendaonline.inventario.GestorInventario;
import com.example.tiendaonline.notificacion.ServicioNotificacion;
import com.example.tiendaonline.pedido.ServicioPedidos;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionApp {

    @Bean
    public ServicioPedidos servicioPedidos(GestorInventario gestorInventario, ServicioNotificacion servicioNotificacion) {
        return new ServicioPedidos(gestorInventario, servicioNotificacion);
    }

    @Bean
    public GestorInventario gestorInventario() {
        return new GestorInventario();
    }

    @Bean
    public ServicioNotificacion servicioNotificacion() {
        return new ServicioNotificacion();
    }
}
