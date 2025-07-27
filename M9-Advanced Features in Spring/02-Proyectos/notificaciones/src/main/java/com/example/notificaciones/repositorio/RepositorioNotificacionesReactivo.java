package com.example.notificaciones.repositorio;

import com.example.notificaciones.modelo.Notificacion;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository; //
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux; //

/**
 * Repositorio reactivo para la entidad Notificacion.
 * Extiende ReactiveMongoRepository para operaciones asíncronas y no bloqueantes
 * con MongoDB, lo que es esencial para el paradigma reactivo y el manejo de backpressure.
 */
@Repository // Marca la interfaz como un componente de repositorio [concepto_Spring]
public interface RepositorioNotificacionesReactivo extends ReactiveMongoRepository<Notificacion, String> {

    /**
     * Busca notificaciones por el identificador del usuario destino.
     * Devuelve un Flux de notificaciones, que es un flujo reactivo que puede emitir 0 a N elementos.
     *
     * @param identificadorUsuario El ID del usuario.
     * @return Un Flux de Notificacion para el usuario especificado.
     */
    Flux<Notificacion> findByIdentificadorUsuarioDestino(String identificadorUsuario); //

    /**
     * Busca notificaciones por el identificador del usuario destino y estado de lectura.
     * Útil para filtrado en el historial de notificaciones.
     *
     * @param identificadorUsuario El ID del usuario.
     * @param leida Indica si la notificación ha sido leída.
     * @return Un Flux de Notificacion que coinciden con los criterios.
     */
    Flux<Notificacion> findByIdentificadorUsuarioDestinoAndLeida(String identificadorUsuario, boolean leida); //

    /**
     * Busca todas las notificaciones por estado de lectura.
     * Puede ser útil para administradores.
     *
     * @param leida Indica si la notificación ha sido leída.
     * @return Un Flux de Notificacion que coinciden con el estado de lectura.
     */
    Flux<Notificacion> findByLeida(boolean leida); //
}

