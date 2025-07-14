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

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AplicacionGestorTareas.class)
@ActiveProfiles("prod") // Esta clase SIEMPRE se ejecutará con el perfil 'prod'
public class ControladorTareasProdIntegracionTest {

    @Autowired
    private WebApplicationContext contextoAplicacionWeb;

    private MockMvc mockMvc;

    @BeforeEach
    public void configurarPrueba() {
        mockMvc = MockMvcBuilders.webAppContextSetup(contextoAplicacionWeb).build();
        assertNotNull(contextoAplicacionWeb, "El contexto de aplicación web no debería ser nulo");
    }

    @Test
    public void cuandoObtenerConfiguracion_conPerfilProd_entoncesDevuelveConfiguracionProd() throws Exception {
        String configuracionEsperada = "Prefijo: PROD_ID_APP, Sufijo: PROD_ENV";

        mockMvc.perform(get("/tarea/configuracion"))
                .andExpect(status().isOk())
                // MEJORA: Se usa equalTo para una aserción más estricta y precisa.
                .andExpect(content().string(equalTo(configuracionEsperada)));
    }
}