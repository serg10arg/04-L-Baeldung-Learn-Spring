package com.example.notificaciones.configuracion;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity; //
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity; //
import org.springframework.security.config.web.server.ServerHttpSecurity; //
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService; //
import org.springframework.security.core.userdetails.User; //
import org.springframework.security.core.userdetails.UserDetails; //
import org.springframework.security.web.server.SecurityWebFilterChain; //
import static org.springframework.security.config.Customizer.withDefaults; //

/**
 * Configuración de seguridad para la aplicación Spring WebFlux.
 * Habilita la seguridad web reactiva y la seguridad a nivel de método con SpEL.
 */
@Configuration // Marca la clase como una fuente de definiciones de beans [concepto_Spring]
@EnableWebFluxSecurity // Habilita la seguridad para aplicaciones Spring WebFlux
@EnableReactiveMethodSecurity // Habilita la seguridad a nivel de método reactiva (para @PreAuthorize)
public class ConfiguracionSeguridadWeb {

    /**
     * Define una cadena de filtros de seguridad web.
     * Aquí se configuran las reglas de autorización para las peticiones HTTP y WebSockets.
     *
     * @param http Objeto ServerHttpSecurity para configurar la seguridad.
     * @return La cadena de filtros de seguridad configurada.
     * @throws Exception Si hay un error en la configuración.
     */
    @Bean // Marca el método para que su valor de retorno sea un bean gestionado por Spring [concepto_Spring]
    public SecurityWebFilterChain cadenaFiltrosSeguridadWeb(ServerHttpSecurity http) throws Exception {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Deshabilita CSRF por simplicidad en un ejemplo de API. Considerar habilitar y configurar en producción para protección.
                .authorizeExchange(intercambios -> intercambios
                        // Permitir acceso sin autenticación a la ruta de eventos entrantes.
                        // En un entorno real, esta ruta podría ser asegurada por un API Gateway o token específico de servicio a servicio.
                        .pathMatchers("/api/notificaciones/eventos").permitAll() //
                        // Asegurar el endpoint WebSocket, se requiere autenticación para conectarse
                        .pathMatchers("/ws/notificaciones/**").authenticated() //
                        // Cualquier otra petición requiere autenticación
                        .anyExchange().authenticated()
                )
                .httpBasic(withDefaults()) // Habilita la autenticación HTTP Basic por defecto
                .formLogin(withDefaults()) // Habilita la autenticación basada en formularios por defecto
                .build(); // Construye la cadena de filtros de seguridad
    }

    /**
     * Configura un servicio de detalles de usuario en memoria para propósitos de demostración.
     * En una aplicación real, se usaría una base de datos, un servicio de directorio (LDAP), o un proveedor de identidad (OAuth2/OIDC).
     *
     * @return Una instancia de MapReactiveUserDetailsService con usuarios de ejemplo.
     */
    @Bean //
    public MapReactiveUserDetailsService servicioDetallesUsuario() {
        // Crear usuarios de ejemplo con roles.
        // User.withDefaultPasswordEncoder() es solo para desarrollo/ejemplo y NO debe usarse en producción.
        UserDetails usuario = User.withDefaultPasswordEncoder()
                .username("usuario")
                .password("password")
                .roles("USER") // Asigna el rol "USER"
                .build();
        UserDetails administrador = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN", "USER") // Asigna los roles "ADMIN" y "USER"
                .build();
        return new MapReactiveUserDetailsService(usuario, administrador);
    }
}

