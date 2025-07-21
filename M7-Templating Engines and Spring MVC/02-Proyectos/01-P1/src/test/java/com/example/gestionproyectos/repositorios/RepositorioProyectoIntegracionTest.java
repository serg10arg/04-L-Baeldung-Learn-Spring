package com.example.gestionproyectos.repositorios;

import com.example.gestionproyectos.modelos.Proyecto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat; // Importar AssertJ

@DataJpaTest // Configura un entorno de prueba para la capa JPA
public class RepositorioProyectoIntegracionTest {

    @Autowired
    private TestEntityManager gestorEntidadesDePrueba; // Para persistir entidades directamente en las pruebas

    @Autowired
    private RepositorioProyecto repositorioProyecto; // Repositorio que se va a probar

    private Proyecto proyectoExistente;

    @BeforeEach // Se ejecuta antes de cada método de prueba
    void configurar() {
        // Limpiar la base de datos de prueba antes de cada test para asegurar la independencia
        gestorEntidadesDePrueba.clear();
        proyectoExistente = new Proyecto("Proyecto Existente", LocalDate.now());
        gestorEntidadesDePrueba.persistAndFlush(proyectoExistente); // Persistir el proyecto de prueba
    }

    @Test
    void cuandoGuardarNuevoProyecto_entoncesDebeSerEncontrado() {
        // Dado
        Proyecto nuevoProyecto = new Proyecto("Proyecto Test", LocalDate.now());

        // Cuando
        Proyecto proyectoGuardado = repositorioProyecto.save(nuevoProyecto);

        // Entonces
        assertThat(proyectoGuardado).isNotNull();
        assertThat(proyectoGuardado.getId()).isNotNull();
        assertThat(proyectoGuardado.getNombre()).isEqualTo("Proyecto Test");

        Optional<Proyecto> encontrado = repositorioProyecto.findById(proyectoGuardado.getId());
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNombre()).isEqualTo("Proyecto Test");
    }

    @Test
    void cuandoBuscarPorNombre_entoncesRetornaProyecto() {
        // Dado un proyecto existente configurado en @BeforeEach

        // Cuando
        Optional<Proyecto> encontrado = repositorioProyecto.findByNombre("Proyecto Existente");

        // Entonces
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNombre()).isEqualTo("Proyecto Existente");
        assertThat(encontrado.get().getId()).isEqualTo(proyectoExistente.getId());
    }

    @Test
    void cuandoBuscarPorNombreNoExistente_entoncesRetornaVacio() {
        // Cuando
        Optional<Proyecto> encontrado = repositorioProyecto.findByNombre("Proyecto No Existente");

        // Entonces
        assertThat(encontrado).isEmpty();
    }

    @Test
    void cuandoEliminarProyecto_entoncesNoDebeSerEncontrado() {
        // Dado un proyecto existente configurado en @BeforeEach

        // Cuando
        repositorioProyecto.deleteById(proyectoExistente.getId());

        // Entonces
        Optional<Proyecto> eliminado = repositorioProyecto.findById(proyectoExistente.getId());
        assertThat(eliminado).isEmpty();
    }
}