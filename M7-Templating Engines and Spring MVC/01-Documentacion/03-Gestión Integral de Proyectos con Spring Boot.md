***
### Visión General del Proyecto

Este proyecto utilizará Spring Boot para simplificar la configuración y el despliegue. Implementaremos un sistema de gestión de proyectos que permitirá crear, listar y visualizar proyectos y sus tareas asociadas. La arquitectura seguirá el patrón MVC (Modelo-Vista-Controlador) para la capa web tradicional y una capa de servicio para la lógica de negocio, interactuando con la capa de persistencia a través de Spring Data JPA. Utilizaremos una base de datos en memoria H2 para facilitar el desarrollo y las pruebas, y Thymeleaf como motor de plantillas para renderizar las vistas HTML dinámicamente.

**Tecnologías Clave:**

*   **Spring Boot:** Para la auto-configuración y la puesta en marcha rápida de la aplicación.
*   **Spring MVC:** Para manejar las peticiones web y estructurar la aplicación bajo el patrón Modelo-Vista-Controlador.
*   **Spring Data JPA:** Para simplificar la capa de persistencia, reduciendo el código repetitivo al interactuar con bases de datos relacionales a través de JPA (Java Persistence API) e Hibernate.
*   **Hibernate:** La implementación de JPA por defecto utilizada por Spring Data JPA para el mapeo objeto-relacional (ORM).
*   **H2 Database:** Una base de datos en memoria que es ideal para el desarrollo y las pruebas.
*   **Thymeleaf:** Un motor de plantillas moderno y robusto para generar contenido HTML dinámico en el lado del servidor.
*   **DTOs (Data Transfer Objects):** Para desacoplar la capa de presentación de la capa de persistencia y mejorar la seguridad y flexibilidad de la API.
*   **Validación:** Para asegurar la integridad de los datos de entrada.
*   **Pruebas de Integración:** Para verificar el correcto funcionamiento de las capas de la aplicación.

Comencemos con la implementación.

---

### 1. Estructura del Proyecto y `pom.xml` (Dependencias Maven)

Primero, crearemos un proyecto Maven y configuraremos las dependencias necesarias en el archivo `pom.xml`.

**Explicación:**
El `spring-boot-starter-parent` gestiona las versiones de dependencias comunes, simplificando su declaración.
*   `spring-boot-starter-web`: Incluye Spring MVC y un servidor web embebido como Tomcat.
*   `spring-boot-starter-data-jpa`: Incluye Spring Data JPA, Hibernate y la API de JPA para la persistencia.
*   `h2`: La base de datos en memoria para desarrollo y pruebas.
*   `spring-boot-starter-thymeleaf`: Para integrar Thymeleaf como motor de plantillas.
*   `spring-boot-starter-test`: Proporciona las herramientas de prueba necesarias para aplicaciones Spring Boot, incluyendo JUnit, Mockito y AssertJ.
*   `spring-boot-starter-validation`: Para la validación de objetos de entrada, que incluye `hibernate-validator`.
*   `modelmapper`: Una biblioteca útil para mapear objetos entre capas, como de entidad a DTO y viceversa.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.2</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>gestion-proyectos-web-integral</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>gestion-proyectos-web-integral</name>
    <description>Aplicacion web para la gestion de proyectos</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <!-- Dependencia para el desarrollo de aplicaciones web con Spring MVC y Tomcat embebido -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- Dependencia para la persistencia de datos con Spring Data JPA y Hibernate -->
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
        <!-- Dependencia para el motor de plantillas Thymeleaf -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <!-- Dependencia para la validación de entradas de usuario -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <!-- Dependencia para las herramientas de desarrollo de Spring Boot (recarga automática, etc.) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <!-- Dependencia para pruebas de Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- Biblioteca para mapeo de objetos, útil para convertir Entidades a DTOs y viceversa -->
        <dependency>
            <groupId>org.modelmapper</groupId>
            <artifactId>modelmapper</artifactId>
            <version>3.2.0</version>
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

---

### 2. Clase Principal de la Aplicación (`Application.java`)

Esta es la clase que arranca tu aplicación Spring Boot. Incluiremos una `CommandLineRunner` para cargar datos iniciales en la base de datos H2 al iniciar la aplicación.

**Explicación:**
*   `@SpringBootApplication`: Una anotación de conveniencia que combina `@Configuration`, `@EnableAutoConfiguration` y `@ComponentScan`. Permite que Spring Boot configure automáticamente el contexto de la aplicación, incluyendo una fuente de datos en memoria H2.
*   `CommandLineRunner`: Una interfaz funcional que te permite ejecutar código justo antes de que la aplicación comience a recibir solicitudes. Es ideal para inicializar datos.

```java
// src/main/java/com/example/gestionproyectos/Application.java
package com.example.gestionproyectos;

import com.example.gestionproyectos.modelos.Proyecto;
import com.example.gestionproyectos.modelos.Tarea;
import com.example.gestionproyectos.modelos.EstadoTarea;
import com.example.gestionproyectos.repositorios.RepositorioProyecto;
import com.example.gestionproyectos.repositorios.RepositorioTarea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootApplication
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Bean para inicializar datos de prueba en la base de datos al inicio de la aplicación.
     * Utiliza CommandLineRunner para ejecutar código después de que el contexto de Spring se haya cargado.
     * @param repositorioProyecto El repositorio de proyectos.
     * @param repositorioTarea El repositorio de tareas.
     * @return Una instancia de CommandLineRunner.
     */
    @Bean
    public CommandLineRunner inicializarDatos(RepositorioProyecto repositorioProyecto, RepositorioTarea repositorioTarea) {
        return args -> {
            LOG.info("Iniciando carga de datos de ejemplo...");

            // Crear algunos proyectos
            Proyecto proyecto1 = new Proyecto("Desarrollo de Aplicación Web", LocalDate.now());
            Proyecto proyecto2 = new Proyecto("Migración a la Nube", LocalDate.now().minusMonths(3));
            Proyecto proyecto3 = new Proyecto("Optimización de Base de Datos", LocalDate.now().plusMonths(1));

            // Añadir tareas al proyecto 1
            Set<Tarea> tareasProyecto1 = new HashSet<>();
            tareasProyecto1.add(new Tarea("Diseñar interfaz de usuario", "Crear mockups y prototipos para la UI.", EstadoTarea.PENDIENTE));
            tareasProyecto1.add(new Tarea("Implementar backend API", "Desarrollar los endpoints REST para la aplicación.", EstadoTarea.EN_PROGRESO));
            tareasProyecto1.add(new Tarea("Configurar base de datos", "Establecer el esquema de la base de datos y la conectividad.", EstadoTarea.COMPLETADA));
            proyecto1.setTareas(tareasProyecto1);
            // Establecer la relación bidireccional
            tareasProyecto1.forEach(tarea -> tarea.setProyecto(proyecto1));


            // Añadir tareas al proyecto 2
            Set<Tarea> tareasProyecto2 = new HashSet<>();
            tareasProyecto2.add(new Tarea("Evaluar proveedores de nube", "Investigar y comparar opciones de proveedores de servicios en la nube.", EstadoTarea.COMPLETADA));
            tareasProyecto2.add(new Tarea("Planificar estrategia de migración", "Definir un plan detallado para la migración.", EstadoTarea.PENDIENTE));
            proyecto2.setTareas(tareasProyecto2);
            tareasProyecto2.forEach(tarea -> tarea.setProyecto(proyecto2));

            // Guardar proyectos y sus tareas asociadas
            repositorioProyecto.save(proyecto1);
            repositorioProyecto.save(proyecto2);
            repositorioProyecto.save(proyecto3);

            LOG.info("Datos de ejemplo cargados exitosamente.");

            // Mostrar todos los proyectos cargados
            LOG.info("Proyectos existentes en la base de datos:");
            repositorioProyecto.findAll().forEach(proyecto -> {
                LOG.info(proyecto.toString());
                proyecto.getTareas().forEach(tarea -> LOG.info("  - Tarea: " + tarea.getNombre() + " (" + tarea.getEstado() + ")"));
            });
        };
    }
}
```

---

### 3. Modelos (Entidades JPA y DTOs)

Definiremos las entidades JPA que mapearán a las tablas de la base de datos y los DTOs para la comunicación con la capa de presentación.

#### 3.1. Entidades JPA

Las entidades representan la estructura de tus datos en la base de datos.
*   `Proyecto.java`: Representa un proyecto.
*   `Tarea.java`: Representa una tarea, asociada a un proyecto.

**Explicación:**
*   `@Entity`: Marca la clase como una entidad JPA.
*   `@Id`: Marca el campo como la clave primaria.
*   `@GeneratedValue`: Configura la estrategia de generación de valores para la clave primaria (aquí `IDENTITY` para auto-incremento de la base de datos, o `AUTO` que delega la estrategia al proveedor de persistencia).
*   `@OneToMany`, `@ManyToOne`: Definen las relaciones entre entidades. `CascadeType.ALL` propaga las operaciones de persistencia, actualización y eliminación de la entidad `Proyecto` a sus `Tarea`s. `FetchType.LAZY` es una buena práctica para cargar colecciones solo cuando son necesarias, evitando problemas de rendimiento.
*   `@JoinColumn`: Especifica la columna de unión en la tabla de la entidad propietaria de la relación (en este caso, `Tarea` tiene `project_id`).
*   Un constructor sin argumentos (protegido o público) es un requisito de la especificación JPA.

```java
// src/main/java/com/example/gestionproyectos/modelos/Proyecto.java
package com.example.gestionproyectos.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank; // Importar NotBlank
import jakarta.validation.constraints.NotNull; // Importar NotNull
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "proyectos") // Mapea la entidad a la tabla 'proyectos' en la base de datos
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID por la base de datos
    private Long id;

    @NotBlank(message = "El nombre del proyecto no puede estar en blanco") // Validación: el nombre no puede ser nulo o solo espacios
    @Column(nullable = false) // Columna no nula en la base de datos
    private String nombre;

    @NotNull(message = "La fecha de creación no puede ser nula") // Validación: la fecha no puede ser nula
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    // Relación OneToMany: Un proyecto puede tener muchas tareas
    // CascadeType.ALL: Las operaciones (persist, merge, remove) se propagan a las entidades Tarea
    // orphanRemoval = true: Si una tarea se elimina de la colección, se elimina de la base de datos
    // mappedBy = "proyecto": Indica que la relación es bidireccional y la entidad Tarea es la dueña de la relación
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Tarea> tareas = new HashSet<>();

    /**
     * Constructor por defecto requerido por JPA.
     */
    protected Proyecto() {
    }

    /**
     * Constructor para crear un nuevo proyecto.
     * @param nombre El nombre del proyecto.
     * @param fechaCreacion La fecha de creación del proyecto.
     */
    public Proyecto(String nombre, LocalDate fechaCreacion) {
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters

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
        this.tareas.clear();
        if (tareas != null) {
            this.tareas.addAll(tareas);
            this.tareas.forEach(tarea -> tarea.setProyecto(this)); // Asegurar la relación bidireccional
        }
    }

    public void agregarTarea(Tarea tarea) {
        this.tareas.add(tarea);
        tarea.setProyecto(this); // Establecer la relación bidireccional
    }

    public void eliminarTarea(Tarea tarea) {
        this.tareas.remove(tarea);
        tarea.setProyecto(null); // Desvincular la tarea del proyecto
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proyecto proyecto = (Proyecto) o;
        // Para equals y hashCode, es recomendable usar una clave de negocio inmutable (si existe)
        // o el ID si se asegura que es generado antes de que la entidad se use en colecciones.
        // En este ejemplo simple, usaremos el ID.
        return Objects.equals(id, proyecto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
```

```java
// src/main/java/com/example/gestionproyectos/modelos/Tarea.java
package com.example.gestionproyectos.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank; // Importar NotBlank
import jakarta.validation.constraints.NotNull; // Importar NotNull
import java.util.Objects;

@Entity
@Table(name = "tareas") // Mapea la entidad a la tabla 'tareas' en la base de datos
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID
    private Long id;

    @NotBlank(message = "El nombre de la tarea no puede estar en blanco")
    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @NotNull(message = "El estado de la tarea no puede ser nulo")
    @Enumerated(EnumType.STRING) // Almacena el nombre del enum como String en la base de datos
    @Column(nullable = false)
    private EstadoTarea estado;

    // Relación ManyToOne: Muchas tareas pueden pertenecer a un proyecto
    // fetch = FetchType.LAZY: Carga el proyecto asociado solo cuando es necesario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false) // Columna de clave foránea en la tabla 'tareas'
    private Proyecto proyecto;

    /**
     * Constructor por defecto requerido por JPA.
     */
    protected Tarea() {
    }

    /**
     * Constructor para crear una nueva tarea.
     * @param nombre El nombre de la tarea.
     * @param descripcion La descripción de la tarea.
     * @param estado El estado actual de la tarea.
     */
    public Tarea(String nombre, String descripcion, EstadoTarea estado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    // Getters y Setters

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarea tarea = (Tarea) o;
        return Objects.equals(id, tarea.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado=" + estado +
                '}';
    }
}
```

```java
// src/main/java/com/example/gestionproyectos/modelos/EstadoTarea.java
package com.example.gestionproyectos.modelos;

/**
 * Enumeración para representar el estado de una tarea.
 */
public enum EstadoTarea {
    PENDIENTE,
    EN_PROGRESO,
    COMPLETADA,
    CANCELADA
}
```

#### 3.2. DTOs (Data Transfer Objects)

Los DTOs se utilizan para transferir datos entre capas de la aplicación. Son una buena práctica para evitar exponer las entidades de base de datos directamente a la capa de presentación.

**Explicación:**
*   `ProyectoCrearDTO`: Utilizado cuando se envía un formulario para crear un nuevo proyecto.
*   `ProyectoVerDTO`: Utilizado para la visualización de un proyecto, puede contener solo los datos relevantes para la vista, incluyendo un DTO para las tareas.
*   `TareaVerDTO`: DTO para la visualización de una tarea.
*   `ModelMapper`: Se usa para simplificar la conversión entre entidades y DTOs, aunque también se puede hacer manualmente.

```java
// src/main/java/com/example/gestionproyectos/modelos/ProyectoCrearDTO.java
package com.example.gestionproyectos.modelos;

import jakarta.validation.constraints.NotBlank; // Importar NotBlank

/**
 * DTO (Data Transfer Object) para la creación de un nuevo proyecto.
 * Contiene solo los campos necesarios para la entrada del usuario al crear un proyecto.
 */
public class ProyectoCrearDTO {

    @NotBlank(message = "El nombre del proyecto no puede estar en blanco") // Validación: el nombre es obligatorio
    private String nombre;

    // Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
```

```java
// src/main/java/com/example/gestionproyectos/modelos/ProyectoVerDTO.java
package com.example.gestionproyectos.modelos;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO (Data Transfer Object) para la visualización de un proyecto.
 * Contiene los campos necesarios para mostrar los detalles de un proyecto.
 */
public class ProyectoVerDTO {

    private Long id;
    private String nombre;
    private LocalDate fechaCreacion;
    private Set<TareaVerDTO> tareas; // Las tareas también se representan con su DTO

    // Getters y Setters

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

    public Set<TareaVerDTO> getTareas() {
        return tareas;
    }

    public void setTareas(Set<TareaVerDTO> tareas) {
        this.tareas = tareas;
    }
}
```

```java
// src/main/java/com/example/gestionproyectos/modelos/TareaVerDTO.java
package com.example.gestionproyectos.modelos;

/**
 * DTO (Data Transfer Object) para la visualización de una tarea.
 */
public class TareaVerDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private EstadoTarea estado;

    // Getters y Setters

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }
}
```

```java
// src/main/java/com/example/gestionproyectos/utilidades/MapeadorDeProyectos.java
package com.example.gestionproyectos.utilidades;

import com.example.gestionproyectos.modelos.Proyecto;
import com.example.gestionproyectos.modelos.ProyectoVerDTO;
import com.example.gestionproyectos.modelos.Tarea;
import com.example.gestionproyectos.modelos.TareaVerDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

/**
 * Clase de configuración para el mapeo de Entidades a DTOs y viceversa.
 * Se utiliza ModelMapper para simplificar este proceso.
 */
@Configuration
public class MapeadorDeProyectos {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configuración para mapear Proyecto a ProyectoVerDTO
        modelMapper.createTypeMap(Proyecto.class, ProyectoVerDTO.class)
                .addMapping(Proyecto::getTareas, ProyectoVerDTO::setTareas); // Mapea la colección de tareas

        // Configuración para mapear Tarea a TareaVerDTO
        modelMapper.createTypeMap(Tarea.class, TareaVerDTO.class);

        return modelMapper;
    }

    // Métodos utilitarios para conversión si no se desea inyectar ModelMapper en todas partes
    // Opcional: se puede usar modelMapper directamente en los servicios/controladores
    public static ProyectoVerDTO convertirAProyectoVerDTO(Proyecto proyecto, ModelMapper mapper) {
        if (proyecto == null) {
            return null;
        }
        ProyectoVerDTO dto = mapper.map(proyecto, ProyectoVerDTO.class);
        if (proyecto.getTareas() != null) {
            dto.setTareas(proyecto.getTareas().stream()
                    .map(tarea -> mapper.map(tarea, TareaVerDTO.class))
                    .collect(Collectors.toSet()));
        }
        return dto;
    }

    public static TareaVerDTO convertirATareaVerDTO(Tarea tarea, ModelMapper mapper) {
        if (tarea == null) {
            return null;
        }
        return mapper.map(tarea, TareaVerDTO.class);
    }
}
```

---

### 4. Capa de Persistencia (Repositorios)

Spring Data JPA simplifica enormemente la implementación de la capa de acceso a datos al generar automáticamente implementaciones de repositorios.

**Explicación:**
*   `JpaRepository`: Una interfaz de Spring Data JPA que proporciona métodos CRUD (Crear, Leer, Actualizar, Eliminar) básicos, así como capacidades de paginación y ordenación. Extiende `PagingAndSortingRepository` y `CrudRepository`.
*   Métodos de consulta derivados: Spring Data JPA puede generar consultas automáticamente basándose en los nombres de los métodos del repositorio (por ejemplo, `findByNombre` se traduce en una consulta SQL para buscar por el campo `nombre`).

```java
// src/main/java/com/example/gestionproyectos/repositorios/RepositorioProyecto.java
package com.example.gestionproyectos.repositorios;

import com.example.gestionproyectos.modelos.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Marca esta interfaz como un componente de repositorio de Spring
public interface RepositorioProyecto extends JpaRepository<Proyecto, Long> {

    /**
     * Encuentra un proyecto por su nombre.
     * Spring Data JPA generará automáticamente la implementación de esta consulta
     * basándose en el nombre del método.
     * @param nombre El nombre del proyecto a buscar.
     * @return Un Optional que contiene el proyecto si se encuentra, o vacío si no.
     */
    Optional<Proyecto> findByNombre(String nombre);
}
```

```java
// src/main/java/com/example/gestionproyectos/repositorios/RepositorioTarea.java
package com.example.gestionproyectos.repositorios;

import com.example.gestionproyectos.modelos.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioTarea extends JpaRepository<Tarea, Long> {
    // Spring Data JPA proveerá las implementaciones CRUD básicas automáticamente.
    // Se pueden añadir métodos de consulta derivados aquí si son necesarios.
}
```

---

### 5. Capa de Servicio

La capa de servicio contiene la lógica de negocio y coordina las operaciones con la capa de persistencia.

**Explicación:**
*   `@Service`: Marca la clase como un componente de servicio de Spring.
*   `@Transactional`: Asegura que los métodos se ejecuten dentro de una transacción de base de datos. Si un método falla, todos los cambios realizados dentro de la transacción se revierten (rollback).

```java
// src/main/java/com/example/gestionproyectos/servicios/ServicioProyecto.java
package com.example.gestionproyectos.servicios;

import com.example.gestionproyectos.modelos.Proyecto;
import com.example.gestionproyectos.modelos.ProyectoCrearDTO;
import com.example.gestionproyectos.modelos.ProyectoVerDTO;
import com.example.gestionproyectos.modelos.Tarea;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la gestión de proyectos.
 * Define las operaciones de negocio relacionadas con los proyectos.
 */
public interface ServicioProyecto {
    /**
     * Crea un nuevo proyecto a partir de un DTO de creación.
     * @param proyectoDTO El DTO que contiene los datos del nuevo proyecto.
     * @return El proyecto creado.
     */
    Proyecto crearProyecto(ProyectoCrearDTO proyectoDTO);

    /**
     * Obtiene todos los proyectos.
     * @return Una lista de todos los proyectos.
     */
    List<Proyecto> obtenerTodosLosProyectos();

    /**
     * Obtiene un proyecto por su ID.
     * @param id El ID del proyecto.
     * @return Un Optional que contiene el proyecto si se encuentra, o vacío si no.
     */
    Optional<Proyecto> obtenerProyectoPorId(Long id);

    /**
     * Actualiza un proyecto existente.
     * @param proyecto El proyecto a actualizar.
     * @return El proyecto actualizado.
     */
    Proyecto actualizarProyecto(Proyecto proyecto);

    /**
     * Elimina un proyecto por su ID.
     * @param id El ID del proyecto a eliminar.
     */
    void eliminarProyecto(Long id);

    /**
     * Agrega una tarea a un proyecto específico.
     * @param proyectoId El ID del proyecto.
     * @param tarea La tarea a agregar.
     * @return El proyecto actualizado con la nueva tarea.
     */
    Optional<Proyecto> agregarTareaAProyecto(Long proyectoId, Tarea tarea);
}
```

```java
// src/main/java/com/example/gestionproyectos/servicios/ImplementacionServicioProyecto.java
package com.example.gestionproyectos.servicios;

import com.example.gestionproyectos.modelos.Proyecto;
import com.example.gestionproyectos.modelos.ProyectoCrearDTO;
import com.example.gestionproyectos.modelos.Tarea;
import com.example.gestionproyectos.repositorios.RepositorioProyecto;
import com.example.gestionproyectos.repositorios.RepositorioTarea;
import jakarta.transaction.Transactional; // Importar jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un componente de servicio de Spring
public class ImplementacionServicioProyecto implements ServicioProyecto {

    private final RepositorioProyecto repositorioProyecto;
    private final RepositorioTarea repositorioTarea;

    @Autowired // Inyección de dependencias de los repositorios
    public ImplementacionServicioProyecto(RepositorioProyecto repositorioProyecto, RepositorioTarea repositorioTarea) {
        this.repositorioProyecto = repositorioProyecto;
        this.repositorioTarea = repositorioTarea;
    }

    @Override
    @Transactional // Asegura que la operación se ejecute dentro de una transacción
    public Proyecto crearProyecto(ProyectoCrearDTO proyectoDTO) {
        // En un escenario real, podríamos validar si ya existe un proyecto con el mismo nombre.
        // Por simplicidad, aquí solo creamos y guardamos.
        Proyecto nuevoProyecto = new Proyecto(proyectoDTO.getNombre(), LocalDate.now());
        return repositorioProyecto.save(nuevoProyecto); // Guarda el nuevo proyecto en la base de datos
    }

    @Override
    public List<Proyecto> obtenerTodosLosProyectos() {
        return repositorioProyecto.findAll(); // Obtiene todos los proyectos de la base de datos
    }

    @Override
    public Optional<Proyecto> obtenerProyectoPorId(Long id) {
        return repositorioProyecto.findById(id); // Busca un proyecto por su ID
    }

    @Override
    @Transactional
    public Proyecto actualizarProyecto(Proyecto proyecto) {
        // Antes de actualizar, verificar que el proyecto exista
        if (!repositorioProyecto.existsById(proyecto.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proyecto no encontrado con ID: " + proyecto.getId());
        }
        return repositorioProyecto.save(proyecto); // Actualiza el proyecto
    }

    @Override
    @Transactional
    public void eliminarProyecto(Long id) {
        // Antes de eliminar, verificar que el proyecto exista
        if (!repositorioProyecto.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proyecto no encontrado con ID: " + id);
        }
        repositorioProyecto.deleteById(id); // Elimina el proyecto por su ID
    }

    @Override
    @Transactional
    public Optional<Proyecto> agregarTareaAProyecto(Long proyectoId, Tarea tarea) {
        Optional<Proyecto> proyectoOptional = repositorioProyecto.findById(proyectoId);
        if (proyectoOptional.isPresent()) {
            Proyecto proyecto = proyectoOptional.get();
            proyecto.agregarTarea(tarea); // Lógica de negocio para agregar la tarea al proyecto
            return Optional.of(repositorioProyecto.save(proyecto)); // Guarda el proyecto (y las tareas en cascada)
        }
        return Optional.empty();
    }
}
```

---

### 6. Capa Web (Controladores y Vistas con Thymeleaf)

La capa web es la interfaz de usuario de nuestra aplicación. Usaremos Spring MVC para manejar las peticiones HTTP y Thymeleaf para renderizar el HTML.

#### 6.1. Controlador Web (`ControladorProyectoWeb.java`)

Este controlador se encargará de las solicitudes relacionadas con los proyectos y de interactuar con el servicio para preparar los datos que se mostrarán en las vistas.

**Explicación:**
*   `@Controller`: Marca esta clase como un controlador Spring MVC, responsable de manejar las solicitudes web.
*   `@RequestMapping("/proyectos")`: Define el mapeo base para todas las rutas dentro de este controlador.
*   `@GetMapping`, `@PostMapping`: Mapean los métodos a las solicitudes HTTP GET y POST respectivamente.
*   `Model`: Un objeto que permite pasar datos del controlador a la vista.
*   `@ModelAttribute`: Se utiliza para vincular un objeto DTO a un formulario en la vista y para que Spring lo instancie y lo rellene con los datos del formulario.
*   `@Valid`: Activa la validación de los campos del DTO según las anotaciones de validación (ej., `@NotBlank`).
*   `BindingResult`: Contiene los resultados de la validación. Si hay errores, se pueden manejar aquí.
*   `RedirectAttributes`: Permite añadir atributos a la URL de redirección (ej., mensajes flash).

```java
// src/main/java/com/example/gestionproyectos/controladores/ControladorProyectoWeb.java
package com.example.gestionproyectos.controladores;

import com.example.gestionproyectos.modelos.Proyecto;
import com.example.gestionproyectos.modelos.ProyectoCrearDTO;
import com.example.gestionproyectos.modelos.ProyectoVerDTO;
import com.example.gestionproyectos.servicios.ServicioProyecto;
import com.example.gestionproyectos.utilidades.MapeadorDeProyectos;
import jakarta.validation.Valid; // Importar jakarta.validation.Valid
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller // Marca esta clase como un controlador Spring MVC
@RequestMapping("/proyectos") // Mapea todas las solicitudes a /proyectos
public class ControladorProyectoWeb {

    private static final Logger LOG = LoggerFactory.getLogger(ControladorProyectoWeb.class);

    private final ServicioProyecto servicioProyecto;
    private final ModelMapper modelMapper; // Para mapear entidades a DTOs

    @Autowired
    public ControladorProyectoWeb(ServicioProyecto servicioProyecto, ModelMapper modelMapper) {
        this.servicioProyecto = servicioProyecto;
        this.modelMapper = modelMapper;
    }

    /**
     * Muestra la lista de todos los proyectos.
     * @param modelo El objeto Model para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para listar proyectos.
     */
    @GetMapping // Maneja solicitudes GET a /proyectos
    public String listarProyectos(Model modelo) {
        LOG.info("Listando todos los proyectos...");
        List<Proyecto> proyectos = servicioProyecto.obtenerTodosLosProyectos();
        List<ProyectoVerDTO> proyectosDTO = proyectos.stream()
                .map(proyecto -> MapeadorDeProyectos.convertirAProyectoVerDTO(proyecto, modelMapper))
                .collect(Collectors.toList());
        modelo.addAttribute("proyectos", proyectosDTO); // Añade la lista de DTOs al modelo
        return "proyectos/listaProyectos"; // Retorna el nombre de la plantilla
    }

    /**
     * Muestra el formulario para crear un nuevo proyecto.
     * @param modelo El objeto Model.
     * @return El nombre de la plantilla Thymeleaf para el formulario de creación.
     */
    @GetMapping("/nuevo") // Maneja solicitudes GET a /proyectos/nuevo
    public String mostrarFormularioCreacion(Model modelo) {
        LOG.info("Mostrando formulario para nuevo proyecto...");
        modelo.addAttribute("proyectoCrearDTO", new ProyectoCrearDTO()); // Añade un objeto DTO vacío al modelo para el formulario
        return "proyectos/crearProyecto"; // Retorna el nombre de la plantilla del formulario
    }

    /**
     * Procesa la solicitud para crear un nuevo proyecto.
     * @param proyectoCrearDTO El DTO que contiene los datos del formulario (validado).
     * @param resultadosValidacion Objeto que contiene los resultados de la validación.
     * @param atributosRedireccion Para añadir atributos flash en la redirección.
     * @return Una redirección a la lista de proyectos o al formulario si hay errores.
     */
    @PostMapping("/nuevo") // Maneja solicitudes POST a /proyectos/nuevo
    public String crearProyecto(@Valid @ModelAttribute("proyectoCrearDTO") ProyectoCrearDTO proyectoCrearDTO,
                                BindingResult resultadosValidacion,
                                RedirectAttributes atributosRedireccion) {
        LOG.info("Intentando crear nuevo proyecto: {}", proyectoCrearDTO.getNombre());

        if (resultadosValidacion.hasErrors()) {
            LOG.warn("Errores de validación al crear proyecto: {}", resultadosValidacion.getAllErrors());
            return "proyectos/crearProyecto"; // Si hay errores, vuelve a mostrar el formulario
        }

        servicioProyecto.crearProyecto(proyectoCrearDTO); // Llama al servicio para crear el proyecto
        atributosRedireccion.addFlashAttribute("mensajeExito", "Proyecto creado exitosamente!"); // Mensaje flash para la redirección
        LOG.info("Proyecto '{}' creado exitosamente.", proyectoCrearDTO.getNombre());
        return "redirect:/proyectos"; // Redirecciona a la lista de proyectos
    }

    /**
     * Muestra los detalles de un proyecto específico.
     * @param id El ID del proyecto.
     * @param modelo El objeto Model.
     * @param atributosRedireccion Para añadir atributos flash en caso de proyecto no encontrado.
     * @return El nombre de la plantilla Thymeleaf para los detalles del proyecto o una redirección.
     */
    @GetMapping("/{id}") // Maneja solicitudes GET a /proyectos/{id}
    public String verDetallesProyecto(@PathVariable Long id, Model modelo, RedirectAttributes atributosRedireccion) {
        LOG.info("Buscando detalles del proyecto con ID: {}", id);
        Optional<Proyecto> proyectoOptional = servicioProyecto.obtenerProyectoPorId(id);
        if (proyectoOptional.isPresent()) {
            Proyecto proyecto = proyectoOptional.get();
            ProyectoVerDTO proyectoVerDTO = MapeadorDeProyectos.convertirAProyectoVerDTO(proyecto, modelMapper);
            modelo.addAttribute("proyecto", proyectoVerDTO); // Añade el DTO del proyecto al modelo
            return "proyectos/detallesProyecto"; // Retorna la plantilla de detalles
        } else {
            LOG.warn("Proyecto con ID {} no encontrado.", id);
            atributosRedireccion.addFlashAttribute("mensajeError", "Proyecto no encontrado!");
            return "redirect:/proyectos"; // Redirecciona a la lista si no se encuentra
        }
    }
}
```

#### 6.2. Vistas Thymeleaf

Las vistas son archivos HTML que utilizan la sintaxis de Thymeleaf para mostrar datos dinámicamente. Estos archivos se guardan típicamente en `src/main/resources/templates/`.

**Explicación:**
*   `th:text`, `th:each`, `th:field`, `th:href`, `th:object`: Son atributos de Thymeleaf que permiten inyectar datos, iterar sobre colecciones, vincular campos de formulario, generar URLs y referenciar objetos del modelo respectivamente.
*   `layout:fragment`: Una característica de Thymeleaf para reutilizar partes de plantillas (no se implementa una plantilla base completa aquí para brevedad, pero es una buena práctica).

`src/main/resources/templates/proyectos/listaProyectos.html`

```html
<!DOCTYPE html>
<html lang="es" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Proyectos</title>
    <!-- Incluir Bootstrap para un estilo básico, no es parte de las fuentes pero es común -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { padding-top: 20px; }
        .container { max-width: 960px; }
        table { margin-top: 20px; }
        .alert { margin-top: 20px; }
    </style>
</head>
<body>
<div class="container">
    <h1 class="mb-4">Gestión de Proyectos</h1>

    <!-- Mensajes flash (ej. éxito, error) -->
    <div th:if="${mensajeExito}" class="alert alert-success" role="alert">
        <span th:text="${mensajeExito}"></span>
    </div>
    <div th:if="${mensajeError}" class="alert alert-danger" role="alert">
        <span th:text="${mensajeError}"></span>
    </div>

    <a th:href="@{/proyectos/nuevo}" class="btn btn-primary mb-3">Crear Nuevo Proyecto</a>

    <h2 class="mt-4">Proyectos Existentes</h2>
    <table class="table table-striped table-bordered">
        <thead class="thead-dark">
        <tr>
            <th>ID</th>
            <th>Nombre del Proyecto</th>
            <th>Fecha de Creación</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <tr th:each="proyecto : ${proyectos}">
            <td th:text="${proyecto.id}">ID</td>
            <td>
                <!-- Enlace a los detalles del proyecto -->
                <a th:href="@{/proyectos/{id}(id=${proyecto.id})}" th:text="${proyecto.nombre}">Nombre del Proyecto</a>
            </td>
            <td th:text="${#temporals.format(proyecto.fechaCreacion, 'dd-MM-yyyy')}">Fecha de Creación</td>
            <td>
                <!-- Aquí se podrían añadir botones para editar o eliminar -->
                <!-- <a th:href="@{/proyectos/editar/{id}(id=${proyecto.id})}" class="btn btn-sm btn-info">Editar</a> -->
                <!-- <a th:href="@{/proyectos/eliminar/{id}(id=${proyecto.id})}" class="btn btn-sm btn-danger">Eliminar</a> -->
            </td>
        </tr>
        </tbody>
    </table>
    <div th:if="${#lists.isEmpty(proyectos)}" class="alert alert-info">
        No hay proyectos registrados. ¡Crea uno nuevo!
    </div>
</div>
</body>
</html>
```

`src/main/resources/templates/proyectos/crearProyecto.html`

```html
<!DOCTYPE html>
<html lang="es" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Nuevo Proyecto</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { padding-top: 20px; }
        .container { max-width: 600px; }
        .form-group { margin-bottom: 1rem; }
        .error-message { color: red; font-size: 0.875em; }
    </style>
</head>
<body>
<div class="container">
    <h1 class="mb-4">Crear Nuevo Proyecto</h1>

    <!-- Formulario para la creación de un proyecto -->
    <form th:action="@{/proyectos/nuevo}" th:object="${proyectoCrearDTO}" method="post">
        <div class="form-group">
            <label for="nombre">Nombre del Proyecto:</label>
            <input type="text" id="nombre" class="form-control" th:field="*{nombre}" placeholder="Introduce el nombre del proyecto" />
            <!-- Muestra el mensaje de error de validación para el campo 'nombre' -->
            <p th:if="${#fields.hasErrors('nombre')}" th:errors="*{nombre}" class="error-message"></p>
        </div>

        <button type="submit" class="btn btn-success">Guardar Proyecto</button>
        <a th:href="@{/proyectos}" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
</body>
</html>
```

`src/main/resources/templates/proyectos/detallesProyecto.html`

```html
<!DOCTYPE html>
<html lang="es" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalles del Proyecto</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { padding-top: 20px; }
        .container { max-width: 960px; }
        .card { margin-top: 20px; }
        .list-group-item { display: flex; justify-content: space-between; align-items: center; }
        .badge { font-size: 0.9em; }
    </style>
</head>
<body>
<div class="container">
    <h1 class="mb-4">Detalles del Proyecto</h1>

    <div th:if="${proyecto}" class="card">
        <div class="card-header bg-primary text-white">
            <h2 th:text="${proyecto.nombre}">Nombre del Proyecto</h2>
        </div>
        <div class="card-body">
            <p><strong>ID:</strong> <span th:text="${proyecto.id}"></span></p>
            <p><strong>Fecha de Creación:</strong> <span th:text="${#temporals.format(proyecto.fechaCreacion, 'dd-MM-yyyy')}"></span></p>

            <h3 class="mt-4">Tareas Asociadas</h3>
            <div th:if="${#lists.isEmpty(proyecto.tareas)}" class="alert alert-info">
                No hay tareas asociadas a este proyecto.
            </div>
            <ul class="list-group">
                <li th:each="tarea : ${proyecto.tareas}" class="list-group-item">
                    <div>
                        <h5 th:text="${tarea.nombre}">Nombre Tarea</h5>
                        <p th:text="${tarea.descripcion}">Descripción Tarea</p>
                    </div>
                    <span th:classappend="${tarea.estado == T(com.example.gestionproyectos.modelos.EstadoTarea).COMPLETADA ? 'badge-success' : (tarea.estado == T(com.example.gestionproyectos.modelos.EstadoTarea).EN_PROGRESO ? 'badge-warning' : 'badge-info')}"
                          class="badge badge-pill" th:text="${tarea.estado}">Estado Tarea</span>
                </li>
            </ul>
        </div>
    </div>
    <div th:unless="${proyecto}" class="alert alert-danger mt-4">
        Proyecto no encontrado.
    </div>

    <a th:href="@{/proyectos}" class="btn btn-secondary mt-3">Volver a la Lista de Proyectos</a>
</div>
</body>
</html>
```

---

### 7. Configuración de la Aplicación (`application.properties`)

Este archivo define propiedades de configuración para Spring Boot, como la base de datos.

**Explicación:**
*   `spring.datasource.url=jdbc:h2:mem:gestionproyectosdb;DB_CLOSE_DELAY=-1`: Configura una base de datos H2 en memoria con el nombre `gestionproyectosdb`. `DB_CLOSE_DELAY=-1` mantiene la base de datos activa hasta que la JVM se detenga, incluso si todas las conexiones se cierran.
*   `spring.jpa.hibernate.ddl-auto=update`: Le dice a Hibernate que actualice el esquema de la base de datos automáticamente si hay cambios en las entidades. Para producción, `validate` o `none` son más seguros.
*   `spring.jpa.show-sql=true`: Muestra las sentencias SQL generadas por Hibernate en la consola, útil para depuración.
*   `spring.jpa.properties.hibernate.format_sql=true`: Formatea el SQL mostrado para una mejor legibilidad.
*   `logging.level.org.hibernate.SQL=DEBUG`: Habilita el log de las sentencias SQL ejecutadas por Hibernate.

`src/main/resources/application.properties`

```properties
# Configuración de la base de datos H2 en memoria
spring.datasource.url=jdbc:h2:mem:gestionproyectosdb;DB_CLOSE_DELAY=-1
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

# Configuración de JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update # Permite a Hibernate actualizar el esquema de la DB automáticamente
spring.jpa.show-sql=true # Muestra el SQL generado por Hibernate en la consola
spring.jpa.properties.hibernate.format_sql=true # Formatea el SQL para mejor legibilidad

# Configuración del logging
logging.level.org.hibernate.SQL=DEBUG # Muestra el SQL ejecutado
logging.level.org.springframework.web=INFO
logging.level.com.example.gestionproyectos=DEBUG # Nivel de log para tus propias clases

# Configuración del servidor (opcional, si quieres cambiar el puerto por defecto)
server.port=8080

# Configuración de Thymeleaf (valores por defecto de Spring Boot)
# spring.thymeleaf.prefix=classpath:/templates/
# spring.thymeleaf.suffix=.html
# spring.thymeleaf.mode=HTML
# spring.thymeleaf.cache=false # Deshabilitar cache en desarrollo para ver cambios instantáneamente
```

---

### 8. Pruebas (Tests de Integración)

Las pruebas son una parte crucial de cualquier proyecto de software. Spring Boot facilita la escritura de pruebas de integración que pueden arrancar el contexto de Spring.

**Explicación:**
*   `@SpringBootTest`: Arranca el contexto completo de la aplicación Spring Boot. Es útil para pruebas de integración que involucran múltiples capas.
    *   `webEnvironment = SpringBootTest.WebEnvironment.MOCK`: Configura un entorno web simulado, lo que significa que no se levanta un servidor HTTP real, sino un entorno simulado para probar controladores web.
*   `@DataJpaTest`: Una anotación especializada para pruebas de la capa de JPA. Configura un `DataSource` en memoria (si no hay uno definido) y un `TestEntityManager`. Las pruebas `@DataJpaTest` son transaccionales por defecto y se revierten al finalizar, lo que asegura que cada prueba comienza con un estado de base de datos limpio.
*   `TestEntityManager`: Una alternativa de Spring Boot al `EntityManager` estándar de JPA, diseñada específicamente para pruebas. Permite operaciones de persistencia que se adhieren al ciclo de vida de la transacción de prueba.
*   `@WebMvcTest`: Se utiliza para probar solo la capa web (controladores). No arranca todo el contexto de la aplicación, sino solo los componentes relacionados con Spring MVC. Es más rápido que `@SpringBootTest` para pruebas de controlador.
*   `MockMvc`: Proporciona una forma de simular solicitudes HTTP al controlador, permitiendo verificar respuestas y comportamientos sin la necesidad de levantar un servidor real.

#### 8.1. Pruebas de Repositorio

```java
// src/test/java/com/example/gestionproyectos/repositorios/RepositorioProyectoIntegracionTest.java
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
```

#### 8.2. Pruebas de Controlador Web

```java
// src/test/java/com/example/gestionproyectos/controladores/ControladorProyectoWebTest.java
package com.example.gestionproyectos.controladores;

import com.example.gestionproyectos.modelos.EstadoTarea;
import com.example.gestionproyectos.modelos.Proyecto;
import com.example.gestionproyectos.modelos.Tarea;
import com.example.gestionproyectos.servicios.ServicioProyecto;
import com.example.gestionproyectos.utilidades.MapeadorDeProyectos;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.*; // Importar matchers para JSONPath
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControladorProyectoWeb.class) // Prueba solo la capa web, enfocándose en ControladorProyectoWeb
@Import(MapeadorDeProyectos.class) // Importar la configuración del ModelMapper
public class ControladorProyectoWebTest {

    @Autowired
    private MockMvc mvc; // Objeto para simular peticiones HTTP

    @MockBean // Simula el servicio para que el test no dependa de la capa de servicio real
    private ServicioProyecto servicioProyecto;

    @Autowired
    private ModelMapper modelMapper; // El ModelMapper real es inyectado gracias a @Import

    @Test
    void cuandoListarProyectos_entoncesRetornaVistaConProyectos() throws Exception {
        // Dado
        Proyecto proyecto1 = new Proyecto("Proyecto Uno", LocalDate.now());
        proyecto1.setId(1L);
        Proyecto proyecto2 = new Proyecto("Proyecto Dos", LocalDate.now());
        proyecto2.setId(2L);

        when(servicioProyecto.obtenerTodosLosProyectos()).thenReturn(Arrays.asList(proyecto1, proyecto2));

        // Cuando y Entonces
        mvc.perform(get("/proyectos")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk()) // Espera un estado HTTP 200 OK
                .andExpect(view().name("proyectos/listaProyectos")) // Espera que se resuelva a la vista correcta
                .andExpect(model().attributeExists("proyectos")) // Espera que el modelo contenga un atributo "proyectos"
                .andExpect(model().attribute("proyectos", hasSize(2))) // Espera que la lista tenga 2 elementos
                .andExpect(content().string(containsString("Proyecto Uno"))) // Verifica que el contenido HTML contenga el nombre del proyecto
                .andExpect(content().string(containsString("Proyecto Dos")));
    }

    @Test
    void cuandoMostrarFormularioCreacion_entoncesRetornaVistaDeFormulario() throws Exception {
        mvc.perform(get("/proyectos/nuevo"))
                .andExpect(status().isOk())
                .andExpect(view().name("proyectos/crearProyecto"))
                .andExpect(model().attributeExists("proyectoCrearDTO")) // Espera que el modelo contenga el DTO para el formulario
                .andExpect(model().attribute("proyectoCrearDTO", hasProperty("nombre", nullValue()))); // El nombre debe ser nulo inicialmente
    }

    @Test
    void cuandoCrearProyectoValido_entoncesRedireccionaYGuarda() throws Exception {
        // Dado
        Proyecto proyectoGuardado = new Proyecto("Nuevo Proyecto", LocalDate.now());
        proyectoGuardado.setId(1L);
        given(servicioProyecto.crearProyecto(any(ProyectoCrearDTO.class))).willReturn(proyectoGuardado);
        // Usamos BDDMockito.given para simular el comportamiento del servicio

        // Cuando y Entonces
        mvc.perform(post("/proyectos/nuevo")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED) // Tipo de contenido para un formulario HTML
                        .param("nombre", "Nuevo Proyecto")) // Parámetro del formulario
                .andExpect(status().is3xxRedirection()) // Espera una redirección (HTTP 302 Found)
                .andExpect(redirectedUrl("/proyectos")) // Espera que redireccione a /proyectos
                .andExpect(flash().attributeExists("mensajeExito")); // Espera un mensaje flash de éxito
    }

    @Test
    void cuandoCrearProyectoInvalido_entoncesRetornaFormularioConErrores() throws Exception {
        // Cuando y Entonces (nombre vacío, lo cual es inválido)
        mvc.perform(post("/proyectos/nuevo")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nombre", "")) // Nombre en blanco
                .andExpect(status().isOk()) // Espera un estado HTTP 200 OK (vuelve a mostrar el formulario)
                .andExpect(view().name("proyectos/crearProyecto"))
                .andExpect(model().attributeHasErrors("proyectoCrearDTO")) // Espera errores de validación en el DTO
                .andExpect(model().attributeHasFieldErrors("proyectoCrearDTO", "nombre")); // Error específico en el campo 'nombre'
    }

    @Test
    void cuandoVerDetallesProyectoExistente_entoncesRetornaVistaConDetalles() throws Exception {
        // Dado
        Proyecto proyecto = new Proyecto("Proyecto con Tareas", LocalDate.now());
        proyecto.setId(1L);
        Set<Tarea> tareas = new HashSet<>();
        tareas.add(new Tarea("Tarea 1", "Descripción 1", EstadoTarea.PENDIENTE));
        tareas.add(new Tarea("Tarea 2", "Descripción 2", EstadoTarea.COMPLETADA));
        proyecto.setTareas(tareas);

        when(servicioProyecto.obtenerProyectoPorId(1L)).thenReturn(Optional.of(proyecto));

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
        when(servicioProyecto.obtenerProyectoPorId(99L)).thenReturn(Optional.empty());

        // Cuando y Entonces
        mvc.perform(get("/proyectos/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/proyectos"))
                .andExpect(flash().attributeExists("mensajeError"));
    }
}
```

---

### 9. Cómo Ejecutar la Aplicación

1.  **Guarda los archivos:** Asegúrate de que todos los archivos `.java`, `.xml` y `.html` estén en las rutas correctas dentro de tu proyecto Maven.
    *   `src/main/java/com/example/gestionproyectos/` para las clases Java principales.
    *   `src/main/resources/application.properties`
    *   `src/main/resources/templates/proyectos/` para los archivos HTML de Thymeleaf.
    *   `src/test/java/com/example/gestionproyectos/` para las clases de prueba.
2.  **Construye el proyecto:** Abre una terminal en el directorio raíz de tu proyecto (donde se encuentra `pom.xml`) y ejecuta:
    ```bash
    mvn clean install
    ```
    Esto descargará las dependencias y construirá tu aplicación.
3.  **Ejecuta la aplicación:**
    ```bash
    mvn spring-boot:run
    ```
    Spring Boot arrancará la aplicación y el servidor Tomcat embebido. Verás los logs de inicialización, incluyendo la creación de la base de datos H2 y la carga de los datos de ejemplo definidos en `Application.java`.
4.  **Accede a la aplicación:** Abre tu navegador web y navega a:
    *   `http://localhost:8080/proyectos` para ver la lista de proyectos.
    *   Desde allí, puedes hacer clic en "Crear Nuevo Proyecto" para probar el formulario.
    *   Haz clic en el nombre de un proyecto para ver sus detalles y tareas.

### Conclusión

Has implementado una aplicación web integral utilizando Spring Boot, Spring MVC y Spring Data JPA. Hemos cubierto desde la configuración de dependencias, la definición de entidades y repositorios, la lógica de negocio en la capa de servicio, hasta la presentación de datos mediante controladores web y vistas Thymeleaf. Además, hemos incorporado buenas prácticas como el uso de DTOs para el desacoplamiento y la validación de entrada, y hemos configurado pruebas de integración para asegurar la robustez de la aplicación.

