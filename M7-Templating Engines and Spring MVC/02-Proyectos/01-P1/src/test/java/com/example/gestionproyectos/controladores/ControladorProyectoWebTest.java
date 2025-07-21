package com.example.gestionproyectos.controladores;

import com.example.gestionproyectos.config.MapeadorDeProyectos;
import com.example.gestionproyectos.controlador.ControladorProyectoWeb;
import com.example.gestionproyectos.dto.ProyectoCrearDTO;
import com.example.gestionproyectos.dto.ProyectoVerDTO;
import com.example.gestionproyectos.dto.TareaVerDTO;
import com.example.gestionproyectos.modelos.EstadoTarea;
import com.example.gestionproyectos.servicio.ServicioProyecto;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// ANÁLISIS SENIOR: Se elimina la anotación de Mockito para usar la de Spring Boot que es más completa para este tipo de test
@WebMvcTest(ControladorProyectoWeb.class)
@Import(MapeadorDeProyectos.class)
@ActiveProfiles("test")
public class ControladorProyectoWebTest {

    @Autowired
    private MockMvc mvc;

    // ANÁLISIS SENIOR: @MockBean es la anotación correcta para @WebMvcTest, ya que integra el mock en el contexto de Spring.
    // La advertencia de "deprecated" es a menudo un falso positivo en algunos IDEs o versiones antiguas.
    // Para @WebMvcTest, @MockBean sigue siendo la herramienta estándar y recomendada.
    @MockitoBean
    private ServicioProyecto servicioProyecto;

    @Autowired
    private ModelMapper modelMapper; // Ahora sí usaremos el ModelMapper

    @Test
    void cuandoListarProyectos_entoncesRetornaVistaConProyectos() throws Exception {
        // Dado: Creamos los DTOs que el servicio DEBE devolver
        ProyectoVerDTO dto1 = new ProyectoVerDTO(1L, "Proyecto Uno", LocalDate.now(), new HashSet<>());
        ProyectoVerDTO dto2 = new ProyectoVerDTO(2L, "Proyecto Dos", LocalDate.now(), new HashSet<>());
        List<ProyectoVerDTO> listaDeDTOs = Arrays.asList(dto1, dto2);

        // El mock ahora devuelve el tipo correcto: List<ProyectoVerDTO>
        given(servicioProyecto.obtenerTodosLosProyectos()).willReturn(listaDeDTOs);

        // Cuando y Entonces
        mvc.perform(get("/proyectos")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("proyectos/listaProyectos"))
                .andExpect(model().attributeExists("proyectos"))
                .andExpect(model().attribute("proyectos", hasSize(2)))
                .andExpect(content().string(containsString("Proyecto Uno")))
                .andExpect(content().string(containsString("Proyecto Dos")));
    }

    @Test
    void cuandoMostrarFormularioCreacion_entoncesRetornaVistaDeFormulario() throws Exception {
        mvc.perform(get("/proyectos/nuevo"))
                .andExpect(status().isOk())
                .andExpect(view().name("proyectos/crearProyecto"))
                .andExpect(model().attributeExists("proyectoCrearDTO"))
                .andExpect(model().attribute("proyectoCrearDTO", hasProperty("nombre", nullValue())));
    }

    @Test
    void cuandoCrearProyectoValido_entoncesRedireccionaYGuarda() throws Exception {
        // Dado: Creamos el DTO que el servicio DEBE devolver
        ProyectoVerDTO dtoGuardado = new ProyectoVerDTO(1L, "Nuevo Proyecto", LocalDate.now(), new HashSet<>());

        // El mock ahora devuelve el tipo correcto: ProyectoVerDTO
        given(servicioProyecto.crearProyecto(any(ProyectoCrearDTO.class))).willReturn(dtoGuardado);

        // Cuando y Entonces
        mvc.perform(post("/proyectos/nuevo")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nombre", "Nuevo Proyecto"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/proyectos"))
                .andExpect(flash().attributeExists("mensajeExito"));
    }

    @Test
    void cuandoCrearProyectoInvalido_entoncesRetornaFormularioConErrores() throws Exception {
        mvc.perform(post("/proyectos/nuevo")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nombre", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("proyectos/crearProyecto"))
                .andExpect(model().attributeHasErrors("proyectoCrearDTO"))
                .andExpect(model().attributeHasFieldErrors("proyectoCrearDTO", "nombre"));
    }

    @Test
    void cuandoVerDetallesProyectoExistente_entoncesRetornaVistaConDetalles() throws Exception {
        // Dado: Creamos la estructura de DTOs anidados que el servicio DEBE devolver
        ProyectoVerDTO proyectoDTO = new ProyectoVerDTO();
        proyectoDTO.setId(1L);
        proyectoDTO.setNombre("Proyecto con Tareas");
        proyectoDTO.setFechaCreacion(LocalDate.now());

        TareaVerDTO tareaDTO1 = new TareaVerDTO(101L, "Tarea 1", "Descripción 1", EstadoTarea.PENDIENTE);
        TareaVerDTO tareaDTO2 = new TareaVerDTO(102L, "Tarea 2", "Descripción 2", EstadoTarea.COMPLETADA);
        proyectoDTO.setTareas(new HashSet<>(Arrays.asList(tareaDTO1, tareaDTO2)));

        // El mock ahora devuelve el tipo correcto: Optional<ProyectoVerDTO>
        given(servicioProyecto.obtenerProyectoPorId(1L)).willReturn(Optional.of(proyectoDTO));

        // Cuando y Entonces
        mvc.perform(get("/proyectos/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("proyectos/detallesProyecto"))
                .andExpect(model().attributeExists("proyecto"))
                .andExpect(model().attribute("proyecto", hasProperty("id", is(1L))))
                .andExpect(model().attribute("proyecto", hasProperty("nombre", is("Proyecto con Tareas"))))
                .andExpect(model().attribute("proyecto", hasProperty("tareas", hasSize(2))));
    }

    @Test
    void cuandoVerDetallesProyectoNoExistente_entoncesRedireccionaALista() throws Exception {
        // Dado
        given(servicioProyecto.obtenerProyectoPorId(99L)).willReturn(Optional.empty());

        // Cuando y Entonces
        mvc.perform(get("/proyectos/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/proyectos"))
                .andExpect(flash().attributeExists("mensajeError"));
    }
}