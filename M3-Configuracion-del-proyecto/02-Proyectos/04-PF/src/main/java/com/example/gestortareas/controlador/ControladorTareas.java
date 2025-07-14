package com.example.gestortareas.controlador;

import com.example.gestortareas.servicio.ServicioTareas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControladorTareas {
    private static final Logger LOG = LoggerFactory.getLogger(ControladorTareas.class);
    private ServicioTareas servicioTareas;

    public ControladorTareas(ServicioTareas servicioTareas) {
        this.servicioTareas = servicioTareas;
    }

    /**
     * Endpoint para generar un ID de tarea.
     * @param nombre El nombre de la tarea
     * @return El ID generado para la tarea
     */
    @GetMapping("/tarea/generar-id/{nombre}")
    public String generarId(@PathVariable String nombre) {
        LOG.info("ControladorTareas: Peticion GET recibida para generar ID para '{}'.", nombre);
       return servicioTareas.generarIdDeTarea(nombre);
    }

    @GetMapping("/tarea/configuracion")
    public String obtenerConfiguracion() {
        LOG.debug("ControladorTareas: Peticion GET recibida para obtener configuracion de ID");
        return servicioTareas.obtenerConfiguracionActual();
    }

}
