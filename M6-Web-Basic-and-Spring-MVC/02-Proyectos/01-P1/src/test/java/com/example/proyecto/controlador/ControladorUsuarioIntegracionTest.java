package com.example.proyecto.controlador;


import com.example.proyecto.dto.UsuarioDTO;
import com.example.proyecto.modelo.Usuario;
import com.example.proyecto.servicio.IServicioUsuario;
import com.example.proyecto.controlador.ControladorUsuarioIntegracionTest.TestConfig;
import org.modelmapper.ModelMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; // Anotación para test de MVC
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType; // Para tipos de medios HTTP
import org.springframework.test.web.servlet.MockMvc; // Para simular solicitudes HTTP

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given; // Para definir el comportamiento de los mocks
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // Builders para solicitudes HTTP
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; // Para verificar el JSON de la respuesta
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; // Para verificar el estado HTTP
import static org.hamcrest.Matchers.is; // Hamcrest para aserciones
import static org.hamcrest.Matchers.hasSize;

// @WebMvcTest enfoca el test en el ControladorUsuario.
// MEJORA: Se importa una configuración de test explícita en lugar de usar @MockBean.
@WebMvcTest(ControladorUsuario.class)
@Import(TestConfig.class)
public class ControladorUsuarioIntegracionTest {

    // MockMvc para simular llamadas HTTP.
    @Autowired
    private MockMvc mvc;

    // CORRECCIÓN: Se elimina @MockBean (deprecado) y se autoinyecta el mock
    // que hemos definido como un @Bean en nuestra TestConfig.
    @Autowired
    private IServicioUsuario servicioUsuario;

    // SOLUCIÓN: Definir una configuración de prueba anidada y estática.
    @TestConfiguration
    static class TestConfig {
        // Este método le enseña a Spring cómo "construir" un bean de IServicioUsuario para este test.
        @Bean
        public IServicioUsuario servicioUsuario() {

            return Mockito.mock(IServicioUsuario.class);
        }

        //Usar una estrategia de coincidencia estricta
        // Esto ayuda a ModelMapper a mapear correctamente entre clases POJO
        // y Records de Java, ya que se basa en los nombres de las propiedades
        // en lugar de depender estrictamente de los metodos get/set
        @Bean
        public ModelMapper modelMapper() {

            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper;
        }
    }

    // MEJORA: Inyectar un ObjectMapper para convertir objetos a JSON de forma segura.
    // Spring Boot lo configura automáticamente en el contexto de test.
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void cuandoObtenerTodosUsuarios_entoncesRetornarArrayJson() throws Exception {
        // Dado: un usuario de prueba.
        Usuario juan = new Usuario("Juan", "juan@test.com");
        List<Usuario> todosUsuarios = Arrays.asList(juan);

        // Cuando: el servicioUsuario.encontrarTodosUsuarios() es llamado, retornar la lista de usuarios.
        given(servicioUsuario.encontrarTodosUsuarios()).willReturn(todosUsuarios);

        // Entonces: simular una solicitud GET a /api/usuarios, verificar el estado y el contenido JSON.
        mvc.perform(get("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera un estado HTTP 200 OK
                .andExpect(jsonPath("$", hasSize(1))) // Espera un array JSON con un elemento
                // CORRECCIÓN: Para acceder al primer elemento de un array JSON, se usa [0].
                .andExpect(jsonPath("$[0].nombre", is(juan.getNombre())));
    }

    @Test
    public void cuandoCrearUsuario_entoncesRetornarUsuarioCreado() throws Exception {
        // Dado: un DTO de usuario y la entidad que sería guardada.
        UsuarioDTO usuarioDTO = new UsuarioDTO(null, "Pedro", "pedro@test.com");
        Usuario pedroGuardado = new Usuario("Pedro", "pedro@test.com");
        pedroGuardado.setId(1L); // Simula el ID generado

        // Cuando: servicioUsuario.guardarUsuario() es llamado, retornar la entidad guardada.
        given(servicioUsuario.guardarUsuario(org.mockito.ArgumentMatchers.any(Usuario.class)))
                .willReturn(pedroGuardado);

        // Entonces: simular una solicitud POST a /api/usuarios con el DTO, verificar estado y contenido.
        // MEJORA: Usar ObjectMapper para crear el cuerpo JSON. Es más robusto que un String hardcodeado.
        mvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isCreated()) // Espera un estado HTTP 201 Created
                .andExpect(jsonPath("$.nombre", is(usuarioDTO.getNombre()))); // Verifica el nombre del usuario creado
    }

    @Test
    public void cuandoObtenerUsuarioNoExistente_entoncesRetornarNotFound() throws Exception {
        // Dado: un ID que no existe.
        Long idNoExistente = 99L;

        // Cuando: el servicioUsuario.encontrarUsuarioPorId() es llamado, retornar Optional.empty().
        given(servicioUsuario.encontrarUsuarioPorId(idNoExistente)).willReturn(Optional.empty());

        // Entonces: simular una solicitud GET y esperar un estado HTTP 404 Not Found.
        mvc.perform(get("/api/usuarios/{id}", idNoExistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Espera un estado HTTP 404 Not Found
    }
}