
---

### **1. Título del Caso Práctico:**
**Construyendo una API RESTful con Spring Boot: Gestión de Datos y Entidades para Aplicaciones Modernas**

### **2. Resumen del Problema y Objetivos de Aprendizaje:**

Las aplicaciones web modernas requieren un backend robusto capaz de gestionar datos de manera eficiente y segura. Frecuentemente, el desafío radica en cómo exponer la información de la capa de persistencia (Entidades JPA) a los clientes (frontends web, otras APIs) sin comprometer la seguridad, el rendimiento o la mantenibilidad del sistema. Exponer directamente las entidades internas puede llevar a la fuga de datos sensibles y a una menor flexibilidad en la evolución de la API. Este caso práctico aborda la construcción de una API RESTful con Spring Boot, enfocándose en la definición de un modelo de datos claro, la implementación de la capa de persistencia con Spring Data JPA y la crucial tarea de transformar las entidades en objetos de transferencia de datos (DTOs) para una interacción cliente-servidor optimizada y segura.

Al completar este caso práctico, un desarrollador junior logrará los siguientes objetivos de aprendizaje clave:

1.  **Fundamentos del Desarrollo Web:** Comprender y aplicar el modelo Cliente-Servidor y el protocolo HTTP, y cómo Spring MVC los utiliza para el desarrollo de aplicaciones web.
2.  **API RESTful con Spring Boot:** Dominar la creación y exposición de endpoints RESTful usando `@RestController`, `@RequestMapping`, y anotaciones HTTP-específicas (`@GetMapping`, `@PostMapping`, etc.) para gestionar recursos.
3.  **Persistencia de Datos con JPA:** Implementar la capa de persistencia con Spring Data JPA, incluyendo la definición de entidades (`@Entity`, `@Id`, `@GeneratedValue`) y la gestión de repositorios, así como la inicialización de datos para desarrollo y pruebas.
4.  **Patrón DTO y Conversión de Datos:** Aplicar el patrón DTO para desacoplar la capa de API de la capa de persistencia, realizando conversiones seguras y eficientes entre entidades y DTOs, ya sea manualmente o con la ayuda de bibliotecas como ModelMapper.
5.  **Estrategias de Testing en Spring Boot:** Escribir tests unitarios y de integración efectivos para las diferentes capas de la aplicación (`@SpringBootTest`, `@WebMvcTest`, `@DataJpaTest`, `@MockBean`, `@Sql`), asegurando la calidad y el comportamiento esperado del sistema.

### **3. Propuesta de Solución (Guía Paso a Paso):**

Vamos a construir una API RESTful que gestiona información de usuarios. Utilizaremos Spring Boot para la configuración automática y la facilidad de desarrollo, Spring Data JPA para la persistencia de datos y el patrón DTO para una comunicación limpia y segura con el cliente.

#### **a. Configuración Inicial del Proyecto**

Lo primero es configurar tu entorno de desarrollo. Para un proyecto Spring Boot, Maven o Gradle son tus herramientas de construcción principales.

**1. Generación del Proyecto:**
Puedes usar Spring Initializr (https://start.spring.io) para generar la estructura básica del proyecto. Selecciona las siguientes dependencias:
*   **Spring Web:** Para construir aplicaciones web y APIs RESTful.
*   **Spring Data JPA:** Para interactuar con bases de datos relacionales usando el estándar JPA.
*   **H2 Database:** Una base de datos en memoria ligera, ideal para desarrollo y pruebas.
*   **Spring Boot DevTools:** Para un reinicio rápido y otras utilidades de desarrollo.
*   **Spring Boot Test:** Para soporte robusto de testing.
*   **Lombok (Opcional):** Para reducir el código repetitivo (getters, setters, constructores, etc.).

**2. Estructura de Directorios:**
La estructura típica de un proyecto Spring Boot es intuitiva, con paquetes para cada capa: `controlador` (controller), `servicio` (service), `repositorio` (repository), `modelo` (model).

**3. Archivo `pom.xml` (Ejemplo de Dependencias Críticas):**
Asegúrate de que tu `pom.xml` (si usas Maven) contenga las dependencias mencionadas. Spring Boot maneja la versión de muchas de ellas a través de su `spring-boot-starter-parent`.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.2</version> <!-- Usar la versión más reciente o recomendada -->
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.tuempresa.proyecto</groupId>
    <artifactId>gestion-usuarios</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>gestion-usuarios</name>
    <description>API RESTful para la gestión de usuarios</description>

    <properties>
        <java.version>17</java.version> <!-- O tu versión de Java preferida, 17+ recomendado -->
    </properties>

    <dependencies>
        <!-- Soporte para construir APIs RESTful y aplicaciones web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- Soporte para la persistencia de datos con JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <!-- Base de datos en memoria para desarrollo y pruebas -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!-- Herramientas de desarrollo de Spring Boot (ej. reinicio rápido) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <!-- Lombok para reducir boilerplate (opcional) -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!-- Soporte de testing para Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- ModelMapper para la conversión de DTOs (añadimos esta manualmente) -->
        <dependency>
            <groupId>org.modelmapper</groupId>
            <artifactId>modelmapper</artifactId>
            <version>3.2.0</version> <!-- Revisar la última versión -->
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

**4. Clase Principal de la Aplicación:**
La clase principal de tu aplicación Spring Boot es el punto de entrada. `@SpringBootApplication` es una anotación de conveniencia que combina `@Configuration`, `@EnableAutoConfiguration` y `@ComponentScan`, habilitando la configuración automática y el escaneo de componentes.

```java
package com.tuempresa.proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication marca la clase principal de la aplicación Spring Boot.
// Permite la auto-configuración y el escaneo de componentes.
@SpringBootApplication
public class AplicacionGestionUsuarios {

    public static void principal(String[] args) {
        // Inicia la aplicación Spring Boot.
        SpringApplication.run(AplicacionGestionUsuarios.class, args);
    }
}
```

#### **b. Modelo de Datos: Entidades y DTOs**

En el desarrollo de software, es fundamental diferenciar entre el modelo de datos interno de tu aplicación (entidades de persistencia) y el modelo de datos que expones a los clientes de tu API (DTOs).

**1. Entidad JPA (`Usuario`):**
Las entidades JPA (`@Entity`) representan tablas en tu base de datos. Cada instancia de una entidad corresponde a una fila en la tabla. El `@Id` marca la clave primaria y `@GeneratedValue` define cómo se generará ese ID.
Aquí, `GenerationType.IDENTITY` es común para bases de datos que soportan columnas auto-incrementales.

```java
package com.tuempresa.proyecto.modelo;

import jakarta.persistence.Entity; // Importa de Jakarta Persistence API
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter; // De Lombok, si lo usas
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Entity marca esta clase como una entidad JPA.
// @Table es opcional si el nombre de la tabla coincide con el de la clase.
@Entity
@Table(name = "usuarios") // Nombre de la tabla en la base de datos
@Getter // Genera getters para todos los campos (Lombok)
@Setter // Genera setters para todos los campos (Lombok)
@NoArgsConstructor // Genera un constructor sin argumentos (Lombok)
public class Usuario {

    // @Id marca el campo como la clave primaria.
    // @GeneratedValue configura la estrategia de generación del ID.
    // GenerationType.IDENTITY usa una columna de identidad en la base de datos (auto-incremento).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Usamos Long (wrapper) para permitir valores nulos iniciales

    private String nombre;
    private String correoElectronico;

    // Constructor para facilitar la creación de objetos (sin ID, ya que es generado)
    public Usuario(String nombre, String correoElectronico) {
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
    }
}
```

**2. Objeto de Transferencia de Datos (DTO) (`UsuarioDTO`):**
Los DTOs (`Data Transfer Objects`) son clases simples de Java que se utilizan para transferir datos entre la aplicación y el cliente (o entre capas). Son cruciales para desacoplar tu API de tu modelo de persistencia. Esto te permite controlar exactamente qué datos se exponen, evitando fugas de información sensible y haciendo que tu API sea más flexible a cambios internos.

A partir de Java 16, las `record classes` son ideales para DTOs debido a su concisión e inmutabilidad.

```java
package com.tuempresa.proyecto.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor; // Genera un constructor con todos los argumentos (Lombok)

// Usamos record para un DTO inmutable (Java 16+).
// Si usas una versión anterior de Java o necesitas mutabilidad, usarías una clase normal.
public record UsuarioDTO(
    Long id, // El ID puede ser nulo al crear un nuevo usuario
    String nombre,
    String correoElectronico
) {
    // Las record classes generan automáticamente getters, equals(), hashCode() y toString().
    // Si necesitas un constructor específico o validación, puedes añadirlo.
    // public UsuarioDTO { // Constructor compacto
    //    if (nombre == null || nombre.isBlank()) {
    //        throw new IllegalArgumentException("El nombre no puede estar vacío.");
    //    }
    // }
}

// Alternativa para Java < 16 o si necesitas setters:
/*
package com.tuempresa.proyecto.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter // Genera getters para todos los campos (Lombok)
@Setter // Genera setters para todos los campos (Lombok)
@NoArgsConstructor // Genera un constructor sin argumentos (Lombok)
@AllArgsConstructor // Genera un constructor con todos los argumentos (Lombok)
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String correoElectronico;
}
*/
```

#### **c. Capa de Persistencia: Repositorios**

La capa de persistencia se encarga de las operaciones de base de datos. Spring Data JPA simplifica enormemente esta tarea al proporcionar interfaces de repositorio que automáticamente implementan métodos CRUD (Crear, Leer, Actualizar, Borrar) sin que tengas que escribir una sola línea de código SQL o JDBC.

**1. Interfaz `RepositorioUsuario`:**
Define una interfaz que extiende `JpaRepository`. Esto te da acceso a métodos como `save()`, `findById()`, `findAll()`, `deleteById()`, etc. Puedes añadir tus propios métodos de consulta (por ejemplo, `findByNombre`) y Spring Data JPA los implementará automáticamente basándose en la convención de nombres.

```java
package com.tuempresa.proyecto.repositorio;

import com.tuempresa.proyecto.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository indica que esta interfaz es un componente de repositorio de Spring.
// Extiende JpaRepository para obtener métodos CRUD básicos.
// <Usuario, Long> indica que es un repositorio para la entidad Usuario con clave primaria de tipo Long.
@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {

    // Método de consulta personalizado. Spring Data JPA lo implementa automáticamente.
    Usuario findByNombre(String nombre); // Encontrar un usuario por su nombre
}
```

**2. Inicialización de Datos (Opcional, pero útil para desarrollo):**
Para tener datos de prueba al iniciar la aplicación, puedes usar un archivo `data.sql` en `src/main/resources`. Spring Boot lo ejecutará automáticamente al arrancar. Para H2 (base de datos en memoria), esto funciona por defecto. Para otras bases de datos, podrías necesitar `spring.sql.init.mode=always`.

```sql
-- src/main/resources/data.sql
-- Script SQL para insertar datos iniciales en la tabla usuarios.
-- Esto se ejecuta automáticamente al iniciar la aplicación con H2.

INSERT INTO usuarios (nombre, correo_electronico) VALUES ('Juan Pérez', 'juan.perez@example.com');
INSERT INTO usuarios (nombre, correo_electronico) VALUES ('Maria Garcia', 'maria.garcia@example.com');
INSERT INTO usuarios (nombre, correo_electronico) VALUES ('Carlos Sanchez', 'carlos.sanchez@example.com');
```
*Nota: Es posible que necesites ajustar `spring.jpa.defer-datasource-initialization=true` en `application.properties` para asegurarte de que Hibernate cree las tablas antes de que se inserten los datos, especialmente si estás usando `schema.sql` y `data.sql` juntos.*

#### **d. Capa de Servicio: Lógica de Negocio**

La capa de servicio contiene la lógica de negocio principal de tu aplicación. Es donde coordinas las operaciones entre repositorios y aplicas las reglas de negocio. Esta capa se inyecta en los controladores.

**1. Interfaz `IServicioUsuario`:**
Define una interfaz para tu servicio. Esto promueve el bajo acoplamiento y facilita el testing, ya que puedes inyectar mocks de la interfaz en tus tests.

```java
package com.tuempresa.proyecto.servicio;

import com.tuempresa.proyecto.modelo.Usuario;
import java.util.List;
import java.util.Optional;

public interface IServicioUsuario {
    List<Usuario> encontrarTodosUsuarios();
    Optional<Usuario> encontrarUsuarioPorId(Long id);
    Usuario guardarUsuario(Usuario usuario);
    void eliminarUsuarioPorId(Long id);
    Usuario actualizarUsuario(Long id, Usuario usuario);
}
```

**2. Implementación `ServicioUsuario`:**
Implementa la lógica de negocio utilizando el `RepositorioUsuario`.

```java
package com.tuempresa.proyecto.servicio;

import com.tuempresa.proyecto.modelo.Usuario;
import com.tuempresa.proyecto.repositorio.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// @Service indica que esta clase es un componente de servicio de Spring.
@Service
public class ServicioUsuario implements IServicioUsuario {

    // Inyección de dependencia del RepositorioUsuario.
    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Override
    public List<Usuario> encontrarTodosUsuarios() {
        return repositorioUsuario.findAll(); // Obtiene todos los usuarios
    }

    @Override
    public Optional<Usuario> encontrarUsuarioPorId(Long id) {
        return repositorioUsuario.findById(id); // Busca un usuario por ID
    }

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        return repositorioUsuario.save(usuario); // Guarda un nuevo usuario
    }

    @Override
    public void eliminarUsuarioPorId(Long id) {
        repositorioUsuario.deleteById(id); // Elimina un usuario por ID
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        // En este caso, buscamos el usuario existente. Si no se encuentra, retornamos nulo o lanzamos excepción.
        // Aquí lanzamos una excepción para demostrar un manejo de error.
        return repositorioUsuario.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
            usuarioExistente.setCorreoElectronico(usuarioActualizado.getCorreoElectronico());
            return repositorioUsuario.save(usuarioExistente);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id)); // Manejo de excepción
    }
}
```

#### **e. Capa de API/Controlador: Definición de Endpoints REST**

El controlador es el "punto de entrada" de tu API. Recibe las solicitudes HTTP, las procesa (usando el servicio) y devuelve una respuesta HTTP. Aquí es donde la conversión a DTOs se vuelve vital.

**1. Controlador REST (`ControladorUsuario`):**
Utiliza `@RestController` para indicar que es un controlador RESTful (combina `@Controller` y `@ResponseBody`). `@RequestMapping` define la ruta base del controlador. Anotaciones como `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` mapean los métodos a verbos HTTP específicos. `@PathVariable` y `@RequestBody` enlazan partes de la URL y el cuerpo de la solicitud a los parámetros del método.

```java
package com.tuempresa.proyecto.controlador;

import com.tuempresa.proyecto.dto.UsuarioDTO;
import com.tuempresa.proyecto.modelo.Usuario;
import com.tuempresa.proyecto.servicio.IServicioUsuario;
import org.modelmapper.ModelMapper; // Para la conversión de DTOs
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Para códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Para respuestas HTTP completas
import org.springframework.web.bind.annotation.*; // Importa todas las anotaciones web
import org.springframework.web.server.ResponseStatusException; // Para lanzar excepciones con estado HTTP

import java.util.List;
import java.util.stream.Collectors;

// @RestController es una anotación de conveniencia para APIs RESTful.
// Combina @Controller y @ResponseBody.
@RestController
// @RequestMapping define la URL base para todos los métodos en este controlador.
@RequestMapping("/api/usuarios")
// @CrossOrigin permite solicitudes de origen cruzado, útil para frontends en otros dominios.
@CrossOrigin(origins = "http://localhost:4200") // Ejemplo, ajustar según tu frontend
public class ControladorUsuario {

    // Inyección de dependencia del servicio de usuario.
    @Autowired
    private IServicioUsuario servicioUsuario;

    // Inyección de dependencia de ModelMapper para conversiones Entity-DTO.
    @Autowired
    private ModelMapper modelMapper;

    // GET /api/usuarios
    // @GetMapping mapea solicitudes GET a este método.
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosUsuarios() {
        List<Usuario> usuarios = servicioUsuario.encontrarTodosUsuarios();
        // Convierte la lista de entidades a DTOs usando streams y ModelMapper.
        List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDTO); // Retorna 200 OK con la lista de DTOs
    }

    // GET /api/usuarios/{id}
    // @PathVariable enlaza una parte de la URL (id) con un parámetro del método.
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        // Busca el usuario por ID y si no lo encuentra, lanza una excepción con estado NOT_FOUND.
        Usuario usuario = servicioUsuario.encontrarUsuarioPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));
        return ResponseEntity.ok(convertirA_DTO(usuario)); // Retorna 200 OK con el DTO del usuario
    }

    // POST /api/usuarios
    // @PostMapping mapea solicitudes POST a este método.
    // @RequestBody mapea el cuerpo de la solicitud HTTP a un objeto UsuarioDTO.
    @PostMapping
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        // Convierte el DTO a entidad para guardar en la base de datos.
        Usuario nuevoUsuario = convertirA_Entidad(usuarioDTO);
        Usuario usuarioGuardado = servicioUsuario.guardarUsuario(nuevoUsuario);
        return new ResponseEntity<>(convertirA_DTO(usuarioGuardado), HttpStatus.CREATED); // Retorna 201 Created con el DTO
    }

    // PUT /api/usuarios/{id}
    // @PutMapping mapea solicitudes PUT a este método.
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        if (!id.equals(usuarioDTO.id())) { // Verifica que el ID de la URL y el DTO coincidan
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los IDs no coinciden."); // Lanza 400 Bad Request
        }
        Usuario usuarioActualizado = servicioUsuario.actualizarUsuario(id, convertirA_Entidad(usuarioDTO));
        return ResponseEntity.ok(convertirA_DTO(usuarioActualizado)); // Retorna 200 OK con el DTO actualizado
    }

    // DELETE /api/usuarios/{id}
    // @DeleteMapping mapea solicitudes DELETE a este método.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        servicioUsuario.eliminarUsuarioPorId(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

    // Método para convertir de Entidad a DTO.
    private UsuarioDTO convertirA_DTO(Usuario usuario) {
        // ModelMapper.map() hace la mayor parte del trabajo de mapeo de campos.
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    // Método para convertir de DTO a Entidad.
    private Usuario convertirA_Entidad(UsuarioDTO usuarioDTO) {
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        // Aquí puedes añadir lógica adicional si el ID del DTO necesita ser manejado para una actualización.
        // Si el ID del DTO es nulo, significa que es una creación, si no, se usa para una actualización.
        if (usuarioDTO.id() != null) {
            usuario.setId(usuarioDTO.id());
        }
        return usuario;
    }
}
```

#### **f. Seguridad y Configuraciones Adicionales (CORS y Testing)**

**1. CORS (Cross-Origin Resource Sharing):**
Cuando tu frontend (por ejemplo, una aplicación Angular) se ejecuta en un dominio o puerto diferente al de tu backend (lo cual es muy común en desarrollo `http://localhost:4200` vs `http://localhost:8080`), los navegadores aplican una política de seguridad de "mismo origen" que restringe las solicitudes. La anotación `@CrossOrigin` en Spring Boot es la solución más sencilla para habilitar la comunicación entre orígenes específicos. Puedes aplicarla a nivel de método o a nivel de clase, como se hizo en el `ControladorUsuario`.

```java
// Ejemplo de @CrossOrigin en el controlador:
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200") // Permite solicitudes desde el puerto 4200 (típico de Angular)
public class ControladorUsuario {
    // ... métodos del controlador
}
```

**2. Testing de la Aplicación:**
El testing es una parte integral del desarrollo profesional. Spring Boot ofrece un excelente soporte para pruebas, permitiéndote probar diferentes capas de tu aplicación de manera eficiente.

**i. Test de Integración de la Capa de Persistencia (`@DataJpaTest`):**
`@DataJpaTest` es una anotación especializada para probar la capa JPA. Configura automáticamente una base de datos en memoria (como H2), Spring Data JPA y Hibernate, y te proporciona un `TestEntityManager` para preparar datos de prueba.

```java
package com.tuempresa.proyecto.repositorio;

import com.tuempresa.proyecto.modelo.Usuario;
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
```

**ii. Test Unitario de la Capa de Controlador (`@WebMvcTest`):**
`@WebMvcTest` se enfoca únicamente en la capa web, instanciando solo los componentes relacionados con MVC. Esto hace que las pruebas sean más rápidas. Combínalo con `@MockBean` para simular dependencias de la capa de servicio, ya que el servicio no se carga en este tipo de test. `MockMvc` te permite simular solicitudes HTTP sin arrancar un servidor completo.

```java
package com.tuempresa.proyecto.controlador;

import com.tuempresa.proyecto.dto.UsuarioDTO;
import com.tuempresa.proyecto.modelo.Usuario;
import com.tuempresa.proyecto.servicio.IServicioUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; // Anotación para test de MVC
import org.springframework.boot.test.mock.mockito.MockBean; // Para simular dependencias
import org.springframework.http.MediaType; // Para tipos de medios HTTP
import org.springframework.test.web.servlet.MockMvc; // Para simular solicitudes HTTP

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.BDDMockito.given; // Para definir el comportamiento de los mocks
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // Builders para solicitudes HTTP
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; // Para verificar el JSON de la respuesta
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; // Para verificar el estado HTTP
import static org.hamcrest.Matchers.is; // Hamcrest para aserciones
import static org.hamcrest.Matchers.hasSize;

// @WebMvcTest enfoca el test en el ControladorUsuario. No carga toda la aplicación.
@WebMvcTest(ControladorUsuario.class)
public class ControladorUsuarioIntegracionTest {

    // MockMvc para simular llamadas HTTP.
    @Autowired
    private MockMvc mvc;

    // @MockBean crea un mock de IServicioUsuario y lo inyecta en el controlador.
    @MockBean
    private IServicioUsuario servicioUsuario;

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
                .andExpect(jsonPath("$.nombre", is(juan.getNombre()))); // Verifica el nombre del primer elemento
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
        mvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"Pedro\", \"correoElectronico\": \"pedro@test.com\"}")) // Cuerpo de la solicitud
                .andExpect(status().isCreated()) // Espera un estado HTTP 201 Created
                .andExpect(jsonPath("$.nombre", is(usuarioDTO.nombre()))); // Verifica el nombre del usuario creado
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
```

**iii. Test de Integración Completo (`@SpringBootTest`):**
Si necesitas probar el comportamiento de tu aplicación con el contexto completo de Spring cargado (incluyendo la base de datos real, no mocks), usa `@SpringBootTest`. Puedes configurar el entorno web (por ejemplo, `WebEnvironment.RANDOM_PORT` para un puerto aleatorio) para asegurarte de que tu aplicación se inicia correctamente.

```java
package com.tuempresa.proyecto.integracion;

import com.tuempresa.proyecto.modelo.Usuario;
import com.tuempresa.proyecto.repositorio.RepositorioUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest; // Para cargar el contexto completo de Spring Boot
import org.springframework.boot.test.web.client.TestRestTemplate; // Para realizar llamadas HTTP en tests de integración
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @Test
    public void cuandoObtenerUsuarioPorId_entoncesUsuarioRetornado() {
        // Dada: un usuario existente en la base de datos.
        Usuario usuarioExistente = new Usuario("Ana Perez", "ana.perez@example.com");
        repositorioUsuario.save(usuarioExistente);

        // Cuando: realizamos una solicitud GET al endpoint del usuario.
        ResponseEntity<Usuario> respuesta = restTemplate.getForEntity(
                "/api/usuarios/{id}", Usuario.class, usuarioExistente.getId());

        // Entonces: verificamos que la respuesta sea 200 OK y contenga el usuario correcto.
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(respuesta.getBody()).isNotNull();
        assertThat(respuesta.getBody().getNombre()).isEqualTo(usuarioExistente.getNombre());
    }

    @Test
    public void cuandoCrearUsuario_entoncesUsuarioEsPersistido() {
        // Dada: un nuevo usuario.
        Usuario nuevoUsuario = new Usuario("Roberto", "roberto@test.com");

        // Cuando: enviamos una solicitud POST para crear el usuario.
        ResponseEntity<Usuario> respuesta = restTemplate.postForEntity(
                "/api/usuarios", nuevoUsuario, Usuario.class);

        // Entonces: verificamos que la respuesta sea 201 Created y que el usuario exista en la DB.
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(respuesta.getBody()).isNotNull();
        assertThat(respuesta.getBody().getId()).isNotNull();

        // Verificación directa en la base de datos.
        Optional<Usuario> usuarioEncontrado = repositorioUsuario.findById(respuesta.getBody().getId());
        assertThat(usuarioEncontrado).isPresent();
        assertThat(usuarioEncontrado.get().getNombre()).isEqualTo(nuevoUsuario.getNombre());
    }
}
```

---

