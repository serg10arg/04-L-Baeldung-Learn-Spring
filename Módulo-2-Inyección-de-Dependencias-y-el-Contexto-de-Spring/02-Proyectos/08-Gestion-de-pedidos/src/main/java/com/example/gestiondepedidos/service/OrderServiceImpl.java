package com.example.gestiondepedidos.service;

import com.example.gestiondepedidos.interfaces.IOrderService;
import com.example.gestiondepedidos.model.Order;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa un bean de servicio que gestiona pedidos
 * Implementa ApplicationContextAware para acceder al contexto y
 * utiliza @PostConstruct y @PreDestroy para callbacks de ciclo de vida.
 */
@Service // Marca esta clase como un bean de servicio de Spring
public class OrderServiceImpl implements IOrderService, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);
    private ApplicationContext applicationContext; // Referencia para almacenar el contexto

    // Mapa simple para simular una base de datos de pedidos
    private final Map<Long, Order> orders = new HashMap<>();
    private Long nextOrderId = 1L;

    /**
     * Este método es llamado por Spring cuando el bean es inicializado
     * Permite acceder al ApplicationContext actual.
     * Concepto: Obtención de referencia al contexto.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        LOG.info("OrderServiceImpl: Contexto Spring con ID '{}' establecido.", applicationContext.getId());
    }

    /**
     * Este método se ejecuta DESPUES de que el bean ha sido instanciado e inicializado.
     * Útil para realizar configuraciones iniciales o cargar datos.
     * Concepto: Ciclo de vida de los Beans (@PostConstruct).
     */
    @PostConstruct
    public void init() {
        LOG.info("OrderServiceImpl: ¡Bean inicializado! (@PostConstruct).");
        // Pre-cargar algunos pedidos de ejemplo
        orders.put(nextOrderId, new Order(nextOrderId++, "Alice Smith", 150.0, "COMPLETED"));
        orders.put(nextOrderId, new Order(nextOrderId++, "Sergio Cabrera", 299.00, "PENDING"));
    }

    /**
     * Este método se ejecuta ANTES de que el bean sea destruido.
     * Útil para liberar recursos, cerrar conexiones, etc.
     * Concepto: Ciclo de vida de los beans (@PreDestroy)
     */
    @PreDestroy
    public void destroy() {
        LOG.info("OrderServiceImpl: ¡Bean destruido! (@PreDestroy).");
        orders.clear(); // Limpiar el "repositorio" de pedidos
    }

    @Override
    public Order createOrder(String customerName, double totalAmount) {
        Long newOrderId = nextOrderId++;
        Order newOrder = new Order(newOrderId, customerName, totalAmount, "NEW");
        orders.put(newOrderId, newOrder);
        return newOrder;
    }

    @Override
    public Order getOrderById(Long id) {
        LOG.info("Buscando pedido con ID: {}", id);
        return orders.get(id);
    }
}
