package com.example.gestortareas.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServicioTareas {
    private static final Logger LOG = LoggerFactory.getLogger(ServicioTareas.class);

    @Value("${tarea.id.prefijo}") // Inyección de propiedad
    private String prefijoIdTarea;

    @Value("${tarea.id.sufijo}") // Inyección de propiedad
    private String sufijoIdTarea;

    /**
     * Genera un ID único para una tarea basada en las propiedades configuradas.
     * @param nombreTarea El nombre de la tarea.
     * @return El ID generado para la tarea
     */
    public String generarIdDeTarea(String nombreTarea) {
        String idGenerado = prefijoIdTarea + "-" + nombreTarea.toLowerCase().replace(" ", "")
                + "-" + sufijoIdTarea;
        LOG.debug("ServicioTarea: Generado ID para tarea '{}'. ID: {}", nombreTarea, idGenerado);
        return idGenerado;
    }

    /**
     * Obtiene la configuración actual de prefijo y sufijo para el ID de tarea.
     * @return Cadena con la configuración actual.
     */
    public String obtenerConfiguracionActual() {
        LOG.info("ServicioTarea: Obteniendo configuracion actual de ID."); // Log a nivel INFO
        return "Prefijo: " + prefijoIdTarea + ", Sufijo: " + sufijoIdTarea;
    }
}
