
---

### **1. Título del Caso Práctico**

**Construyendo una Aplicación Web Integral con Spring Boot: Gestión de Proyectos, Persistencia y Capa Web**

### **2. Resumen del Problema y Objetivos de Aprendizaje**

En el desarrollo de software moderno, la creación de aplicaciones web robustas y escalables es una habilidad fundamental. Este caso práctico aborda el desafío de construir una aplicación de gestión de proyectos y tareas, que requiere una manipulación eficiente de datos, una clara separación de responsabilidades y una interfaz de usuario dinámica. A menudo, los desarrolladores se enfrentan a la complejidad de integrar frameworks de persistencia como JPA y Hibernate, desarrollar APIs RESTful para la comunicación entre cliente y servidor, o construir interfaces de usuario tradicionales basadas en el patrón Modelo-Vista-Controlador (MVC). Este proyecto te guiará a través de la implementación de estas capas, mostrando cómo Spring Boot simplifica drásticamente estas tareas complejas y promueve las mejores prácticas de ingeniería de software.

Al completar este caso práctico, alcanzarás los siguientes objetivos de aprendizaje clave:

1.  **Dominar la Persistencia de Datos con Spring Data JPA:** Aprenderás a configurar y utilizar Spring Data JPA para implementar una capa de persistencia eficiente, realizando operaciones CRUD básicas y consultas avanzadas sobre entidades de `Proyecto` y `Tarea` utilizando una base de datos en memoria H2.
2.  **Desarrollar Capas Web con Spring MVC y REST:** Entenderás y aplicarás el patrón MVC en Spring para crear controladores web tradicionales y APIs RESTful, definiendo endpoints HTTP, manejando solicitudes y estructurando las respuestas para clientes web.
3.  **Implementar el Diseño de Capas y Objetos de Transferencia de Datos (DTOs):** Comprenderás la importancia de la separación de responsabilidades en una aplicación de múltiples capas, y aprenderás a desacoplar la capa de controlador de las entidades de persistencia utilizando DTOs para un manejo seguro y eficiente de los datos.
4.  **Integrar Motores de Plantillas y Validación de Formularios:** Adquirirás experiencia en la creación de interfaces de usuario dinámicas utilizando motores de plantillas como Thymeleaf o Mustache, y aprenderás a implementar y mostrar validaciones de formularios para asegurar la integridad de los datos ingresados por el usuario.
5.  **Configuración y Pruebas Efectivas con Spring Boot:** Te familiarizarás con el proceso de configuración de un proyecto Spring Boot, desde la adición de dependencias hasta la escritura de pruebas de integración para validar la funcionalidad de la aplicación en un entorno cercano al de producción.

### **3. Propuesta de Solución (Guía Paso a Paso)**

A continuación, se presenta una guía detallada y secuencial para construir la aplicación de gestión de proyectos y tareas.

#### **a. Configuración Inicial**

El primer paso es configurar el proyecto Java con Spring Boot. Utilizaremos Maven para la gestión de dependencias.

1.  **Creación del Proyecto:** Puedes iniciar un nuevo proyecto Spring Boot usando Spring Initializr (start.spring.io) o tu IDE preferido.
2.  **Dependencias (pom.xml):** Incluye las siguientes dependencias esenciales para una aplicación web con persistencia y pruebas:

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>3.2.0</version> <!-- O una versión más reciente -->
            <relativePath/> <!-- lookup parent from repository -->
        </parent>
        <groupId>com.baeldung.casopractico</groupId>
        <artifactId>gestion-proyectos</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <name>gestion-proyectos</name>
        <description>Proyecto de Gestión de Proyectos y Tareas con Spring Boot</description>

        <properties>
            <java.version>17</java.version>
        </properties>

        <dependencies>
            <!-- Dependencia para aplicaciones web (Spring MVC, Tomcat) -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <!-- Dependencia para persistencia con Spring Data JPA -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-jpa</artifactId>
            </dependency>
            <!-- Base de datos en memoria H2 para desarrollo y pruebas -->
            <dependency>
                <groupId>com.h2database</groupId>
                <artifactId>h2</artifactId>
                <scope>runtime</scope>
            </dependency>
            <!-- Soporte para validación de Bean (Hibernate Validator) -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-validation</artifactId>
            </dependency>
            <!-- Motor de plantillas Thymeleaf -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-thymeleaf</artifactId>
            </dependency>
            <!-- Para conversión de Entidad a DTO (ModelMapper) -->
            <dependency>
                <groupId>org.modelmapper</groupId>
                <artifactId>modelmapper</artifactId>
                <version>3.2.0</version>
            </dependency>
            <!-- Dependencia para pruebas en Spring Boot -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
            </dependency>
        </dependencies>

        <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                </plugin>
            </plugins>
        </build>
    </project>
    ```

3.  **Clase Principal de la Aplicación:** Spring Boot autoconfigura muchas funcionalidades al detectar las dependencias en el classpath. La clase principal debe estar anotada con `@SpringBootApplication`.

    ```java
    // src/main/java/com/baeldung/casopractico/gestionproyectos/Aplicacion.java
    package com.baeldung.casopractico.gestionproyectos;

    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;

    @SpringBootApplication
    public class Aplicacion {

        public static void main(String[] args) {
            SpringApplication.run(Aplicacion.class, args);
        }

    }
    ```

#### **b. Modelo de Datos**

Definiremos las entidades JPA para nuestra base de datos y los DTOs para la comunicación con la capa web.

1.  **Entidad `Proyecto`:** Representa un proyecto en nuestra aplicación. Tendrá un `id` (clave primaria) generado automáticamente, un `nombre` y una `fechaCreacion`.

    ```java
    // src/main/java/com/baeldung/casopractico/gestionproyectos/modelo/Proyecto.java
    package com.baeldung.casopractico.gestionproyectos.modelo;

    import jakarta.persistence.CascadeType;
    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.OneToMany;
    import java.time.LocalDate;
    import java.util.HashSet;
    import java.util.Set;

    @Entity
    public class Proyecto {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) // ID generado por la base de datos
        private Long id;
        private String nombre;
        @Column(name = "fecha_creacion")
        private LocalDate fechaCreacion;

        // Un proyecto puede tener varias tareas (relación uno a muchos)
        // CascadeType.ALL significa que las operaciones (persist, merge, remove)
        // se propagarán a las entidades Tarea asociadas.
        @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<Tarea> tareas = new HashSet<>();

        public Proyecto() {
            // Constructor vacío requerido por JPA
        }

        public Proyecto(String nombre, LocalDate fechaCreacion) {
            this.nombre = nombre;
            this.fechaCreacion = fechaCreacion;
        }

        // --- Getters y Setters ---
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public LocalDate getFechaCreacion() {
            return fechaCreacion;
        }

        public void setFechaCreacion(LocalDate fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
        }

        public Set<Tarea> getTareas() {
            return tareas;
        }

        public void setTareas(Set<Tarea> tareas) {
            this.tareas = tareas;
        }

        public void agregarTarea(Tarea tarea) {
            this.tareas.add(tarea);
            tarea.setProyecto(this);
        }

        public void eliminarTarea(Tarea tarea) {
            this.tareas.remove(tarea);
            tarea.setProyecto(null);
        }
    }
    ```

2.  **Entidad `Tarea`:** Representa una tarea asociada a un proyecto.

    ```java
    // src/main/java/com/baeldung/casopractico/gestionproyectos/modelo/Tarea.java
    package com.baeldung.casopractico.gestionproyectos.modelo;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.ManyToOne;
    import java.time.LocalDate;

    @Entity
    public class Tarea {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String descripcion;
        @Column(name = "fecha_vencimiento")
        private LocalDate fechaVencimiento;
        private boolean completada;

        // Muchas tareas pueden pertenecer a un solo proyecto (relación muchos a uno)
        @ManyToOne
        @JoinColumn(name = "proyecto_id") // Clave foránea en la tabla Tarea
        private Proyecto proyecto;

        public Tarea() {
        }

        public Tarea(String descripcion, LocalDate fechaVencimiento, boolean completada, Proyecto proyecto) {
            this.descripcion = descripcion;
            this.fechaVencimiento = fechaVencimiento;
            this.completada = completada;
            this.proyecto = proyecto;
        }

        // --- Getters y Setters ---
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public LocalDate getFechaVencimiento() {
            return fechaVencimiento;
        }

        public void setFechaVencimiento(LocalDate fechaVencimiento) {
            this.fechaVencimiento = fechaVencimiento;
        }

        public boolean isCompletada() {
            return completada;
        }

        public void setCompletada(boolean completada) {
            this.completada = completada;
        }

        public Proyecto getProyecto() {
            return proyecto;
        }

        public void setProyecto(Proyecto proyecto) {
            this.proyecto = proyecto;
        }
    }
    ```

3.  **DTOs para `Proyecto` y `Tarea`:** Para mantener la capa de controlador desacoplada de la capa de persistencia, definimos Objetos de Transferencia de Datos (DTOs). Esto es crucial en aplicaciones reales.

    ```java
    // src/main/java/com/baeldung/casopractico/gestionproyectos/dto/ProyectoDTO.java
    package com.baeldung.casopractico.gestionproyectos.dto;

    import jakarta.validation.constraints.NotBlank; // Para validación
    import java.time.LocalDate;
    import java.util.ArrayList;
    import java.util.List;

    public class ProyectoDTO {

        private Long id;
        @NotBlank(message = "El nombre del proyecto no puede estar vacío") // Validación
        private String nombre;
        private LocalDate fechaCreacion;
        private List<TareaDTO> tareas = new ArrayList<>(); // Para incluir tareas en el DTO

        public ProyectoDTO() {
        }

        // Constructor con todos los campos para facilitar la creación
        public ProyectoDTO(Long id, String nombre, LocalDate fechaCreacion, List<TareaDTO> tareas) {
            this.id = id;
            this.nombre = nombre;
            this.fechaCreacion = fechaCreacion;
            this.tareas = tareas;
        }

        // --- Getters y Setters ---
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public LocalDate getFechaCreacion() {
            return fechaCreacion;
        }

        public void setFechaCreacion(LocalDate fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
        }

        public List<TareaDTO> getTareas() {
            return tareas;
        }

        public void setTareas(List<TareaDTO> tareas) {
            this.tareas = tareas;
        }
    }

    // src/main/java/com/baeldung/casopractico/gestionproyectos/dto/TareaDTO.java
    package com.baeldung.casopractico.gestionproyectos.dto;

    import jakarta.validation.constraints.NotBlank;
    import java.time.LocalDate;

    public class TareaDTO {

        private Long id;
        @NotBlank(message = "La descripción de la tarea no puede estar vacía")
        private String descripcion;
        private LocalDate fechaVencimiento;
        private boolean completada;
        private Long idProyecto; // Para referenciar el proyecto padre

        public TareaDTO() {
        }

        public TareaDTO(Long id, String descripcion, LocalDate fechaVencimiento, boolean completada, Long idProyecto) {
            this.id = id;
            this.descripcion = descripcion;
            this.fechaVencimiento = fechaVencimiento;
            this.completada = completada;
            this.idProyecto = idProyecto;
        }

        // --- Getters y Setters ---
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public LocalDate getFechaVencimiento() {
            return fechaVencimiento;
        }

        public void setFechaVencimiento(LocalDate fechaVencimiento) {
            this.fechaVencimiento = fechaVencimiento;
        }

        public boolean isCompletada() {
            return completada;
        }

        public void setCompletada(boolean completada) {
            this.completada = completada;
        }

        public Long getIdProyecto() {
            return idProyecto;
        }

        public void setIdProyecto(Long idProyecto) {
            this.idProyecto = idProyecto;
        }
    }
    ```

4.  **Configuración de ModelMapper:** Para simplificar la conversión entre entidades y DTOs, configuraremos `ModelMapper` como un bean de Spring.

    ```java
    // src/main/java/com/baeldung/casopractico/gestionproyectos/configuracion/ConfiguracionAplicacion.java
    package com.baeldung.casopractico.gestionproyectos.configuracion;

    import org.modelmapper.ModelMapper;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    @Configuration
    public class ConfiguracionAplicacion {

        @Bean
        public ModelMapper modelMapper() {
            ModelMapper modelMapper = new ModelMapper();
            // Aquí puedes añadir configuraciones personalizadas si es necesario
            // Por ejemplo, para mapear propiedades con nombres diferentes o anidadas.
            return modelMapper;
        }
    }
    ```

#### **c. Capa de Persistencia**

Spring Data JPA simplifica enormemente la implementación de la capa de acceso a datos.

1.  **Interfaces de Repositorio:** Define interfaces que extiendan `JpaRepository` (que a su vez extiende `PagingAndSortingRepository` y `CrudRepository`) para `Proyecto` y `Tarea`. Spring Data JPA generará automáticamente las implementaciones en tiempo de ejecución.

    ```java
    // src/main/java/com/baeldung/casopractico/gestionproyectos/repositorio/RepositorioProyecto.java
    package com.baeldung.casopractico.gestionproyectos.repositorio;

    import com.baeldung.casopractico.gestionproyectos.modelo.Proyecto;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    import java.util.List;

    @Repository
    public interface RepositorioProyecto extends JpaRepository<Proyecto, Long> {
        // Métodos derivados: Spring Data JPA genera la consulta automáticamente
        List<Proyecto> findByNombreContainingIgnoreCase(String nombre);
        List<Proyecto> findByFechaCreacionBetween(java.time.LocalDate inicio, java.time.LocalDate fin); // Ejemplo de consulta avanzada
    }

    // src/main/java/com/baeldung/casopractico/gestionproyectos/repositorio/RepositorioTarea.java
    package com.baeldung.casopractico.gestionproyectos.repositorio;

    import com.baeldung.casopractico.gestionproyectos.modelo.Tarea;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    import java.util.List;

    @Repository
    public interface RepositorioTarea extends JpaRepository<Tarea, Long> {
        List<Tarea> findByProyectoId(Long proyectoId);
        List<Tarea> findByCompletada(boolean completada);
    }
    ```

2.  **Carga de Datos Iniciales (Opcional):** Para tener datos con los que trabajar al iniciar la aplicación, puedes usar un `CommandLineRunner` o los archivos `schema.sql` y `data.sql`.

    ```java
    // Extensión de la clase Aplicacion.java para cargar datos iniciales
    // src/main/java/com/baeldung/casopractico/gestionproyectos/Aplicacion.java
    package com.baeldung.casopractico.gestionproyectos;

    import com.baeldung.casopractico.gestionproyectos.modelo.Proyecto;
    import com.baeldung.casopractico.gestionproyectos.modelo.Tarea;
    import com.baeldung.casopractico.gestionproyectos.repositorio.RepositorioProyecto;
    import org.springframework.boot.CommandLineRunner;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.context.annotation.Bean;
    import java.time.LocalDate;
    import java.util.Arrays;

    @SpringBootApplication
    public class Aplicacion {

        public static void main(String[] args) {
            SpringApplication.run(Aplicacion.class, args);
        }

        @Bean
        public CommandLineRunner iniciarDatos(RepositorioProyecto repositorioProyecto) {
            return args -> {
                // Crear y guardar proyectos
                Proyecto proyecto1 = new Proyecto("Desarrollo de API REST", LocalDate.now());
                Proyecto proyecto2 = new Proyecto("Diseño de Base de Datos", LocalDate.now().minusMonths(1));
                Proyecto proyecto3 = new Proyecto("Implementación de Frontend", LocalDate.now().plusWeeks(2));

                repositorioProyecto.saveAll(Arrays.asList(proyecto1, proyecto2, proyecto3));

                // Agregar tareas al proyecto1
                proyecto1.agregarTarea(new Tarea("Definir Endpoints", LocalDate.now().plusDays(5), false, proyecto1));
                proyecto1.agregarTarea(new Tarea("Implementar Autenticación", LocalDate.now().plusDays(10), false, proyecto1));
                proyecto1.agregarTarea(new Tarea("Escribir Pruebas Unitarias", LocalDate.now().plusDays(15), false, proyecto1));
                repositorioProyecto.save(proyecto1); // Guardar proyecto con tareas asociadas

                // Agregar tareas al proyecto2
                proyecto2.agregarTarea(new Tarea("Diseñar Modelo E-R", LocalDate.now().minusWeeks(2), true, proyecto2));
                proyecto2.agregarTarea(new Tarea("Normalizar Esquema", LocalDate.now().minusWeeks(1), true, proyecto2));
                repositorioProyecto.save(proyecto2);

                System.out.println("Datos iniciales cargados:");
                repositorioProyecto.findAll().forEach(System.out::println);
            };
        }
    }
    ```

#### **d. Capa de Servicio**

La capa de servicio contiene la lógica de negocio. Es aquí donde inyectamos los repositorios y aplicamos la lógica transaccional.

1.  **Clase `ServicioProyecto`:** Anotada con `@Service`, inyecta el `RepositorioProyecto` y `ModelMapper`. Los métodos deben estar anotados con `@Transactional` para asegurar la atomicidad de las operaciones de base de datos.

    ```java
    // src/main/java/com/baeldung/casopractico/gestionproyectos/servicio/ServicioProyecto.java
    package com.baeldung.casopractico.gestionproyectos.servicio;

    import com.baeldung.casopractico.gestionproyectos.dto.ProyectoDTO;
    import com.baeldung.casopractico.gestionproyectos.dto.TareaDTO;
    import com.baeldung.casopractico.gestionproyectos.modelo.Proyecto;
    import com.baeldung.casopractico.gestionproyectos.modelo.Tarea;
    import com.baeldung.casopractico.gestionproyectos.repositorio.RepositorioProyecto;
    import com.baeldung.casopractico.gestionproyectos.repositorio.RepositorioTarea;
    import org.modelmapper.ModelMapper;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional; // Importación correcta

    import java.time.LocalDate;
    import java.util.List;
    import java.util.Optional;
    import java.util.stream.Collectors;

    @Service
    public class ServicioProyecto {

        private final RepositorioProyecto repositorioProyecto;
        private final RepositorioTarea repositorioTarea;
        private final ModelMapper modelMapper;

        @Autowired
        public ServicioProyecto(RepositorioProyecto repositorioProyecto, RepositorioTarea repositorioTarea, ModelMapper modelMapper) {
            this.repositorioProyecto = repositorioProyecto;
            this.repositorioTarea = repositorioTarea;
            this.modelMapper = modelMapper;
        }

        @Transactional(readOnly = true)
        public List<ProyectoDTO> obtenerTodosLosProyectos() {
            return repositorioProyecto.findAll().stream()
                    .map(this::convertirAProyectoDTO)
                    .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public Page<ProyectoDTO> obtenerTodosLosProyectosPaginados(Pageable paginable) {
            return repositorioProyecto.findAll(paginable)
                    .map(this::convertirAProyectoDTO);
        }

        @Transactional(readOnly = true)
        public Optional<ProyectoDTO> obtenerProyectoPorId(Long id) {
            return repositorioProyecto.findById(id)
                    .map(this::convertirAProyectoDTO);
        }

        @Transactional
        public ProyectoDTO crearProyecto(ProyectoDTO proyectoDTO) {
            Proyecto proyecto = convertirAProyectoEntidad(proyectoDTO);
            proyecto.setFechaCreacion(LocalDate.now()); // Establecer fecha de creación al crear
            Proyecto proyectoGuardado = repositorioProyecto.save(proyecto);
            return convertirAProyectoDTO(proyectoGuardado);
        }

        @Transactional
        public ProyectoDTO actualizarProyecto(Long id, ProyectoDTO proyectoDTO) {
            return repositorioProyecto.findById(id).map(proyectoExistente -> {
                proyectoExistente.setNombre(proyectoDTO.getNombre());
                // Asegurar que las tareas se actualizan correctamente o se manejan las nuevas/eliminadas
                // Lógica de actualización de tareas más compleja si es necesario
                return convertirAProyectoDTO(repositorioProyecto.save(proyectoExistente));
            }).orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));
        }

        @Transactional
        public void eliminarProyecto(Long id) {
            repositorioProyecto.deleteById(id);
        }

        @Transactional
        public TareaDTO agregarTareaAProyecto(Long proyectoId, TareaDTO tareaDTO) {
            Proyecto proyecto = repositorioProyecto.findById(proyectoId)
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + proyectoId));
            Tarea tarea = modelMapper.map(tareaDTO, Tarea.class);
            proyecto.agregarTarea(tarea);
            repositorioProyecto.save(proyecto); // Esto guarda la tarea también debido al CascadeType.ALL
            return modelMapper.map(tarea, TareaDTO.class);
        }

        // Métodos de conversión de Entidad a DTO y viceversa
        private ProyectoDTO convertirAProyectoDTO(Proyecto proyecto) {
            ProyectoDTO proyectoDTO = modelMapper.map(proyecto, ProyectoDTO.class);
            // Mapear tareas anidadas si están cargadas
            if (proyecto.getTareas() != null && !proyecto.getTareas().isEmpty()) {
                proyectoDTO.setTareas(
                        proyecto.getTareas().stream()
                                .map(tarea -> modelMapper.map(tarea, TareaDTO.class))
                                .collect(Collectors.toList())
                );
            }
            return proyectoDTO;
        }

        private Proyecto convertirAProyectoEntidad(ProyectoDTO proyectoDTO) {
            Proyecto proyecto = modelMapper.map(proyectoDTO, Proyecto.class);
            // Reconstruir la relación bidireccional si hay tareas en el DTO
            if (proyectoDTO.getTareas() != null && !proyectoDTO.getTareas().isEmpty()) {
                proyectoDTO.getTareas().forEach(tareaDTO -> {
                    Tarea tarea = modelMapper.map(tareaDTO, Tarea.class);
                    tarea.setProyecto(proyecto);
                    proyecto.getTareas().add(tarea);
                });
            }
            return proyecto;
        }
    }
    ```

#### **e. Capa de API/Controlador**

Esta capa expone los recursos a través de endpoints HTTP. Demostraremos tanto una API RESTful como un controlador MVC tradicional.

1.  **Controlador RESTful para `Proyecto`:** Usaremos `@RestController` para indicar que este controlador servirá una API REST, devolviendo directamente los objetos como JSON/XML.

    ```java
    // src/main/java/com/baeldung/casopractico/gestionproyectos/controlador/ControladorProyectoREST.java
    package com.baeldung.casopractico.gestionproyectos.controlador;

    import com.baeldung.casopractico.gestionproyectos.dto.ProyectoDTO;
    import com.baeldung.casopractico.gestionproyectos.servicio.ServicioProyecto;
    import jakarta.validation.Valid; // Para habilitar la validación
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Sort;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/proyectos") // Ruta base para todos los endpoints de este controlador
    public class ControladorProyectoREST {

        private final ServicioProyecto servicioProyecto;

        @Autowired
        public ControladorProyectoREST(ServicioProyecto servicioProyecto) {
            this.servicioProyecto = servicioProyecto;
        }

        @GetMapping // GET /api/proyectos
        public ResponseEntity<List<ProyectoDTO>> obtenerTodosLosProyectos(
                @RequestParam(defaultValue = "0") int pagina,
                @RequestParam(defaultValue = "10") int tamano,
                @RequestParam(defaultValue = "id,asc") String[] ordenarPor) {
            Sort.Direction direccion = Sort.Direction.fromString(ordenarPor);
            Sort sort = Sort.by(direccion, ordenarPor);
            Pageable paginable = PageRequest.of(pagina, tamano, sort); // Paginación y ordenación
            Page<ProyectoDTO> paginaProyectos = servicioProyecto.obtenerTodosLosProyectosPaginados(paginable);
            return ResponseEntity.ok(paginaProyectos.getContent());
        }

        @GetMapping("/{id}") // GET /api/proyectos/{id}
        public ResponseEntity<ProyectoDTO> obtenerProyectoPorId(@PathVariable Long id) {
            return servicioProyecto.obtenerProyectoPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        @PostMapping // POST /api/proyectos
        public ResponseEntity<ProyectoDTO> crearProyecto(@Valid @RequestBody ProyectoDTO proyectoDTO) {
            ProyectoDTO nuevoProyecto = servicioProyecto.crearProyecto(proyectoDTO);
            return new ResponseEntity<>(nuevoProyecto, HttpStatus.CREATED);
        }

        @PutMapping("/{id}") // PUT /api/proyectos/{id}
        public ResponseEntity<Void> actualizarProyecto(@PathVariable Long id, @Valid @RequestBody ProyectoDTO proyectoDTO) {
            if (!id.equals(proyectoDTO.getId())) {
                throw new IllegalArgumentException("Los IDs no coinciden.");
            }
            servicioProyecto.actualizarProyecto(id, proyectoDTO);
            return ResponseEntity.ok().build();
        }

        @DeleteMapping("/{id}") // DELETE /api/proyectos/{id}
        public ResponseEntity<Void> eliminarProyecto(@PathVariable Long id) {
            servicioProyecto.eliminarProyecto(id);
            return ResponseEntity.noContent().build();
        }
    }
    ```

#### **f. Motores de Plantillas y Spring MVC Tradicional**

Para construir una interfaz de usuario renderizada por el servidor, utilizaremos el patrón MVC tradicional con Thymeleaf.

1.  **Cambio de `RestController` a `Controller`:** Para manejar vistas, necesitamos un `@Controller` que devuelva un nombre de vista (String) o un `ModelAndView`.

    ```java
    // src/main/java/com/baeldung/casopractico/gestionproyectos/controlador/ControladorProyectoMVC.java
    package com.baeldung.casopractico.gestionproyectos.controlador;

    import com.baeldung.casopractico.gestionproyectos.dto.ProyectoDTO;
    import com.baeldung.casopractico.gestionproyectos.servicio.ServicioProyecto;
    import jakarta.validation.Valid;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model; // Para añadir atributos al modelo
    import org.springframework.validation.BindingResult; // Para manejar errores de validación
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.ModelAttribute;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    import java.time.LocalDate;
    import java.util.List;

    @Controller
    @RequestMapping("/proyectos") // Ruta base para el controlador MVC
    public class ControladorProyectoMVC {

        private final ServicioProyecto servicioProyecto;

        @Autowired
        public ControladorProyectoMVC(ServicioProyecto servicioProyecto) {
            this.servicioProyecto = servicioProyecto;
        }

        @GetMapping("/lista") // GET /proyectos/lista
        public String listarProyectos(Model modelo) {
            List<ProyectoDTO> proyectos = servicioProyecto.obtenerTodosLosProyectos();
            modelo.addAttribute("proyectos", proyectos); // Añadir lista de proyectos al modelo
            modelo.addAttribute("tituloPagina", "Lista de Proyectos"); // Ejemplo de título para la plantilla
            return "lista-proyectos"; // Nombre de la vista Thymeleaf
        }

        @GetMapping("/nuevo") // GET /proyectos/nuevo
        public String mostrarFormularioCreacion(Model modelo) {
            modelo.addAttribute("proyecto", new ProyectoDTO()); // Objeto para el formulario
            modelo.addAttribute("tituloPagina", "Crear Nuevo Proyecto");
            return "formulario-proyecto"; // Nombre de la vista para el formulario
        }

        @PostMapping("/guardar") // POST /proyectos/guardar
        public String guardarProyecto(@Valid @ModelAttribute("proyecto") ProyectoDTO proyectoDTO,
                                     BindingResult resultadoValidacion, // Resultado de la validación
                                     RedirectAttributes atributosRedireccion) {
            if (resultadoValidacion.hasErrors()) {
                // Si hay errores de validación, vuelve al formulario
                return "formulario-proyecto";
            }
            servicioProyecto.crearProyecto(proyectoDTO);
            atributosRedireccion.addFlashAttribute("mensajeExito", "Proyecto guardado exitosamente!");
            return "redirect:/proyectos/lista"; // Redirigir a la lista de proyectos
        }

        // --- Ejemplo con ModelAndView (alternativa a String y Model) ---
        @GetMapping("/detalles/{id}")
        public ModelAndView verDetallesProyecto(@PathVariable Long id) {
            ModelAndView mv = new ModelAndView("detalles-proyecto"); // Nombre de la vista
            servicioProyecto.obtenerProyectoPorId(id)
                    .ifPresent(proyecto -> mv.addObject("proyecto", proyecto)); // Añadir objeto al modelo
            return mv;
        }
    }
    ```

2.  **Plantillas Thymeleaf:** Las plantillas se colocan en `src/main/resources/templates`. Thymeleaf utiliza atributos `th:*` para procesar el HTML.

    ```html
    <!-- src/main/resources/templates/lista-proyectos.html -->
    <!DOCTYPE html>
    <html xmlns:th="http://www.thymeleaf.org">
    <head>
        <title th:text="${tituloPagina}">Lista de Proyectos</title>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    </head>
    <body>
    <div class="container my-4">
        <h1 th:text="${tituloPagina}"></h1>
        <a th:href="@{/proyectos/nuevo}" class="btn btn-primary mb-3">Crear Nuevo Proyecto</a>
        <div th:if="${mensajeExito}" class="alert alert-success" role="alert" th:text="${mensajeExito}"></div>

        <table class="table table-bordered">
            <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Fecha de Creación</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <tr th:each="proyecto : ${proyectos}"> <!-- Iterar sobre la lista de proyectos -->
                <td th:text="${proyecto.id}">1</td>
                <td th:text="${proyecto.nombre}">Nombre del Proyecto</td>
                <td th:text="${proyecto.fechaCreacion}">2023-01-01</td>
                <td>
                    <a th:href="@{/proyectos/detalles/{id}(id=${proyecto.id})}" class="btn btn-info btn-sm">Ver Tareas</a>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    </body>
    </html>

    <!-- src/main/resources/templates/formulario-proyecto.html -->
    <!DOCTYPE html>
    <html xmlns:th="http://www.thymeleaf.org">
    <head>
        <title th:text="${tituloPagina}">Formulario Proyecto</title>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    </head>
    <body>
    <div class="container my-4">
        <h1 th:text="${tituloPagina}"></h1>
        <form th:action="@{/proyectos/guardar}" th:object="${proyecto}" method="post"> <!-- th:object enlaza con el DTO -->
            <div class="form-group">
                <label for="nombre">Nombre del Proyecto:</label>
                <input type="text" id="nombre" th:field="*{nombre}" class="form-control"
                       th:classappend="${#fields.hasErrors('nombre')} ? 'is-invalid' : ''">
                <!-- Mostrar errores de validación -->
                <div class="invalid-feedback" th:if="${#fields.hasErrors('nombre')}" th:errors="*{nombre}">
                    Error de nombre
                </div>
            </div>
            <button type="submit" class="btn btn-success">Guardar Proyecto</button>
            <a th:href="@{/proyectos/lista}" class="btn btn-secondary">Cancelar</a>
        </form>
    </div>
    </body>
    </html>
    ```

3.  **Configuración de FreeMarker/Mustache (Alternativa a Thymeleaf):** Si prefieres Mustache, necesitarías la dependencia `spring-boot-starter-mustache` y la estructura de plantillas con su sintaxis lógica.

    ```html
    <!-- Ejemplo de plantilla Mustache: src/main/resources/templates/articulo.html -->
    <div class="starter-template">
        {{#articulos}} <!-- Itera sobre una colección de 'articulos' -->
        <h1>{{titulo}}</h1> <!-- Muestra el valor de la propiedad 'titulo' -->
        <h3>{{fechaPublicacion}}</h3>
        <h3>{{autor}}</h3>
        <p>{{cuerpo}}</p>
        {{/articulos}}
    </div>
    ```

    El controlador para Mustache devolvería un `ModelAndView` con el nombre de la plantilla y el modelo:

    ```java
    @GetMapping("/articulo")
    public ModelAndView mostrarArticulo(Map<String, Object> modelo) {
        // Lógica para obtener 'articulos' y añadirlos al mapa 'modelo'
        return new ModelAndView("articulo", modelo); // "articulo" es el nombre de la plantilla Mustache
    }
    ```

### **6. Consideraciones Adicionales y Pruebas**

*   **Pruebas de Integración:** Para validar las capas de persistencia y controlador, se pueden escribir pruebas de integración utilizando `@SpringBootTest` y `MockMvc`.

    ```java
    // src/test/java/com/baeldung/casopractico/gestionproyectos/controlador/ControladorProyectoRESTIntegracionTest.java
    package com.baeldung.casopractico.gestionproyectos.controlador;

    import com.baeldung.casopractico.gestionproyectos.Aplicacion;
    import com.baeldung.casopractico.gestionproyectos.dto.ProyectoDTO;
    import com.baeldung.casopractico.gestionproyectos.modelo.Proyecto;
    import com.baeldung.casopractico.gestionproyectos.repositorio.RepositorioProyecto;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.http.MediaType;
    import org.springframework.test.context.junit.jupiter.SpringExtension;
    import org.springframework.test.web.servlet.MockMvc;

    import java.time.LocalDate;

    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
    import static org.hamcrest.Matchers.*;

    @ExtendWith(SpringExtension.class)
    @SpringBootTest(
            webEnvironment = SpringBootTest.WebEnvironment.MOCK,
            classes = Aplicacion.class)
    @AutoConfigureMockMvc
    public class ControladorProyectoRESTIntegracionTest {

        @Autowired
        private MockMvc mvc; // Permite simular solicitudes HTTP

        @Autowired
        private RepositorioProyecto repositorioProyecto;

        @Autowired
        private ObjectMapper objectMapper; // Para convertir objetos Java a JSON

        @BeforeEach
        void configurar() {
            // Limpiar la base de datos antes de cada prueba
            repositorioProyecto.deleteAll();
            // Cargar datos de prueba específicos para el controlador REST
            repositorioProyecto.save(new Proyecto("Proyecto Prueba 1", LocalDate.now()));
            repositorioProyecto.save(new Proyecto("Proyecto Prueba 2", LocalDate.now().minusDays(10)));
        }

        @Test
        void cuandoObtenerTodosLosProyectos_entoncesRetornarListaProyectos() throws Exception {
            mvc.perform(get("/api/proyectos")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2))) // Espera 2 proyectos
                    .andExpect(jsonPath("$.nombre", is("Proyecto Prueba 1")));
        }

        @Test
        void cuandoCrearProyecto_entoncesRetornarProyectoCreado() throws Exception {
            ProyectoDTO nuevoProyectoDTO = new ProyectoDTO(null, "Nuevo Proyecto", LocalDate.now(), null);

            mvc.perform(post("/api/proyectos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(nuevoProyectoDTO)))
                    .andExpect(status().isCreated()) // Espera estado HTTP 201
                    .andExpect(jsonPath("$.nombre", is("Nuevo Proyecto")));
        }

        @Test
        void cuandoCrearProyectoConNombreVacio_entoncesRetornarErrorValidacion() throws Exception {
            ProyectoDTO nuevoProyectoDTO = new ProyectoDTO(null, "", LocalDate.now(), null); // Nombre vacío

            mvc.perform(post("/api/proyectos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(nuevoProyectoDTO)))
                    .andExpect(status().isBadRequest()) // Espera estado HTTP 400
                    .andExpect(jsonPath("$.nombre", is("El nombre del proyecto no puede estar vacío")));
        }

        @Test
        void cuandoEliminarProyecto_entoncesProyectoEliminado() throws Exception {
            Proyecto proyectoExistente = repositorioProyecto.save(new Proyecto("Proyecto a Eliminar", LocalDate.now()));

            mvc.perform(delete("/api/proyectos/{id}", proyectoExistente.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent()); // Espera estado HTTP 204

            // Verificar que el proyecto fue eliminado
            mvc.perform(get("/api/proyectos/{id}", proyectoExistente.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound()); // Espera estado HTTP 404
        }
    }
    ```

Este caso práctico te proporciona una base sólida para construir aplicaciones web con Spring Boot, cubriendo desde la persistencia hasta la capa web con buenas prácticas y ejemplos de código en español, lo cual es invaluable para tu portafolio. ¡Éxito en tu aprendizaje!