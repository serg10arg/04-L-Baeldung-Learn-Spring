package com.example.integralproyecto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc; // Importación necesaria para @EnableWebMvc

/**
 * Clase de configuración web para Spring MVC.
 * Implementa WebMvcConfigurer para personalizar la configuración MVC.
 * @EnableWebMvc es opcional en Spring Boot si se usa spring-boot-starter-web,
 * ya que Spring Boot ya auto-configura Spring MVC. Se incluye para mayor
 * claridad o para sobrescribir el comportamiento por defecto.
 */
@Configuration // Indica que esta clase contiene definiciones de beans de configuración
// @EnableWebMvc // Habilita la configuración explícita de Spring MVC. Comentado porque Spring Boot lo hace automáticamente.
public class ConfiguracionWeb implements WebMvcConfigurer {

    // Aquí se podrían agregar configuraciones personalizadas como:
    // - addCorsMappings: para configurar CORS globalmente
    // - configureMessageConverters: para añadir o modificar HttpMessageConverters
    // - addInterceptors: para registrar interceptores
    // - configureContentNegotiation: para personalizar cómo se determina el tipo de contenido
    // etc.
}
