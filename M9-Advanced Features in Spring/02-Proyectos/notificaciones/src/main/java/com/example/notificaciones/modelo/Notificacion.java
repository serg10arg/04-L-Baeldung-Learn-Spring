package com.example.notificaciones.modelo;

import lombok.AllArgsConstructor; // No en fuentes directas, pero común para reducir boilerplate en Java. Se declara como información fuera de las fuentes.
import lombok.Data; // No en fuentes directas, pero común para reducir boilerplate en Java. Se declara como información fuera de las fuentes.
import lombok.NoArgsConstructor; // No en fuentes directas, pero común para reducir boilerplate en Java. Se declara como información fuera de las fuentes.
import org.springframework.data.annotation.Id; // (implícitamente para id de MongoDB)
import org.springframework.data.mongodb.core.mapping.Document; // (para mapeo a MongoDB)

import java.time.LocalDateTime;

/**
 * Clase de modelo que representa una notificación a ser almacenada en MongoDB.
 * Utiliza Lombok para reducir el boilerplate de getters, setters, constructores, etc.
 * @note La anotación @Document de Spring Data MongoDB es esencial para mapear esta clase a una colección de MongoDB.
 */
@Data // Genera getters, setters, toString, equals, hashCode [información externa a las fuentes]
@NoArgsConstructor // Genera un constructor sin argumentos [información externa a las fuentes]
@AllArgsConstructor // Genera un constructor con todos los argumentos [información externa a las fuentes]
@Document(collection = "notificaciones") // Mapea esta clase a la colección "notificaciones" en MongoDB
public class Notificacion {

    @Id // Marca el campo como el identificador primario del documento MongoDB
    private String id;

    private String identificadorUsuarioDestino; // ID del usuario al que va dirigida la notificación
    private String tipo; // Tipo de evento que generó la notificación (ej. "NUEVA_TAREA", "CAMBIO_ESTADO_PROYECTO")
    private String mensaje; // Contenido textual de la notificación
    private LocalDateTime fechaCreacion; // Fecha y hora de creación de la notificación
    private boolean leida; // Indica si la notificación ha sido leída por el usuario [valor_interno_del_proyecto]

    /**
     * Constructor para crear una nueva notificación.
     * El 'id' se generará automáticamente por MongoDB.
     *
     * @param identificadorUsuarioDestino El ID del usuario que debe recibir la notificación.
     * @param tipo El tipo de evento que desencadenó la notificación.
     * @param mensaje El mensaje de la notificación.
     */
    public Notificacion(String identificadorUsuarioDestino, String tipo, String mensaje) {
        this.identificadorUsuarioDestino = identificadorUsuarioDestino;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fechaCreacion = LocalDateTime.now(); // Establece la fecha de creación actual
        this.leida = false; // Por defecto, la notificación no está leída [valor_interno_del_proyecto]
    }
}
