package com.example.proyecto.repositorio;

import com.example.proyecto.modelo.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test; // JUnit 5
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest; // Anotación para test de JPA
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager; // Para gestión de entidades en tests

import static org.assertj.core.api.Assertions.assertThat; // AssertJ para aserciones fluidas

// @DataJpaTest configura un entorno de test enfocado en la capa JPA.
@DataJpaTest
public class RepositorioUsuarioIntegracionTest {

    // TestEntityManager para persistir y encontrar entidades en un contexto de prueba.
    @Autowired
    private TestEntityManager gestorEntidadesPrueba;

    // Inyección del repositorio a probar.
    @Autowired
    private RepositorioUsuario repositorioUsuario;


    // @BeforeEach se ejecuta antes de cada test.
    // Esto asegura que la tabla de usuarios este vacia, garantizando aislamiento
    // del test y evitando colisiones con los datos de data.sql
    @BeforeEach
    void setUp() {
        repositorioUsuario.deleteAll();
    }


    @Test
    public void cuandoEncontrarPorNombre_entoncesRetornarUsuario() {
        // Dada una entidad de usuario.
        Usuario juan = new Usuario("Juan Pérez", "juan.perez@test.com");
        gestorEntidadesPrueba.persistAndFlush(juan); // Persiste y vacía la sesión para asegurar que está en DB.

        // Cuando buscamos el usuario por nombre.
        Usuario encontrado = repositorioUsuario.findByNombre(juan.getNombre());

        // Entonces, el usuario encontrado debe coincidir.
        assertThat(encontrado.getNombre()).isEqualTo(juan.getNombre());
    }

    @Test
    public void cuandoGuardarUsuario_entoncesIdGenerado() {
        // Dada una nueva entidad de usuario.
        Usuario nuevoUsuario = new Usuario("Nuevo Usuario", "nuevo.usuario@test.com");

        // Cuando guardamos el usuario.
        Usuario usuarioGuardado = repositorioUsuario.save(nuevoUsuario);

        // Entonces el ID debe ser generado y no nulo.
        assertThat(usuarioGuardado.getId()).isNotNull();
    }
}
