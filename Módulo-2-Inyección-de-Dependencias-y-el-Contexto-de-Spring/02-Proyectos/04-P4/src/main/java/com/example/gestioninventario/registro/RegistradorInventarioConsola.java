package com.example.gestioninventario.registro;

import com.example.gestioninventario.contratos.RegistradorInventario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RegistradorInventarioConsola implements RegistradorInventario {

    private static final Logger logger = LoggerFactory.getLogger(RegistradorInventarioConsola.class);

    /**
     * SOLUCIÓN 1: Se ajusta el método para que devuelva un String,
     * coincidiendo con el nuevo contrato de la interfaz.
     */
    @Override
    public String guardar(String mensaje) {
        logger.info("[REGISTRO DE INVENTARIO] {}", mensaje);
        return "Mensaje registrado exitosamente en la consola."; // Devuelve un mensaje de confirmación.
    }

    /**
     * SOLUCIÓN 2: Se implementa el nuevo método 'buscarPorID' exigido por la interfaz.
     * Nota: La lógica aquí es un placeholder, ya que un logger normalmente no busca datos.
     */
    @Override
    public String buscarPorID(String id) {
        String mensajeError = "Operación 'buscarPorID' no es aplicable para un registrador de consola.";
        logger.warn(mensajeError);
        return mensajeError;
    }
}