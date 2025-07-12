package com.example.tiendaonline.notificacion;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class ServicioNotificacion {
    private boolean conectadoAlSistemaDeMensajeria;

    public ServicioNotificacion() {
        System.out.println(" [ServicioNotificacion]: Constructor invocado.");
    }

    @PostConstruct
    public void conectar() {
        System.out.println(" [ServicioNotificacion]: Hook @PostConstruct - Estableciendo conexión con sistema de mensajería");
        this.conectadoAlSistemaDeMensajeria = true;
        System.out.println(" [ServicioNotificacion]: Conexion establecida");
    }

    public void enviarConfirmacionPedidos(String producto, int cantidad) {
        if (conectadoAlSistemaDeMensajeria) {
            System.out.println(" [ServicioNotificacion]: Enviando confirmacion de pedido: " + producto + " x" + cantidad);
        }
    }

    public void enviarAlertaStock(String producto) {
        if (conectadoAlSistemaDeMensajeria) {
            System.out.println(" [ServicioNotificacion]: Enviando alerta de stock bajo para: " + producto);
        }
    }

    @PreDestroy
    public void desconectar() {
        System.out.println(" [ServicioNotificacion]: Hook @PreDestroy - Cerrando conexion con sistema de mensajeria");
        this.conectadoAlSistemaDeMensajeria = false;
        System.out.println(" [ServicioNotificacion]: Conexion cerrada");
    }
}
