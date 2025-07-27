package com.example.notificaciones.configuracion;

import com.example.notificaciones.manejador.ManejadorWebSocketNotificaciones;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Map;

/**
 * Configuración de WebSockets para el Microservicio de Notificaciones.
 * Mapea la ruta WebSocket a un manejador específico de forma idiomática para WebFlux.
 */
@Configuration
public class ConfiguracionWebSocket {

    private final ManejadorWebSocketNotificaciones manejadorWebSocketNotificaciones;

    public ConfiguracionWebSocket(ManejadorWebSocketNotificaciones manejadorWebSocketNotificaciones) {
        this.manejadorWebSocketNotificaciones = manejadorWebSocketNotificaciones;
    }

    /**
     * Define el mapeo de rutas para los manejadores WebSocket.
     * Mapea el path "/ws/notificaciones/{identificadorUsuario}" al ManejadorWebSocketNotificaciones.
     *
     * @return Un HandlerMapping que contiene los mapeos.
     */
    @Bean
    public HandlerMapping manejadorWebSocketMapping() {
        // Mapear el endpoint WebSocket. Se usará el ID de usuario como parte de la ruta para identificar la sesión.
        Map<String, WebSocketHandler> mapeoHandlers = Map.of(
                "/ws/notificaciones/{identificadorUsuario}", manejadorWebSocketNotificaciones
        );

        // SimpleUrlHandlerMapping es la forma estándar de mapear URLs a handlers en WebFlux.
        SimpleUrlHandlerMapping mapeo = new SimpleUrlHandlerMapping();
        mapeo.setUrlMap(mapeoHandlers);
        // Orden alto para que este mapeo se verifique antes que otros mapeos de controladores REST.
        mapeo.setOrder(-1);
        return mapeo;
    }

    /**
     * Bean para adaptar los manejadores WebSocket.
     * Esencial para que Spring WebFlux procese las conexiones WebSocket.
     *
     * @return Un WebSocketHandlerAdapter.
     */
    @Bean
    public WebSocketHandlerAdapter manejadorWebSocketAdapter() {
        return new WebSocketHandlerAdapter();
    }
}