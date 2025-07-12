package com.example.gestiondepedidos;

import com.example.gestiondepedidos.interfaces.IOrderService;
import com.example.gestiondepedidos.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Clase principal para demostrar la creación y gestión de un ApplicationContext
 * de forma programática
 */

public class EcommerceApp {

    private static final Logger LOG = LoggerFactory.getLogger(EcommerceApp.class);

    public static void main(String[] args) {
        // --- 1. Crear un nuevo ApplicationContext (programáticamente) ---
        LOG.info("Intentando crear un nuevo ApplicationContext...");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        LOG.info("Contexto creado con ID '{}'", context.getId());
        LOG.info("Contexto activo antes de configurar el escaneo: {}", context.isActive());

        // --- 2. Configurar el Contexto para Escanear Beans ---
        // Decimos al contexto dónde buscar los beans (nuestro OrderServiceImpl)
        // Concepto: Configuración del Contexto y Escaneo de Beans.
        context.scan("com.example.gestiondepedidos.service"); // Escanear el paquete donde esta OrderServiceImpl
        context.refresh(); // Importante refrescar el contexto después de configurar el escaneo
                           // (Esto no está explícitamente en las fuentes para scan(),
                           // pero es una practica estándar para que el contexto procese
                           // las definiciones de beans después de un scan programático).
        LOG.info("Contexto configurado para escanear com.example.gestiondepedidos.service ");
        LOG.info("Contexto activo después de configurar el escaneo: {}" ,context.isActive());


        // --- 3. Recuperar y Usar el Bean ---
        // Concepto: Beans (Recuperación)
        LOG.info("Intentando recuperar el bean OrderService...");
        try {
            IOrderService orderService = context.getBean("orderServiceImpl", IOrderService.class);
            LOG.info("Bean OrderService recuperado con exito: {}", orderService.getClass().getName());

            // Usar el Servicio
            LOG.info("Utilizando el OrderService...");
            Order newOrder = orderService.createOrder("Juan Perez", 75.50);
            Order retrieveOrder = orderService.getOrderById(1L);
            LOG.info("Pedido recuperado por ID (1L): {}", retrieveOrder);
        } catch (Exception e) {
            LOG.error("Error al recuperar el bean o usar el bean OrderService: {}", e.getMessage());
        } finally {
            // --- 4. Cerrar el Contexto ---
            // Concepto: Ciclo de vida de los beans (Cierre del Contexto).
            LOG.info("Contexto activo antes de cerrar {}", context.isActive());
            LOG.info("Cerrando el ApplicationContext...");
            context.close(); // Cierra el contexto, lo que dispara @PreDestroy del bean
            LOG.info("Contexto activo después de cerrar: {}", context.isActive());
            LOG.info("Aplicación finalizada. Deberías ver el mensaje @PreDestroy");
        }
    }

}
