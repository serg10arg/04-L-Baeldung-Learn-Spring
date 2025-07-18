package com.example.proyecto.integracion;

import com.example.proyecto.dto.UsuarioDTO;
import com.example.proyecto.modelo.Usuario;
import com.example.proyecto.repositorio.RepositorioUsuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest; // Para cargar el contexto completo de Spring Boot
import org.springframework.boot.test.web.client.TestRestTemplate; // Para realizar llamadas HTTP en tests de integración
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest carga el contexto completo de la aplicación.
// webEnvironment = WebEnvironment.RANDOM_PORT inicia la aplicación en un puerto HTTP aleatorio.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioApiIntegracionTest {

    // TestRestTemplate es útil para enviar solicitudes HTTP a tu aplicación en un test de integración.
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RepositorioUsuario repositorioUsuario; // Para verificar el estado de la base de datos

    // MEJORA: Limpiar la base de datos después de cada test para garantizar el aislamiento.
    @AfterEach
    public void tearDown() {
        repositorioUsuario.deleteAll();
    }

    @Test
    public void cuandoObtenerUsuarioPorId_entoncesUsuarioRetornado() {
        // Dada: un usuario existente en la base de datos.
        Usuario usuarioExistente = new Usuario("Ana Perez", "ana.perez@example.com");
        usuarioExistente = repositorioUsuario.save(usuarioExistente);

        // Cuando: realizamos una solicitud GET al endpoint del usuario.
        // CORRECCIÓN: La API devuelve un UsuarioDTO, no la entidad Usuario.
        ResponseEntity<UsuarioDTO> respuesta = restTemplate.getForEntity(
                "/api/usuarios/{id}", UsuarioDTO.class, usuarioExistente.getId());

        // Entonces: verificamos que la respuesta sea 200 OK y contenga el usuario correcto.
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(respuesta.getBody()).isNotNull();
        // Verificamos contra el DTO esperado.
        assertThat(respuesta.getBody().getNombre()).isEqualTo(usuarioExistente.getNombre());
        assertThat(respuesta.getBody().getId()).isEqualTo(usuarioExistente.getId());
    }

    @Test
    public void cuandoCrearUsuario_entoncesUsuarioEsPersistido() {
        // Dada: un nuevo DTO de usuario para enviar a la API.
        // CORRECCIÓN: Enviamos un DTO, que es el contrato de la API.
        UsuarioDTO nuevoUsuarioDTO = new UsuarioDTO(null, "Roberto", "roberto@test.com");

        // Cuando: enviamos una solicitud POST para crear el usuario.
        ResponseEntity<UsuarioDTO> respuesta = restTemplate.postForEntity(
                "/api/usuarios", nuevoUsuarioDTO, UsuarioDTO.class);

        // Entonces: verificamos que la respuesta sea 201 Created y que el usuario exista en la DB.
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UsuarioDTO usuarioRespuesta = respuesta.getBody();
        assertThat(usuarioRespuesta).isNotNull();
        assertThat(usuarioRespuesta.getId()).isNotNull();
        assertThat(usuarioRespuesta.getNombre()).isEqualTo(nuevoUsuarioDTO.getNombre());

        // Verificación directa en la base de datos.
        // Usamos el ID de la respuesta para buscar en la base de datos.
        Optional<Usuario> usuarioEncontrado = repositorioUsuario.findById(usuarioRespuesta.getId());
        assertThat(usuarioEncontrado).isPresent();
        assertThat(usuarioEncontrado.get().getNombre()).isEqualTo(nuevoUsuarioDTO.getNombre());
    }
}
