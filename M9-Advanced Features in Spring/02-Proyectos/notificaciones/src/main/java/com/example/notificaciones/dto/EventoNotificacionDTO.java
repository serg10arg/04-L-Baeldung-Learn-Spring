package com.example.notificaciones.dto;

import lombok.AllArgsConstructor; // No en fuentes directas, pero común para reducir boilerplate en Java. Se declara como información fuera de las fuentes.
import lombok.Data; // No en fuentes directas, pero común para reducir boilerplate en Java. Se declara como información fuera de las fuentes.
import lombok.NoArgsConstructor; // No en fuentes directas, pero común para reducir boilerplate en Java. Se declara como información fuera de las fuentes.

/**
 * DTO (Data Transfer Object) para la recepción de eventos de notificación.
 * Representa la estructura de los datos que se recibirán de otros sistemas.
 * Utiliza Lombok para reducir el boilerplate.
 */
@Data // Genera getters, setters, toString, equals, hashCode [información externa a las fuentes]
@NoArgsConstructor // Genera un constructor sin argumentos [información externa a las fuentes]
@AllArgsConstructor // Genera un constructor con todos los argumentos [información externa a las fuentes]
public class EventoNotificacionDTO {
    private String identificadorUsuarioDestino; // El ID del usuario receptor.
    private String tipo; // El tipo de evento (ej. "NUEVA_TAREA").
    private String mensaje; // El contenido o mensaje del evento.
    private String origen; // El sistema que originó el evento (ej. "PROYECTOS", "TAREAS"). [información_adicional_del_proyecto]
}
