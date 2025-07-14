package com.example.gestortareas.controlador;

import com.example.gestortareas.AplicacionGestorTareas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class) // Habilita el soporte de Spring para JUnit 5
@SpringBootTest(classes = AplicacionGestorTareas.class) // Carga el contexto completo de Spring Boot
@ActiveProfiles("dev") // Esta clase SIEMPRE se ejecutará con el perfil 'dev'
public class ControladorTareasDevIntegracionTest {

    @Autowired
    private WebApplicationContext contextoAplicacionWeb; // Inyecta el contexto web de la aplicación

    private MockMvc mockMvc; // Objeto MockMvc para simular peticiones HTTP

    @BeforeEach // Se ejecuta antes de cada método de prueba
    public void configurarPrueba() {
        mockMvc = MockMvcBuilders.webAppContextSetup(contextoAplicacionWeb).build(); // Inicializa MockMvc
        assertNotNull(contextoAplicacionWeb, "El contexto de aplicación web no debería ser nulo"); // Verifica que el contexto se cargó
    }

    @Test
    public void cuandoGenerarIdTarea_entoncesDevuelveIdConPrefijoDev() throws Exception {
        String nombreTarea = "NuevaTarea";
        // Realiza una petición GET al endpoint y verifica la respuesta
        String idEsperado = "DEV_ID-" + nombreTarea.toLowerCase().replace(" ", "") + "-ENTORNO_DEV";

        mockMvc.perform(get("/tarea/generar-id/{nombre}", nombreTarea))
                .andExpect(status().isOk()) // Espera un estado HTTP 200 OK
                // MEJORA: Se usa equalTo para una aserción más estricta y precisa.
                .andExpect(content().string(equalTo(idEsperado))); // // Verifica el contenido con el prefijo DEV
    }
}