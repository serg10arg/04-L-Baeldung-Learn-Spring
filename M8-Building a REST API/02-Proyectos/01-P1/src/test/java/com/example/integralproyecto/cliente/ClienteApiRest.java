package com.example.integralproyecto.cliente;

// 1. Imports de DTOs (Correcto)
import com.example.integralproyecto.dto.proyecto.ProyectoCrearDTO;
import com.example.integralproyecto.dto.proyecto.ProyectoDetalleDTO;
import com.example.integralproyecto.dto.proyecto.ProyectoResumenDTO;

// 2. Imports de JUnit 5 y Spring (Correcto)
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

// 3. CORRECCIÓN: Eliminar los imports conflictivos y dejar solo el de JUnit 5
import static org.junit.jupiter.api.Assertions.*;


/**
 * Clase de prueba actualizada para demostrar el consumo de la API REST moderna.
 * Utiliza DTOs especializados para cada operación, reflejando el contrato actual de la API.
 */
public class ClienteApiRest {

    private static final String URL_BASE_API = "http://localhost:8080/api/";
    private static RestTemplate restTemplate;

    @BeforeAll
    static void configurar() {
        restTemplate = new RestTemplate();
    }

    @Test
    void pruebasCompletasDeApi() {
        System.out.println("\n--- INICIANDO PRUEBAS DE CLIENTE API REST ---");

        // --- Pruebas de Proyectos ---
        System.out.println("\n--- PROYECTOS ---");
        ProyectoDetalleDTO proyecto1 = crearNuevoProyecto(new ProyectoCrearDTO("Proyecto Web", "Desarrollo de una aplicación web"));
        ProyectoDetalleDTO proyecto2 = crearNuevoProyecto(new ProyectoCrearDTO("Proyecto Móvil", "Desarrollo de una aplicación móvil"));

        obtenerTodosLosProyectos();
        obtenerProyectoPorId(proyecto1.getId());

        // ... (El resto de las pruebas se adaptarían de forma similar) ...

        System.out.println("\n--- PRUEBAS DE CLIENTE API REST FINALIZADAS ---");
    }

    // --- Métodos de Ayuda para Proyectos (Refactorizados) ---

    ProyectoDetalleDTO crearNuevoProyecto(ProyectoCrearDTO proyectoCrearDTO) {
        System.out.println("\nCreando proyecto: " + proyectoCrearDTO.getNombre());
        try {
            ResponseEntity<ProyectoDetalleDTO> respuesta = restTemplate.postForEntity(
                    URL_BASE_API + "proyectos", proyectoCrearDTO, ProyectoDetalleDTO.class);

            // 4. Ahora todas las aserciones provienen de la misma fuente (JUnit 5) y funcionarán
            assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
            assertNotNull(respuesta.getBody());
            assertNotNull(respuesta.getBody().getId());
            System.out.println("Proyecto creado: " + respuesta.getBody());
            return respuesta.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error al crear proyecto: " + e.getResponseBodyAsString());
            fail("Falló la creación del proyecto: " + e.getMessage());
            return null;
        }
    }

    void obtenerTodosLosProyectos() {
        System.out.println("\nObteniendo todos los proyectos...");
        try {
            ResponseEntity<List<ProyectoResumenDTO>> respuesta = restTemplate.exchange(
                    URL_BASE_API + "proyectos",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ProyectoResumenDTO>>() {});

            assertEquals(HttpStatus.OK, respuesta.getStatusCode());
            assertNotNull(respuesta.getBody());
            System.out.println("Proyectos obtenidos (" + respuesta.getBody().size() + "): " + respuesta.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error al obtener todos los proyectos: " + e.getResponseBodyAsString());
            fail("Falló la obtención de todos los proyectos: " + e.getMessage());
        }
    }

    void obtenerProyectoPorId(Long id) {
        System.out.println("\nObteniendo proyecto con ID: " + id);
        try {
            ResponseEntity<ProyectoDetalleDTO> respuesta = restTemplate.getForEntity(
                    URL_BASE_API + "proyectos/" + id, ProyectoDetalleDTO.class);
            assertEquals(HttpStatus.OK, respuesta.getStatusCode());
            assertNotNull(respuesta.getBody());
            assertEquals(id, respuesta.getBody().getId());
            System.out.println("Proyecto obtenido: " + respuesta.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("Error al obtener proyecto con ID " + id + ": " + e.getResponseBodyAsString());
            fail("Falló la obtención del proyecto: " + e.getMessage());
        }
    }

    // ... (Los demás métodos de ayuda se refactorizarían de manera similar) ...
}