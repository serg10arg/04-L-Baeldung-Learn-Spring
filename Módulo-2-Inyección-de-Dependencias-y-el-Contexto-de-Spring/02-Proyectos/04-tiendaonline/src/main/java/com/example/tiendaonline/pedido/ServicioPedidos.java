package com.example.tiendaonline.pedido;

import com.example.tiendaonline.inventario.GestorInventario;
import com.example.tiendaonline.notificacion.ServicioNotificacion;

public class ServicioPedidos {

    private final GestorInventario gestorInventario;
    private final ServicioNotificacion servicioNotificacion;

    public ServicioPedidos(GestorInventario gestorInventario, ServicioNotificacion servicioNotificacion) {
        this.gestorInventario = gestorInventario;
        this.servicioNotificacion = servicioNotificacion;
    }

    public void realizarPedidos(String producto, int cantidad) {
        System.out.println(" [ServicioPedidps]: Intentando procesar pedido de " + cantidad + " x " + producto + "...");
        if (gestorInventario.verificarStock(producto, cantidad)) {
            gestorInventario.reducirStock(producto, cantidad);
            servicioNotificacion.enviarConfirmacionPedidos(producto, cantidad);
            System.out.println(" [ServicioPedidos]: Pedido de '" + producto + "' procesando exitosamente.");
        } else {
            System.out.println(" [ServicioPedidos]: ¡ERROR! No hay suficiente stock para '" + producto + "'. Pedido fallido.");
            servicioNotificacion.enviarAlertaStock(producto);
        }
    }
}
