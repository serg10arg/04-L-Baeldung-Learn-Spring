package com.example.pf.contrato;

/**
 * Define el contrato que todos los servicios de notificación deben implementar.
 */
public interface ServicioDeNotificacion {

    /**
     * Envía un mensaje de notificación.
     * @param mensaje El contenido del mensaje a enviar
     */
    void enviar(String mensaje);
}
