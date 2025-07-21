
Este proyecto integral demostrará cómo construir una API RESTful utilizando Spring Boot, incluyendo la definición de modelos de datos, la capa de persistencia con Spring Data JPA, la lógica de negocio en la capa de servicio, la exposición de endpoints RESTful en la capa de controlador, la validación de entrada de datos, y un manejo robusto de excepciones. Además, se incluye un cliente para consumir la API utilizando `RestTemplate`.

### Estructura del Proyecto

El proyecto seguirá la siguiente estructura de directorios y archivos:

```
src/main/java/
└── com/baeldung/integralproyecto/
    ├── IntegralProyectoApplication.java
    │
    ├── config/
    │   └── ConfiguracionWeb.java (Opcional, para configuración avanzada de MVC)
    │
    ├── modelo/
    │   ├── Proyecto.java
    │   └── Persona.java
    │
    ├── dto/
    │   ├── ProyectoDTO.java
    │   └── PersonaDTO.java
    │
    ├── repositorio/
    │   ├── RepositorioProyecto.java
    │   └── RepositorioPersona.java
    │
    ├── servicio/
    │   ├── ServicioProyecto.java (Interfaz)
    │   └── impl/
    │       └── ServicioProyectoImpl.java (Implementación)
    │   ├── ServicioPersona.java (Interfaz)
    │   └── impl/
    │       └── ServicioPersonaImpl.java (Implementación)
    │
    ├── controlador/
    │   ├── ControladorProyecto.java
    │   └── ControladorPersona.java
    │
    ├── excepcion/
    │   ├── ApiError.java
    │   ├── ManejadorExcepcionesGlobal.java
    │   ├── RecursoNoEncontradoExcepcion.java
    │
    └── cliente/
        └── ClienteApiRest.java (Clase de prueba para el cliente RestTemplate)

src/main/resources/
└── application.properties

```

---

### **1. Configuración de Dependencias (pom.xml)**

El archivo `pom.xml` define las dependencias necesarias para un proyecto Spring Boot con JPA, validación y testing. Se utiliza H2 como base de datos en memoria para simplificar la ejecución sin necesidad de configurar una base de datos externa.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version> <!-- Versión de Spring Boot -->
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.baeldung</groupId>
    <artifactId>integral-proyecto</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>integral-proyecto</name>
    <description>Proyecto Integral de Gestión de Recursos (Projects &amp; Persons)</description>

    <properties>
        <java.version>17</java.version> <!-- Requiere Java 17 o superior para Bean Validation 3.0 -->
    </properties>

    <dependencies>
        <!-- Web y RESTful APIs -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- Persistencia con JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </input>
        <!-- Base de datos en memoria H2 para desarrollo y pruebas [N/A - práctica común, no en fuentes] -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!-- Validación de Beans (JSR 380) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <!-- Utilidades para testing de Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- Lombok para reducir código boilerplate (getters, setters, constructores, etc.) [N/A - práctica común, no en fuentes] -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>

```

**Explicación:**

- `spring-boot-starter-parent`: Proporciona la gestión de dependencias y configuraciones por defecto de Spring Boot.
- `spring-boot-starter-web`: Incluye Spring MVC y Tomcat embebido, esencial para construir aplicaciones web RESTful. También trae transitivamente Jackson para manejar JSON.
- `spring-boot-starter-data-jpa`: Habilita el uso de Spring Data JPA para la persistencia de datos, simplificando la interacción con bases de datos.
- `h2`: Una base de datos en memoria que es excelente para el desarrollo y las pruebas, ya que no requiere configuración externa.
- `spring-boot-starter-validation`: Incluye la implementación de Bean Validation (Hibernate Validator), permitiendo usar anotaciones como `@NotNull`, `@Size`, etc., para validar objetos.
- `spring-boot-starter-test`: Contiene utilidades para escribir pruebas en Spring Boot, incluyendo soporte para `RestTemplate` en un contexto de prueba.
- `lombok`: Una librería opcional pero muy útil que reduce el código repetitivo (boilerplate) mediante anotaciones como `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`.

---

### **2. Archivo de Propiedades (application.properties)**

Este archivo define configuraciones básicas para la aplicación. Es crucial activar la inclusión de mensajes de error en las respuestas para facilitar la depuración durante el desarrollo.

```
# Configuración del servidor
server.port=8080

# Configuración de la base de datos H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:integraldb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update # Actualiza el esquema de la base de datos automáticamente
spring.jpa.show-sql=true # Muestra las consultas SQL en la consola

# Configuración de manejo de errores
# Permite incluir el mensaje detallado de la excepción en la respuesta de error
server.error.include-message=always
# Permite incluir el stacktrace en la respuesta de error para depuración
server.error.include-stacktrace=always

```

**Explicación:**

- `server.port`: Puerto en el que se ejecutará la aplicación.
- Configuraciones de H2: Habilita la consola web de H2 y define la URL de la base de datos en memoria.
- `spring.jpa.hibernate.ddl-auto=update`: Permite que Hibernate (ORM de JPA) cree o actualice automáticamente las tablas de la base de datos según las entidades definidas.
- `server.error.include-message=always`: Configuración crucial para el desarrollo, ya que asegura que los mensajes de error detallados se incluyan en las respuestas de la API, lo que es invaluable para la depuración.
- `server.error.include-stacktrace=always`: Similar al anterior, incluye el rastro de la pila de errores, muy útil para identificar la causa raíz de un problema. En producción, estas dos últimas propiedades deberían deshabilitarse para evitar la fuga de información sensible.

---

### **3. Modelos (modelo)**

Las clases de modelo representan las entidades de la base de datos.

### **3.1. `modelo/Proyecto.java`**

```java
package com.baeldung.integralproyecto.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidad que representa un proyecto en la base de datos.
 * Utiliza Lombok para reducir el código boilerplate (getters, setters, constructores).
 */
@Entity // Indica que esta clase es una entidad JPA [N/A - práctica común, no en fuentes]
@Data // Genera getters, setters, toString, equals y hashCode [N/A]
@NoArgsConstructor // Genera un constructor sin argumentos [N/A]
@AllArgsConstructor // Genera un constructor con todos los argumentos [N/A]
public class Proyecto {

    @Id // Marca 'id' como la clave primaria [N/A]
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación automática del ID [N/A]
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaCreacion; // Fecha de creación del proyecto
}

```

### **3.2. `modelo/Persona.java`**

```java
package com.baeldung.integralproyecto.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una persona en la base de datos.
 * Utiliza Lombok para reducir el código boilerplate.
 */
@Entity // Indica que esta clase es una entidad JPA
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
public class Persona {

    @Id // Marca 'id' como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación automática del ID
    private Long id;
    private String nombre;
    private int edad;
    private String correoElectronico;
}

```

---

### **4. DTOs (Data Transfer Objects) (dto)**

Los DTOs se utilizan para transferir datos entre las capas de la aplicación, especialmente en las APIs REST. Aquí es donde se aplican las validaciones.

### **4.1. `dto/ProyectoDTO.java`**

```java
package com.baeldung.integralproyecto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para la entidad Proyecto.
 * Contiene anotaciones de validación para asegurar la integridad de los datos de entrada.
 */
@Data // Genera getters, setters, toString, equals y hashCode [N/A]
@NoArgsConstructor // Genera un constructor sin argumentos [N/A]
@AllArgsConstructor // Genera un constructor con todos los argumentos [N/A]
public class ProyectoDTO {

    private Long id;

    @NotBlank(message = "El nombre del proyecto no puede estar vacío.") // Valida que el campo no sea nulo ni vacío
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.") // Valida la longitud del campo
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres.") // Valida la longitud máxima
    private String descripcion;
}

```

### **4.2. `dto/PersonaDTO.java`**

```java
package com.baeldung.integralproyecto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para la entidad Persona.
 * Contiene anotaciones de validación para asegurar la integridad de los datos de entrada.
 */
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
public class PersonaDTO {

    private Long id;

    @NotBlank(message = "El nombre de la persona no puede estar vacío.") // Valida que el campo no sea nulo ni vacío
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.") // Valida la longitud del campo
    private String nombre;

    @Min(value = 0, message = "La edad no puede ser negativa.") // Valida que la edad sea al menos 0
    private int edad;

    @NotBlank(message = "El correo electrónico no puede estar vacío.") // Valida que el campo no sea nulo ni vacío
    @Email(message = "Debe ser un formato de correo electrónico válido.") // Valida el formato del correo electrónico
    private String correoElectronico;
}

```

---

### **5. Repositorios (repositorio)**

Los repositorios, basados en Spring Data JPA, proporcionan una interfaz para interactuar con la base de datos sin escribir implementaciones manuales de CRUD.

### **5.1. `repositorio/RepositorioProyecto.java`**

```java
package com.baeldung.integralproyecto.repositorio;

import com.baeldung.integralproyecto.modelo.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz de repositorio para la entidad Proyecto.
 * Extiende JpaRepository para obtener operaciones CRUD básicas.
 */
@Repository // Indica que esta interfaz es un componente de repositorio [N/A - práctica común, no en fuentes]
public interface RepositorioProyecto extends JpaRepository<Proyecto, Long> {
    // Spring Data JPA proporciona automáticamente implementaciones para los métodos CRUD
}

```

### **5.2. `repositorio/RepositorioPersona.java`**

```java
package com.baeldung.integralproyecto.repositorio;

import com.baeldung.integralproyecto.modelo.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz de repositorio para la entidad Persona.
 * Extiende JpaRepository para obtener operaciones CRUD básicas.
 */
@Repository // Indica que esta interfaz es un componente de repositorio
public interface RepositorioPersona extends JpaRepository<Persona, Long> {
    // Spring Data JPA proporciona automáticamente implementaciones para los métodos CRUD
}

```

---

### **6. Servicios (servicio)**

La capa de servicio contiene la lógica de negocio y coordina las operaciones entre los controladores y los repositorios.

### **6.1. Interfaz `servicio/ServicioProyecto.java`**

```java
package com.baeldung.integralproyecto.servicio;

import com.baeldung.integralproyecto.modelo.Proyecto;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para las operaciones de negocio relacionadas con Proyectos.
 */
public interface ServicioProyecto {
    Proyecto guardarProyecto(Proyecto proyecto);
    Optional<Proyecto> obtenerProyectoPorId(Long id);
    List<Proyecto> obtenerTodosLosProyectos();
    void eliminarProyecto(Long id);
}

```

### **6.2. Implementación `servicio/impl/ServicioProyectoImpl.java`**

```java
package com.baeldung.integralproyecto.servicio.impl;

import com.baeldung.integralproyecto.excepcion.RecursoNoEncontradoExcepcion;
import com.baeldung.integralproyecto.modelo.Proyecto;
import com.baeldung.integralproyecto.repositorio.RepositorioProyecto;
import com.baeldung.integralproyecto.servicio.ServicioProyecto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para las operaciones de negocio relacionadas con Proyectos.
 */
@Service // Indica que esta clase es un componente de servicio [N/A - práctica común, no en fuentes]
public class ServicioProyectoImpl implements ServicioProyecto {

    private final RepositorioProyecto repositorioProyecto;

    @Autowired // Inyecta el RepositorioProyecto
    public ServicioProyectoImpl(RepositorioProyecto repositorioProyecto) {
        this.repositorioProyecto = repositorioProyecto;
    }

    /**
     * Guarda un nuevo proyecto o actualiza uno existente.
     * Si el proyecto es nuevo (ID nulo), establece la fecha de creación.
     * @param proyecto El proyecto a guardar.
     * @return El proyecto guardado.
     */
    @Override
    public Proyecto guardarProyecto(Proyecto proyecto) {
        if (proyecto.getId() == null) {
            proyecto.setFechaCreacion(LocalDate.now()); // Establece la fecha de creación para nuevos proyectos
        }
        return repositorioProyecto.save(proyecto);
    }

    /**
     * Obtiene un proyecto por su ID.
     * @param id El ID del proyecto.
     * @return Un Optional que contiene el proyecto si existe.
     */
    @Override
    public Optional<Proyecto> obtenerProyectoPorId(Long id) {
        return repositorioProyecto.findById(id);
    }

    /**
     * Obtiene todos los proyectos.
     * @return Una lista de todos los proyectos.
     */
    @Override
    public List<Proyecto> obtenerTodosLosProyectos() {
        return repositorioProyecto.findAll();
    }

    /**
     * Elimina un proyecto por su ID.
     * Lanza una excepción si el proyecto no se encuentra.
     * @param id El ID del proyecto a eliminar.
     */
    @Override
    public void eliminarProyecto(Long id) {
        // En Spring Boot 3.0.4+, deleteById() ya no lanza EmptyResultDataAccessException.
        // Se reintroduce el comportamiento explícitamente para fines educativos.
        if (!repositorioProyecto.existsById(id)) {
            throw new RecursoNoEncontradoExcepcion("Proyecto no encontrado con ID: " + id);
        }
        repositorioProyecto.deleteById(id);
    }
}

```

### **6.3. Interfaz `servicio/ServicioPersona.java`**

```java
package com.baeldung.integralproyecto.servicio;

import com.baeldung.integralproyecto.modelo.Persona;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para las operaciones de negocio relacionadas con Personas.
 */
public interface ServicioPersona {
    Persona guardarPersona(Persona persona);
    Optional<Persona> obtenerPersonaPorId(Long id);
    List<Persona> obtenerTodasLasPersonas();
    void eliminarPersona(Long id);
}

```

### **6.4. Implementación `servicio/impl/ServicioPersonaImpl.java`**

```java
package com.baeldung.integralproyecto.servicio.impl;

import com.baeldung.integralproyecto.excepcion.RecursoNoEncontradoExcepcion;
import com.baeldung.integralproyecto.modelo.Persona;
import com.baeldung.integralproyecto.repositorio.RepositorioPersona;
import com.baeldung.integralproyecto.servicio.ServicioPersona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para las operaciones de negocio relacionadas con Personas.
 */
@Service // Indica que esta clase es un componente de servicio
public class ServicioPersonaImpl implements ServicioPersona {

    private final RepositorioPersona repositorioPersona;

    @Autowired // Inyecta el RepositorioPersona
    public ServicioPersonaImpl(RepositorioPersona repositorioPersona) {
        this.repositorioPersona = repositorioPersona;
    }

    /**
     * Guarda una nueva persona o actualiza una existente.
     * @param persona La persona a guardar.
     * @return La persona guardada.
     */
    @Override
    public Persona guardarPersona(Persona persona) {
        return repositorioPersona.save(persona);
    }

    /**
     * Obtiene una persona por su ID.
     * @param id El ID de la persona.
     * @return Un Optional que contiene la persona si existe.
     */
    @Override
    public Optional<Persona> obtenerPersonaPorId(Long id) {
        return repositorioPersona.findById(id);
    }

    /**
     * Obtiene todas las personas.
     * @return Una lista de todas las personas.
     */
    @Override
    public List<Persona> obtenerTodasLasPersonas() {
        return repositorioPersona.findAll();
    }

    /**
     * Elimina una persona por su ID.
     * Lanza una excepción si la persona no se encuentra.
     * @param id El ID de la persona a eliminar.
     */
    @Override
    public void eliminarPersona(Long id) {
        // Se reintroduce el comportamiento de excepción explícitamente para fines educativos.
        if (!repositorioPersona.existsById(id)) {
            throw new RecursoNoEncontradoExcepcion("Persona no encontrada con ID: " + id);
        }
        repositorioPersona.deleteById(id);
    }
}

```

---

### **7. Manejo de Excepciones (excepcion)**

Una parte crucial de cualquier API REST es el manejo consistente y útil de errores.

### **7.1. `excepcion/ApiError.java`**

Esta clase define la estructura estandarizada para las respuestas de error de la API, incluyendo el código de estado HTTP, un mensaje general y una lista de errores más específicos.

```java
package com.baeldung.integralproyecto.excepcion;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase DTO para estandarizar las respuestas de error de la API REST.
 * Proporciona información clara sobre el estado HTTP, el mensaje y los errores detallados.
 */
@Data // Genera getters, setters, toString, equals y hashCode [N/A]
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime fechaHora; // Timestamp de cuando ocurrió el error
    private HttpStatus estado; // Código de estado HTTP
    private String mensaje; // Mensaje general de error
    private List<String> errores; // Lista de errores específicos o de validación

    public ApiError(HttpStatus estado, String mensaje, List<String> errores) {
        this.fechaHora = LocalDateTime.now();
        this.estado = estado;
        this.mensaje = mensaje;
        this.errores = errores;
    }

    public ApiError(HttpStatus estado, String mensaje, String error) {
        this.fechaHora = LocalDateTime.now();
        this.estado = estado;
        this.mensaje = mensaje;
        this.errores = List.of(error);
    }
}

```

### **7.2. `excepcion/RecursoNoEncontradoExcepcion.java`**

Una excepción personalizada para indicar que un recurso no fue encontrado.

```java
package com.baeldung.integralproyecto.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para cuando un recurso solicitado no es encontrado.
 * Esta anotación hace que Spring devuelva un estado HTTP 404 NOT FOUND automáticamente.
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // Asocia esta excepción con el código de estado HTTP 404
public class RecursoNoEncontradoExcepcion extends RuntimeException {

    public RecursoNoEncontradoExcepcion(String mensaje) {
        super(mensaje); // Pasa el mensaje al constructor de la superclase
    }
}

```

### **7.3. `excepcion/ManejadorExcepcionesGlobal.java`**

Esta clase centraliza el manejo de excepciones para toda la aplicación. Extiende `ResponseEntityExceptionHandler` para manejar las excepciones estándar de Spring MVC y añade manejadores específicos para otras excepciones.

```java
package com.baeldung.integralproyecto.excepcion;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase para el manejo global de excepciones en la API REST.
 * Centraliza la lógica de manejo de errores para proporcionar respuestas consistentes.
 */
@ControllerAdvice // Anotación que marca esta clase como un componente que maneja excepciones globalmente
public class ManejadorExcepcionesGlobal extends ResponseEntityExceptionHandler {

    /**
     * Maneja RecursoNoEncontradoExcepcion para devolver un 404 Not Found.
     * Esta excepción es una RuntimeException anotada con @ResponseStatus(HttpStatus.NOT_FOUND).
     * Sin embargo, para mayor control sobre el cuerpo de la respuesta, la manejamos explícitamente aquí.
     * @param ex La excepción RecursoNoEncontradoExcepcion.
     * @param request La solicitud web.
     * @return Una ResponseEntity con el estado 404 y un ApiError.
     */
    @ExceptionHandler(RecursoNoEncontradoExcepcion.class) // Especifica la excepción a manejar
    public ResponseEntity<Object> manejarRecursoNoEncontrado(RecursoNoEncontradoExcepcion ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), "El recurso solicitado no fue encontrado.");
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getEstado());
    }

    /**
     * Maneja MethodArgumentNotValidException, que ocurre cuando la validación de un argumento del método falla.
     * Por ejemplo, si un DTO con @Valid no cumple sus restricciones.
     * @param ex La excepción MethodArgumentNotValidException.
     * @param headers Los encabezados HTTP.
     * @param status El estado HTTP.
     * @param request La solicitud web.
     * @return Una ResponseEntity con el estado 400 y un ApiError.
     */
    @Override // Sobrescribe el método de ResponseEntityExceptionHandler
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errores = new ArrayList<>();
        // Recopila errores de campo
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.add(error.getField() + ": " + error.getDefaultMessage()));
        // Recopila errores globales (a nivel de objeto)
        ex.getBindingResult().getGlobalErrors().forEach(error ->
                errores.add(error.getObjectName() + ": " + error.getDefaultMessage()));

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Error de validación de argumento.", errores);
        return handleExceptionInternal(ex, apiError, headers, apiError.getEstado(), request);
    }

    /**
     * Maneja MissingServletRequestParameterException, que ocurre cuando falta un parámetro de solicitud requerido.
     * @param ex La excepción MissingServletRequestParameterException.
     * @param headers Los encabezados HTTP.
     * @param status El estado HTTP.
     * @param request La solicitud web.
     * @return Una ResponseEntity con el estado 400 y un ApiError.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = ex.getParameterName() + " parámetro es requerido.";
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getEstado());
    }

    /**
     * Maneja ConstraintViolationException, que ocurre debido a fallas de validación de Bean (JSR 380)
     * fuera de @RequestBody, por ejemplo en @PathVariable o @RequestParam.
     * @param ex La excepción ConstraintViolationException.
     * @param request La solicitud web.
     * @return Una ResponseEntity con el estado 400 y un ApiError.
     */
    @ExceptionHandler({ ConstraintViolationException.class }) // Especifica la excepción a manejar
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errores = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) { // Recorre todas las violaciones
            errores.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errores);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getEstado());
    }

    /**
     * Maneja HttpRequestMethodNotSupportedException, que ocurre cuando el método HTTP de la solicitud no es soportado.
     * @param ex La excepción HttpRequestMethodNotSupportedException.
     * @param headers Los encabezados HTTP.
     * @param status El estado HTTP.
     * @param request La solicitud web.
     * @return Una ResponseEntity con el estado 405 y un ApiError.
     */
    @Override // Sobrescribe el método de ResponseEntityExceptionHandler
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" método no soportado para esta solicitud. Métodos soportados son ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " ")); // Lista los métodos HTTP soportados
        ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getEstado());
    }

    /**
     * Maneja NoHandlerFoundException, que ocurre cuando no se encuentra un controlador para la URL solicitada.
     * @param ex La excepción NoHandlerFoundException.
     * @param headers Los encabezados HTTP.
     * @param status El estado HTTP.
     * @param request La solicitud web.
     * @return Una ResponseEntity con el estado 404 y un ApiError.
     */
    @Override // Sobrescribe el método de ResponseEntityExceptionHandler
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = "No se encontró un manejador para " + ex.getHttpMethod() + " " + ex.getRequestURL();
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getEstado());
    }

    /**
     * Maneja HttpMediaTypeNotSupportedException, que ocurre cuando el tipo de contenido de la solicitud no es soportado.
     * @param ex La excepción HttpMediaTypeNotSupportedException.
     * @param headers Los encabezados HTTP.
     * @param status El estado HTTP.
     * @param request La solicitud web.
     * @return Una ResponseEntity con el estado 415 y un ApiError.
     */
    @Override // Sobrescribe el método de ResponseEntityExceptionHandler
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            org.springframework.web.HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" tipo de medio no soportado. Tipos de medio soportados son ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", ")); // Lista los tipos de medio soportados
        ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getEstado());
    }

    /**
     * Manejador de excepciones genérico para cualquier otra excepción no capturada.
     * Siempre debe ser el último manejador en una @ControllerAdvice.
     * @param ex La excepción.
     * @param request La solicitud web.
     * @return Una ResponseEntity con el estado 500 y un ApiError.
     */
    @ExceptionHandler({ Exception.class }) // Captura cualquier otra excepción no manejada
    public ResponseEntity<Object> manejarTodasLasExcepciones(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Ocurrió un error inesperado en el servidor.");
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getEstado());
    }
}

```

**Explicación:**

- `@ControllerAdvice`: Anotación clave que permite que esta clase proporcione un manejo global de excepciones para todos los controladores.
- `ResponseEntityExceptionHandler`: Una clase base conveniente proporcionada por Spring que ya implementa manejadores para muchas excepciones comunes de Spring MVC, como `MethodArgumentNotValidException`. Al extenderla, podemos sobrescribir sus métodos para personalizar las respuestas o añadir nuevos manejadores.
- `@ExceptionHandler`: Se utiliza en los métodos dentro de `@ControllerAdvice` para especificar qué tipo de excepción debe manejar ese método en particular.
- `RecursoNoEncontradoExcepcion`: Una excepción personalizada con `@ResponseStatus` que se maneja aquí para un formato de error consistente.
- `handleMethodArgumentNotValid`: Sobrescribe el manejo predeterminado de `MethodArgumentNotValidException`, que se lanza cuando la validación de `@Valid` en los argumentos del controlador falla. Recopila todos los errores de validación de campo y globales.
- `handleConstraintViolation`: Maneja `ConstraintViolationException`, que puede ocurrir cuando las validaciones de Bean se aplican a `@PathVariable` o `@RequestParam`.
- `handleHttpRequestMethodNotSupported`: Maneja el error cuando se usa un método HTTP no permitido para una ruta (ej., POST en un `@GetMapping`).
- `handleNoHandlerFoundException`: Captura el error 404 cuando no hay un manejador para la ruta solicitada.
- `manejarTodasLasExcepciones`: Un manejador genérico que captura cualquier `Exception` no manejada por otros `@ExceptionHandler` más específicos. Es un "último recurso" para asegurar que ninguna excepción no manejada cause una respuesta no estructurada.

---

### **8. Controladores (controlador)**

Los controladores gestionan las solicitudes HTTP entrantes y devuelven respuestas HTTP. Utilizan los servicios para realizar las operaciones de negocio.

### **8.1. `controlador/ControladorProyecto.java`**

```java
package com.baeldung.integralproyecto.controlador;

import com.baeldung.integralproyecto.dto.ProyectoDTO;
import com.baeldung.integralproyecto.excepcion.RecursoNoEncontradoExcepcion;
import com.baeldung.integralproyecto.modelo.Proyecto;
import com.baeldung.integralproyecto.servicio.ServicioProyecto;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de proyectos.
 * Expone endpoints para operaciones CRUD de proyectos.
 */
@RestController // Combina @Controller y @ResponseBody, indicando que los métodos devuelven datos directamente al cuerpo de la respuesta HTTP
@RequestMapping("/api/proyectos") // Mapea todas las solicitudes bajo /api/proyectos a este controlador
public class ControladorProyecto {

    private final ServicioProyecto servicioProyecto;

    @Autowired // Inyecta el ServicioProyecto
    public ControladorProyecto(ServicioProyecto servicioProyecto) {
        this.servicioProyecto = servicioProyecto;
    }

    /**
     * Obtiene todos los proyectos.
     * GET /api/proyectos
     * @return ResponseEntity con una lista de ProyectoDTOs y estado 200 OK.
     */
    @GetMapping // Mapea las solicitudes GET a esta URL
    public ResponseEntity<List<ProyectoDTO>> obtenerTodosLosProyectos() {
        List<ProyectoDTO> proyectosDTO = servicioProyecto.obtenerTodosLosProyectos().stream()
                .map(this::convertirAProyectoDTO) // Convierte cada entidad a DTO
                .collect(Collectors.toList());
        return ResponseEntity.ok(proyectosDTO); // Devuelve 200 OK con la lista de proyectos
    }

    /**
     * Obtiene un proyecto por su ID.
     * GET /api/proyectos/{id}
     * @param id El ID del proyecto.
     * @return ResponseEntity con el ProyectoDTO y estado 200 OK.
     * @throws RecursoNoEncontradoExcepcion si el proyecto no se encuentra.
     */
    @GetMapping("/{id}") // Mapea las solicitudes GET a /api/proyectos/{id}
    public ResponseEntity<ProyectoDTO> obtenerProyectoPorId(@PathVariable Long id) { // Extrae el ID de la URL
        Proyecto proyecto = servicioProyecto.obtenerProyectoPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Proyecto no encontrado con ID: " + id)); // Lanza excepción si no se encuentra
        return ResponseEntity.ok(convertirAProyectoDTO(proyecto)); // Devuelve 200 OK con el proyecto
    }

    /**
     * Crea un nuevo proyecto.
     * POST /api/proyectos
     * @param proyectoDTO El DTO del proyecto a crear.
     * @return ResponseEntity con el ProyectoDTO creado y estado 201 Created.
     */
    @PostMapping // Mapea las solicitudes POST a esta URL
    // @ResponseStatus(HttpStatus.CREATED) // Opcional: Establece el código de estado 201 si el método completa con éxito
    public ResponseEntity<ProyectoDTO> crearProyecto(@Valid @RequestBody ProyectoDTO proyectoDTO) { // @Valid activa la validación del DTO, @RequestBody deserializa el cuerpo de la solicitud a un objeto Java
        Proyecto proyecto = convertirAEntidad(proyectoDTO); // Convierte DTO a entidad
        Proyecto nuevoProyecto = servicioProyecto.guardarProyecto(proyecto);
        ProyectoDTO nuevoProyectoDTO = convertirAProyectoDTO(nuevoProyecto);

        // Construye la URI del nuevo recurso creado para el encabezado 'Location' [N/A - práctica RESTful]
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(nuevoProyecto.getId()).toUri();

        return ResponseEntity.created(ubicacion).body(nuevoProyectoDTO); // Devuelve 201 Created con el proyecto y su ubicación
    }

    /**
     * Actualiza un proyecto existente.
     * PUT /api/proyectos/{id}
     * @param id El ID del proyecto a actualizar.
     * @param proyectoDTO El DTO del proyecto con los datos actualizados.
     * @return ResponseEntity con el ProyectoDTO actualizado y estado 200 OK.
     * @throws RecursoNoEncontradoExcepcion si el proyecto no se encuentra.
     */
    @PutMapping("/{id}") // Mapea las solicitudes PUT a /api/proyectos/{id}
    public ResponseEntity<ProyectoDTO> actualizarProyecto(@PathVariable Long id, @Valid @RequestBody ProyectoDTO proyectoDTO) {
        // Verifica si el proyecto existe antes de intentar actualizar
        servicioProyecto.obtenerProyectoPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Proyecto no encontrado con ID: " + id));

        Proyecto proyectoParaActualizar = convertirAEntidad(proyectoDTO);
        proyectoParaActualizar.setId(id); // Asegura que el ID de la entidad sea el de la ruta

        Proyecto proyectoActualizado = servicioProyecto.guardarProyecto(proyectoParaActualizar);
        return ResponseEntity.ok(convertirAProyectoDTO(proyectoActualizado)); // Devuelve 200 OK con el proyecto actualizado
    }

    /**
     * Elimina un proyecto por su ID.
     * DELETE /api/proyectos/{id}
     * @param id El ID del proyecto a eliminar.
     * @return ResponseEntity con estado 204 No Content.
     */
    @DeleteMapping("/{id}") // Mapea las solicitudes DELETE a /api/proyectos/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT) // Establece el código de estado 204 si el método completa con éxito
    public void eliminarProyecto(@PathVariable Long id) {
        servicioProyecto.eliminarProyecto(id); // La excepción de "no encontrado" se maneja en el servicio o el manejador global.
    }

    // Métodos de conversión entre Entidad y DTO
    private ProyectoDTO convertirAProyectoDTO(Proyecto proyecto) {
        ProyectoDTO proyectoDTO = new ProyectoDTO();
        BeanUtils.copyProperties(proyecto, proyectoDTO); // Copia propiedades con el mismo nombre [N/A]
        return proyectoDTO;
    }

    private Proyecto convertirAEntidad(ProyectoDTO proyectoDTO) {
        Proyecto proyecto = new Proyecto();
        BeanUtils.copyProperties(proyectoDTO, proyecto); // Copia propiedades con el mismo nombre
        return proyecto;
    }
}

```

### **8.2. `controlador/ControladorPersona.java`**

```java
package com.baeldung.integralproyecto.controlador;

import com.baeldung.integralproyecto.dto.PersonaDTO;
import com.baeldung.integralproyecto.excepcion.RecursoNoEncontradoExcepcion;
import com.baeldung.integralproyecto.modelo.Persona;
import com.baeldung.integralproyecto.servicio.ServicioPersona;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de personas.
 * Expone endpoints para operaciones CRUD de personas.
 */
@RestController // Combina @Controller y @ResponseBody
@RequestMapping("/api/personas") // Mapea todas las solicitudes bajo /api/personas a este controlador
public class ControladorPersona {

    private final ServicioPersona servicioPersona;

    @Autowired // Inyecta el ServicioPersona
    public ControladorPersona(ServicioPersona servicioPersona) {
        this.servicioPersona = servicioPersona;
    }

    /**
     * Obtiene todas las personas.
     * GET /api/personas
     * @return ResponseEntity con una lista de PersonaDTOs y estado 200 OK.
     */
    @GetMapping // Mapea las solicitudes GET a esta URL
    public ResponseEntity<List<PersonaDTO>> obtenerTodasLasPersonas() {
        List<PersonaDTO> personasDTO = servicioPersona.obtenerTodasLasPersonas().stream()
                .map(this::convertirAPersonaDTO) // Convierte cada entidad a DTO
                .collect(Collectors.toList());
        return ResponseEntity.ok(personasDTO); // Devuelve 200 OK con la lista de personas
    }

    /**
     * Obtiene una persona por su ID.
     * GET /api/personas/{id}
     * @param id El ID de la persona.
     * @return ResponseEntity con la PersonaDTO y estado 200 OK.
     * @throws RecursoNoEncontradoExcepcion si la persona no se encuentra.
     */
    @GetMapping("/{id}") // Mapea las solicitudes GET a /api/personas/{id}
    public ResponseEntity<PersonaDTO> obtenerPersonaPorId(@PathVariable Long id) { // Extrae el ID de la URL
        Persona persona = servicioPersona.obtenerPersonaPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Persona no encontrada con ID: " + id)); // Lanza excepción si no se encuentra
        return ResponseEntity.ok(convertirAPersonaDTO(persona)); // Devuelve 200 OK con la persona
    }

    /**
     * Crea una nueva persona.
     * POST /api/personas
     * @param personaDTO El DTO de la persona a crear.
     * @return ResponseEntity con la PersonaDTO creada y estado 201 Created.
     */
    @PostMapping // Mapea las solicitudes POST a esta URL
    public ResponseEntity<PersonaDTO> crearPersona(@Valid @RequestBody PersonaDTO personaDTO) { // @Valid activa la validación del DTO, @RequestBody deserializa el cuerpo de la solicitud
        Persona persona = convertirAEntidad(personaDTO); // Convierte DTO a entidad
        Persona nuevaPersona = servicioPersona.guardarPersona(persona);
        PersonaDTO nuevaPersonaDTO = convertirAPersonaDTO(nuevaPersona);

        // Construye la URI del nuevo recurso creado para el encabezado 'Location'
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(nuevaPersona.getId()).toUri();

        return ResponseEntity.created(ubicacion).body(nuevaPersonaDTO); // Devuelve 201 Created con la persona y su ubicación
    }

    /**
     * Actualiza una persona existente.
     * PUT /api/personas/{id}
     * @param id El ID de la persona a actualizar.
     * @param personaDTO El DTO de la persona con los datos actualizados.
     * @return ResponseEntity con la PersonaDTO actualizada y estado 200 OK.
     * @throws RecursoNoEncontradoExcepcion si la persona no se encuentra.
     */
    @PutMapping("/{id}") // Mapea las solicitudes PUT a /api/personas/{id}
    public ResponseEntity<PersonaDTO> actualizarPersona(@PathVariable Long id, @Valid @RequestBody PersonaDTO personaDTO) {
        // Verifica si la persona existe antes de intentar actualizar
        servicioPersona.obtenerPersonaPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Persona no encontrada con ID: " + id));

        Persona personaParaActualizar = convertirAEntidad(personaDTO);
        personaParaActualizar.setId(id); // Asegura que el ID de la entidad sea el de la ruta

        Persona personaActualizada = servicioPersona.guardarPersona(personaParaActualizar);
        return ResponseEntity.ok(convertirAPersonaDTO(personaActualizada)); // Devuelve 200 OK con la persona actualizada
    }

    /**
     * Elimina una persona por su ID.
     * DELETE /api/personas/{id}
     * @param id El ID de la persona a eliminar.
     * @return ResponseEntity con estado 204 No Content.
     */
    @DeleteMapping("/{id}") // Mapea las solicitudes DELETE a /api/personas/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT) // Establece el código de estado 204 si el método completa con éxito
    public void eliminarPersona(@PathVariable Long id) {
        servicioPersona.eliminarPersona(id); // La excepción de "no encontrado" se maneja en el servicio o el manejador global.
    }

    // Métodos de conversión entre Entidad y DTO
    private PersonaDTO convertirAPersonaDTO(Persona persona) {
        PersonaDTO personaDTO = new PersonaDTO();
        BeanUtils.copyProperties(persona, personaDTO); // Copia propiedades con el mismo nombre
        return personaDTO;
    }

    private Persona convertirAEntidad(PersonaDTO personaDTO) {
        Persona persona = new Persona();
        BeanUtils.copyProperties(personaDTO, persona); // Copia propiedades con el mismo nombre
        return persona;
    }
}

```

**Explicación de Controladores:**

- `@RestController`: Hace que la clase sea un controlador REST, implicando que el valor de retorno de los métodos se serializa directamente en el cuerpo de la respuesta HTTP, sin resolución de vistas.
- `@RequestMapping("/api/...")`: Mapea todas las solicitudes con un prefijo de URL específico a este controlador.
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`: Son anotaciones de acceso directo para `@RequestMapping` que pre-seleccionan el verbo HTTP (GET, POST, PUT, DELETE, respectivamente), simplificando el mapeo.
- `@PathVariable`: Extrae valores de la URL de la solicitud (parte de la ruta) y los vincula a los parámetros del método.
- `@RequestBody`: Indica que un parámetro de método debe vincularse al cuerpo de la solicitud HTTP. Spring lo deserializa automáticamente a un objeto Java (típicamente de JSON).
- `@Valid`: Activa la validación de Bean (JSR 380) para el objeto `@RequestBody`, lo que significa que las anotaciones de validación definidas en los DTOs se aplicarán. Si falla la validación, se lanza un `MethodArgumentNotValidException`, que es capturado por el `ManejadorExcepcionesGlobal`.
- `ResponseEntity<T>`: Una clase flexible para devolver respuestas HTTP que permite controlar el código de estado, los encabezados y el cuerpo de la respuesta.
- `@ResponseStatus(HttpStatus.NO_CONTENT)`: En el método `eliminarProyecto`, se usa para especificar que la operación exitosa debe devolver un código de estado 204 (No Content), que es el estándar para operaciones DELETE que no devuelven contenido.
- Conversión DTO a Entidad y viceversa: Se utilizan métodos privados y `BeanUtils.copyProperties` para mapear los objetos DTO a las entidades de dominio y viceversa. Esto desacopla la API de la capa de persistencia.

---

### **9. Clase Principal de la Aplicación (IntegralProyectoApplication)**

La clase principal para iniciar la aplicación Spring Boot.

### **9.1. `IntegralProyectoApplication.java`**

```java
package com.baeldung.integralproyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot.
 * Se encarga de arrancar la aplicación.
 */
@SpringBootApplication // Anotación de conveniencia que agrega:
                       // @Configuration: Marca la clase como una fuente de definición de beans.
                       // @EnableAutoConfiguration: Habilita la configuración automática de Spring Boot.
                       // @ComponentScan: Busca componentes (controladores, servicios, repositorios) en el paquete actual y subpaquetes.
public class IntegralProyectoApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegralProyectoApplication.class, args);
    }
}

```

**Explicación:**

- `@SpringBootApplication`: Esta es la anotación principal para una aplicación Spring Boot. Combina `@Configuration`, `@EnableAutoConfiguration` y `@ComponentScan` [N/A - práctica común, no en fuentes]. Esto significa que Spring Boot configurará automáticamente la aplicación web, la base de datos y escaneará los componentes definidos en este paquete y sus subpaquetes.

---

### **10. Configuración Web (Opcional) (config)**

Para algunas configuraciones avanzadas de Spring MVC, se puede utilizar una clase que implemente `WebMvcConfigurer`. Para el JSON básico, `spring-boot-starter-web` ya configura los `HttpMessageConverter`s por defecto.

### **10.1. `config/ConfiguracionWeb.java`**

```java
package com.baeldung.integralproyecto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc; // Importación necesaria para @EnableWebMvc

/**
 * Clase de configuración web para Spring MVC.
 * Implementa WebMvcConfigurer para personalizar la configuración MVC.
 * @EnableWebMvc es opcional en Spring Boot si se usa spring-boot-starter-web,
 * ya que Spring Boot ya auto-configura Spring MVC. Se incluye para mayor
 * claridad o para sobrescribir el comportamiento por defecto.
 */
@Configuration // Indica que esta clase contiene definiciones de beans de configuración
// @EnableWebMvc // Habilita la configuración explícita de Spring MVC. Comentado porque Spring Boot lo hace automáticamente.
public class ConfiguracionWeb implements WebMvcConfigurer {

    // Aquí se podrían agregar configuraciones personalizadas como:
    // - addCorsMappings: para configurar CORS globalmente
    // - configureMessageConverters: para añadir o modificar HttpMessageConverters
    // - addInterceptors: para registrar interceptores
    // - configureContentNegotiation: para personalizar cómo se determina el tipo de contenido
    // etc.
}

```

**Explicación:**

- `@Configuration`: Marca esta clase como una fuente de definiciones de beans.
- `WebMvcConfigurer`: Interfaz que permite personalizar la configuración predeterminada de Spring MVC proporcionada por Spring Boot. Aunque no se añaden métodos específicos aquí, es el lugar donde se harían personalizaciones para la negociación de contenido, interceptores, o manejadores de recursos estáticos. Para el JSON básico, el `spring-boot-starter-web` ya incluye los `HttpMessageConverter`s necesarios (como `MappingJackson2HttpMessageConverter`).

---

### **11. Cliente de API REST (cliente)**

Esta clase demuestra cómo consumir la API REST utilizando `RestTemplate` de Spring. Está configurada como una prueba para que pueda ejecutarla fácilmente.

### **11.1. `cliente/ClienteApiRest.java`**

```java
package com.baeldung.integralproyecto.cliente;

import com.baeldung.integralproyecto.dto.PersonaDTO;
import com.baeldung.integralproyecto.dto.ProyectoDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodSource;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba para demostrar el consumo de la API REST utilizando RestTemplate.
 * Aunque se ejecuta como prueba, el código muestra cómo un cliente externo interactuaría con la API.
 */
public class ClienteApiRest {

    private static final String URL_BASE_API = "http://localhost:8080/api/";
    private static RestTemplate restTemplate;

    @BeforeAll // Se ejecuta una vez antes de todas las pruebas
    static void configurar() {
        restTemplate = new RestTemplate(); // Inicializa RestTemplate
    }

    /**
     * Fuente de datos para crear personas y proyectos.
     * @return Stream de Object arrays con datos de DTOs.
     */
    static Stream<Object[]> datosDePrueba() {
        return Stream.of(
            new Object[]{new ProyectoDTO(null, "Proyecto Alpha", "Descripción de Alpha")},
            new Object[]{new ProyectoDTO(null, "Proyecto Beta", "Descripción de Beta")},
            new Object[]{new PersonaDTO(null, "Alice", 30, "alice@example.com")},
            new Object[]{new PersonaDTO(null, "Bob", 25, "bob@example.com")}
        );
    }

    @Test // Marca este método como una prueba JUnit
    void pruebasCompletasDeApi() {
        System.out.println("\n--- INICIANDO PRUEBAS DE CLIENTE API REST ---");

        // --- Pruebas de Proyectos ---
        System.out.println("\n--- PROYECTOS ---");
        // 1. Crear proyectos
        ProyectoDTO proyecto1 = crearNuevoProyecto(new ProyectoDTO(null, "Proyecto Web", "Desarrollo de una aplicación web"));
        ProyectoDTO proyecto2 = crearNuevoProyecto(new ProyectoDTO(null, "Proyecto Móvil", "Desarrollo de una aplicación móvil"));

        // 2. Obtener todos los proyectos
        obtenerTodosLosProyectos();

        // 3. Obtener un proyecto por ID
        obtenerProyectoPorId(proyecto1.getId());

        // 4. Actualizar un proyecto
        proyecto1.setNombre("Proyecto Web (Actualizado)");
        actualizarProyecto(proyecto1.getId(), proyecto1);

        // 5. Eliminar un proyecto
        eliminarProyecto(proyecto2.getId());

        // 6. Intentar obtener un proyecto eliminado (debe fallar)
        intentarObtenerProyectoInexistente(proyecto2.getId());

        // 7. Intentar crear proyecto con datos inválidos (nombre vacío)
        intentarCrearProyectoInvalido(new ProyectoDTO(null, "", "Descripción corta"));

        // --- Pruebas de Personas ---
        System.out.println("\n--- PERSONAS ---");
        // 1. Crear personas
        PersonaDTO persona1 = crearNuevaPersona(new PersonaDTO(null, "Carlos", 28, "carlos@example.com"));
        PersonaDTO persona2 = crearNuevaPersona(new PersonaDTO(null, "Diana", 35, "diana@example.com"));

        // 2. Obtener todas las personas
        obtenerTodasLasPersonas();

        // 3. Obtener una persona por ID
        obtenerPersonaPorId(persona1.getId());

        // 4. Actualizar una persona
        persona1.setEdad(29);
        actualizarPersona(persona1.getId(), persona1);

        // 5. Eliminar una persona
        eliminarPersona(persona2.getId());

        // 6. Intentar obtener una persona eliminada (debe fallar)
        intentarObtenerPersonaInexistente(persona2.getId());

        // 7. Intentar crear persona con datos inválidos (email no válido, edad negativa)
        intentarCrearPersonaInvalida(new PersonaDTO(null, "Invalid", -5, "invalid-email"));

        System.out.println("\n--- PRUEBAS DE CLIENTE API REST FINALIZADAS ---");
    }

    // --- Métodos de Ayuda para Proyectos ---
    ProyectoDTO crearNuevoProyecto(ProyectoDTO proyectoDTO) {
        System.out.println("\nCreando proyecto: " + proyectoDTO.getNombre());
        try {
            // postForEntity envía una solicitud POST y devuelve ResponseEntity
            ResponseEntity<ProyectoDTO> respuesta = restTemplate.postForEntity(
                    URL_BASE_API + "proyectos", proyectoDTO, ProyectoDTO.class);
            assertEquals(HttpStatus.CREATED, respuesta.getStatusCode()); // Verifica código de estado 201
            assertNotNull(respuesta.getBody()); // Verifica que el cuerpo de la respuesta no sea nulo
            assertNotNull(respuesta.getBody().getId()); // Verifica que el ID fue generado
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
        // getForEntity envía una solicitud GET y devuelve ResponseEntity
        ResponseEntity<List> respuesta = restTemplate.getForEntity(
                URL_BASE_API + "proyectos", List.class);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        System.out.println("Proyectos obtenidos (" + respuesta.getBody().size() + "): " + respuesta.getBody());
    }

    void obtenerProyectoPorId(Long id) {
        System.out.println("\nObteniendo proyecto con ID: " + id);
        try {
            ResponseEntity<ProyectoDTO> respuesta = restTemplate.getForEntity(
                    URL_BASE_API + "proyectos/" + id, ProyectoDTO.class);
            assertEquals(HttpStatus.OK, respuesta.getStatusCode());
            assertNotNull(respuesta.getBody());
            assertEquals(id, respuesta.getBody().getId());
            System.out.println("Proyecto obtenido: " + respuesta.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("Error al obtener proyecto con ID " + id + ": " + e.getResponseBodyAsString());
            fail("Falló la obtención del proyecto: " + e.getMessage());
        }
    }

    void actualizarProyecto(Long id, ProyectoDTO proyectoDTO) {
        System.out.println("\nActualizando proyecto con ID " + id + " a nombre: " + proyectoDTO.getNombre());
        try {
            // exchange para PUT permite enviar un cuerpo y especificar el tipo de retorno
            restTemplate.put(URL_BASE_API + "proyectos/" + id, proyectoDTO);
            System.out.println("Proyecto con ID " + id + " actualizado correctamente.");
            // Verificar la actualización obteniéndolo de nuevo
            obtenerProyectoPorId(id);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error al actualizar proyecto con ID " + id + ": " + e.getResponseBodyAsString());
            fail("Falló la actualización del proyecto: " + e.getMessage());
        }
    }

    void eliminarProyecto(Long id) {
        System.out.println("\nEliminando proyecto con ID: " + id);
        try {
            restTemplate.delete(URL_BASE_API + "proyectos/" + id);
            System.out.println("Proyecto con ID " + id + " eliminado correctamente.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error al eliminar proyecto con ID " + id + ": " + e.getResponseBodyAsString());
            fail("Falló la eliminación del proyecto: " + e.getMessage());
        }
    }

    void intentarObtenerProyectoInexistente(Long id) {
        System.out.println("\nIntentando obtener proyecto inexistente con ID: " + id);
        try {
            restTemplate.getForEntity(URL_BASE_API + "proyectos/" + id, ProyectoDTO.class);
            fail("Se esperaba RecursoNoEncontradoExcepcion para el proyecto con ID: " + id);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode()); // Esperamos un 404
            System.out.println("Recibido 404 Not Found como se esperaba para el proyecto con ID " + id);
            System.out.println("Cuerpo del error: " + e.getResponseBodyAsString());
        }
    }

    void intentarCrearProyectoInvalido(ProyectoDTO proyectoDTO) {
        System.out.println("\nIntentando crear proyecto con datos inválidos (nombre vacío)...");
        try {
            restTemplate.postForEntity(URL_BASE_API + "proyectos", proyectoDTO, ProyectoDTO.class);
            fail("Se esperaba un error de validación para el proyecto inválido.");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode()); // Esperamos un 400
            System.out.println("Recibido 400 Bad Request como se esperaba para el proyecto inválido.");
            System.out.println("Cuerpo del error: " + e.getResponseBodyAsString());
        }
    }

    // --- Métodos de Ayuda para Personas ---
    PersonaDTO crearNuevaPersona(PersonaDTO personaDTO) {
        System.out.println("\nCreando persona: " + personaDTO.getNombre());
        try {
            ResponseEntity<PersonaDTO> respuesta = restTemplate.postForEntity(
                    URL_BASE_API + "personas", personaDTO, PersonaDTO.class);
            assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
            assertNotNull(respuesta.getBody());
            assertNotNull(respuesta.getBody().getId());
            System.out.println("Persona creada: " + respuesta.getBody());
            return respuesta.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error al crear persona: " + e.getResponseBodyAsString());
            fail("Falló la creación de la persona: " + e.getMessage());
            return null;
        }
    }

    void obtenerTodasLasPersonas() {
        System.out.println("\nObteniendo todas las personas...");
        ResponseEntity<List> respuesta = restTemplate.getForEntity(
                URL_BASE_API + "personas", List.class);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        System.out.println("Personas obtenidas (" + respuesta.getBody().size() + "): " + respuesta.getBody());
    }

    void obtenerPersonaPorId(Long id) {
        System.out.println("\nObteniendo persona con ID: " + id);
        try {
            ResponseEntity<PersonaDTO> respuesta = restTemplate.getForEntity(
                    URL_BASE_API + "personas/" + id, PersonaDTO.class);
            assertEquals(HttpStatus.OK, respuesta.getStatusCode());
            assertNotNull(respuesta.getBody());
            assertEquals(id, respuesta.getBody().getId());
            System.out.println("Persona obtenida: " + respuesta.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("Error al obtener persona con ID " + id + ": " + e.getResponseBodyAsString());
            fail("Falló la obtención de la persona: " + e.getMessage());
        }
    }

    void actualizarPersona(Long id, PersonaDTO personaDTO) {
        System.out.println("\nActualizando persona con ID " + id + " a edad: " + personaDTO.getEdad());
        try {
            restTemplate.put(URL_BASE_API + "personas/" + id, personaDTO);
            System.out.println("Persona con ID " + id + " actualizada correctamente.");
            obtenerPersonaPorId(id);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error al actualizar persona con ID " + id + ": " + e.getResponseBodyAsString());
            fail("Falló la actualización de la persona: " + e.getMessage());
        }
    }

    void eliminarPersona(Long id) {
        System.out.println("\nEliminando persona con ID: " + id);
        try {
            restTemplate.delete(URL_BASE_API + "personas/" + id);
            System.out.println("Persona con ID " + id + " eliminada correctamente.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error al eliminar persona con ID " + id + ": " + e.getResponseBodyAsString());
            fail("Falló la eliminación de la persona: " + e.getMessage());
        }
    }

    void intentarObtenerPersonaInexistente(Long id) {
        System.out.println("\nIntentando obtener persona inexistente con ID: " + id);
        try {
            restTemplate.getForEntity(URL_BASE_API + "personas/" + id, PersonaDTO.class);
            fail("Se esperaba RecursoNoEncontradoExcepcion para la persona con ID: " + id);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            System.out.println("Recibido 404 Not Found como se esperaba para la persona con ID " + id);
            System.out.println("Cuerpo del error: " + e.getResponseBodyAsString());
        }
    }

    void intentarCrearPersonaInvalida(PersonaDTO personaDTO) {
        System.out.println("\nIntentando crear persona con datos inválidos (email no válido, edad negativa)...");
        try {
            restTemplate.postForEntity(URL_BASE_API + "personas", personaDTO, PersonaDTO.class);
            fail("Se esperaba un error de validación para la persona inválida.");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            System.out.println("Recibido 400 Bad Request como se esperaba para la persona inválida.");
            System.out.println("Cuerpo del error: " + e.getResponseBodyAsString());
        }
    }
}

```

**Explicación del Cliente RestTemplate:**

- `RestTemplate`: Una clase fundamental de Spring para realizar solicitudes HTTP del lado del cliente. Es robusta y adecuada tanto para casos de uso simples como avanzados al probar puntos finales HTTP.
- `@BeforeAll`: Anotación de JUnit 5 que indica que el método `configurar` se ejecutará una vez antes de todas las pruebas en la clase.
- `URL_BASE_API`: Constante que define la base de la URL de la API.
- `restTemplate.postForEntity(...)`: Envía una solicitud HTTP POST. Toma la URL, el objeto a enviar como cuerpo de la solicitud (que se serializará automáticamente a JSON), y la clase del tipo de respuesta esperado (o `Void.class` si no se espera cuerpo de respuesta). Devuelve un `ResponseEntity`, que encapsula la respuesta completa (cuerpo, encabezados y código de estado).
- `restTemplate.getForEntity(...)`: Envía una solicitud HTTP GET y recupera una `ResponseEntity`. Es útil cuando se necesita acceder a los encabezados HTTP o al código de estado de la respuesta.
- `restTemplate.put(...)`: Envía una solicitud HTTP PUT.
- `restTemplate.delete(...)`: Envía una solicitud HTTP DELETE.
- `HttpStatus`: Enumeración de Spring que representa los códigos de estado HTTP.
- `HttpClientErrorException` y `HttpServerErrorException`: Clases de excepción de `RestTemplate` que capturan errores del lado del cliente (4xx) y del servidor (5xx) respectivamente [N/A - práctica común, no en fuentes]. Es crucial manejarlas para verificar los códigos de error devueltos por la API.
- Aserciones de JUnit (`assertEquals`, `assertNotNull`, `fail`): Se utilizan para verificar el comportamiento esperado de la API, como los códigos de estado HTTP y la presencia de datos en la respuesta.

---

### **12. Arrancar la Aplicación**

Para ejecutar el proyecto:

1. **Navegue a la raíz del proyecto** en su terminal (donde se encuentra `pom.xml`).
2. **Compile el proyecto:**

    ```
    mvn clean install
    
    ```

3. **Ejecute la aplicación Spring Boot:**

    ```
    mvn spring-boot:run
    
    ```


La aplicación se iniciará en `http://localhost:8080`.
La consola H2 estará disponible en `http://localhost:8080/h2-console` (use `jdbc:h2:mem:integraldb` como URL JDBC, usuario `sa`, contraseña `password`).

Para **ejecutar las pruebas del cliente** (`ClienteApiRest.java`) que interactúan con la API en ejecución:

1. Asegúrese de que la aplicación Spring Boot (`mvn spring-boot:run`) esté **en ejecución** en segundo plano.
2. En una **terminal diferente**, navegue a la raíz del proyecto.
3. Ejecute las pruebas de JUnit:

Observará la salida de las pruebas en la consola, incluyendo los mensajes de creación, obtención, actualización, eliminación y los errores esperados.

    ```
    mvn test -Dtest=ClienteApiRest
    
    ```


---

### **Reflexiones y Conocimiento Adicional como Experto**

Como programador Java senior, puedo ofrecer las siguientes reflexiones y puntos clave para solidificar su comprensión:

1. **Coherencia en el Diseño de API:** Hemos seguido los principios RESTful. Por ejemplo, usar `GET` para obtener recursos, `POST` para crear, `PUT` para actualizar y `DELETE` para eliminar. También, la inclusión del encabezado `Location` en las respuestas de `POST` es una buena práctica REST para indicar dónde se puede acceder al recurso recién creado.
2. **Capas de la Aplicación:** La separación en capas (modelo, repositorio, servicio, controlador) es fundamental para la mantenibilidad y escalabilidad. Cada capa tiene una responsabilidad única, lo que facilita el desarrollo, las pruebas y la depuración.
3. **Validación Robusta:** La validación de entrada con Bean Validation (JSR 380) y la anotación `@Valid` es una práctica esencial para asegurar que los datos que recibe su API son válidos antes de procesarlos. Esto previene errores lógicos y mejora la seguridad.
4. **Manejo Global de Excepciones:** La clase `ManejadorExcepcionesGlobal` con `@ControllerAdvice` es la piedra angular para proporcionar respuestas de error consistentes y amigables para el cliente. Esto evita que la lógica de manejo de errores se duplique en cada controlador. Al extender `ResponseEntityExceptionHandler`, cubrimos muchas excepciones comunes de Spring de forma predeterminada.
5. **Comunicación Cliente-Servidor (RestTemplate):** `RestTemplate` es una herramienta poderosa para consumir APIs REST. Sus métodos como `getForEntity` y `postForEntity` simplifican la interacción, manejando automáticamente la serialización y deserialización de objetos Java a JSON (y viceversa) a través de `HttpMessageConverter`s. Aunque los ejemplos están en una clase de prueba, demuestran el patrón de uso para cualquier aplicación cliente Java.
6. **Spring Boot para la Agilidad:** Spring Boot reduce drásticamente el tiempo de configuración inicial y el boilerplate, permitiendo a los desarrolladores centrarse en la lógica de negocio. La autoconfiguración es una ventaja clave.

### **Analogía para Solidificar la Comprensión:**

Imagine su API REST como un **restaurante de alta cocina**.

- **Los Modelos (`Proyecto`, `Persona`)** son los **ingredientes crudos** en la despensa del chef: tienen una forma base y propiedades intrínsecas.
- **Los DTOs (`ProyectoDTO`, `PersonaDTO`)** son los **ingredientes preparados o las instrucciones de la receta para el cliente**. Contienen solo lo esencial que el cliente necesita ver o enviar, y están "validados" para asegurar que tienen la calidad y forma adecuadas (ej., la carne no está caducada, las verduras están limpias).
- **Los Repositorios** son los **almacenes o proveedores** del restaurante: saben cómo obtener y guardar cada tipo de ingrediente (Proyecto, Persona) en la despensa (base de datos).
- **Los Servicios** son los **chefs expertos** en la cocina: reciben los ingredientes preparados, aplican la lógica de la receta (lógica de negocio), combinan los ingredientes, y luego usan los almacenes para obtener o guardar los ingredientes finales.
- **Los Controladores** son los **camareros** del restaurante: reciben los pedidos de los clientes (solicitudes HTTP), los traducen para los chefs (usan los DTOs y llaman a los servicios), y luego entregan el plato terminado (respuesta HTTP) de vuelta al cliente. Si un cliente hace un pedido con instrucciones poco claras (validación fallida) o pide algo que no está en el menú (ruta no encontrada), el camarero sabe cómo responder educadamente con una explicación clara (manejo de excepciones).
- **El Manejador de Excepciones Global** es el **gerente de quejas** del restaurante: si algo sale mal en cualquier punto de la cocina o el servicio, él se encarga de estandarizar la disculpa y la explicación al cliente, asegurando que el restaurante mantenga una imagen profesional y útil incluso en caso de problemas.
- **El Cliente `RestTemplate`** es usted, el **comensal que llama al restaurante**: realiza pedidos específicos (solicitudes GET, POST, PUT, DELETE) y espera una respuesta que pueda entender (JSON deserializado a objetos Java, códigos de estado claros).

De esta forma, cada parte trabaja en armonía para ofrecer una experiencia de "servicio" eficiente y bien organizada, incluso cuando las cosas no salen según lo planeado.