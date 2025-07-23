# **Gestor de Proyectos y Personas - API REST**

Este es un proyecto de demostración construido con Spring Boot que implementa una API REST para la gestión de Proyectos y Personas. El objetivo principal es mostrar las mejores prácticas de la industria para construir aplicaciones robustas, mantenibles y escalables.

## **✨ Características Principales**

- **API RESTful Completa:** Endpoints para operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en los recursos de Proyectos y Personas.
- **Arquitectura en Capas:** Clara separación de responsabilidades entre Controladores, Servicios y Repositorios.
- **Patrón DTO (Data Transfer Object):** Uso extensivo de DTOs especializados para cada caso de uso (Crear, Actualizar, Detalle, Resumen), desacoplando la capa de la API de las entidades de la base de datos y mejorando la seguridad y la flexibilidad.
- **Manejo de Excepciones Centralizado:** Un manejador global (`@ControllerAdvice`) que proporciona respuestas de error consistentes y estandarizadas en formato JSON.
- **Validación de Datos:** Reglas de validación robustas en los DTOs de entrada para garantizar la integridad de los datos.
- **Base de Datos en Memoria H2:** Configurada para un arranque y desarrollo rápidos, con una consola web para inspección directa de la base de datos.
- **Pruebas:** Incluye pruebas de integración para asegurar la calidad y el correcto funcionamiento de la aplicación.

## **🏛️ Arquitectura**

El proyecto sigue una arquitectura en capas clásica, promoviendo un bajo acoplamiento y una alta cohesión.

1. **Capa de Controlador (`@RestController`):**
    - Maneja las peticiones HTTP (`/api/...`), valida los DTOs de entrada y se comunica exclusivamente con la capa de servicio. No tiene lógica de negocio.
2. **Capa de Servicio (`@Service`):**
    - Contiene toda la lógica de negocio (ej. validar que un correo no exista antes de crear una persona).
    - Orquesta las operaciones, interactuando con la capa de repositorio.
    - Realiza el mapeo entre Entidades y DTOs usando ModelMapper.
3. **Capa de Repositorio (`@Repository`):**
    - Interfaz de acceso a datos basada en Spring Data JPA.
    - Define las consultas a la base de datos, aprovechando tanto los métodos derivados del nombre como las proyecciones optimizadas con `@Query`.
4. **Capa de Dominio/Modelo (`@Entity`):**
    - Define las entidades JPA (`Proyecto`, `Persona`) que se mapean a las tablas de la base de datos.

## **📈 Diagramas de Flujo de la Arquitectura**

Estos diagramas ilustran el recorrido de una petición a través de las diferentes capas de la aplicación, demostrando la separación de responsabilidades.

### **Flujo de Lectura (Ej: `GET /api/personas/{id}`)**

Este diagrama muestra el "camino feliz" cuando un cliente solicita un recurso que existe y el caso en que no se encuentra.

```mermaid
sequenceDiagram
    participant Client as Cliente
    participant Controller as ControladorPersona
    participant Service as ServicioPersona
    participant Repository as RepositorioPersona
    participant DB as Base de Datos (H2)
    participant Mapper as ModelMapper
    participant ExceptionHandler as ManejadorExcepcionesGlobal

    Client->>+Controller: GET /api/personas/1
    Controller->>+Service: obtenerPersonaPorId(1)
    Service->>+Repository: findById(1)
    Repository->>+DB: SELECT * FROM personas WHERE id = 1
    DB-->>-Repository: Devuelve Entidad Persona
    Repository-->>-Service: Devuelve Optional<Persona>

    alt Recurso Encontrado
        Service->>+Mapper: map(persona, PersonaDetalleDTO.class)
        Mapper-->>-Service: Devuelve PersonaDetalleDTO
        Service-->>-Controller: Devuelve PersonaDetalleDTO
        Controller-->>-Client: 200 OK con JSON del DTO
    else Recurso NO Encontrado
        Repository-->>Service: Devuelve Optional.empty()
        Service-->>Service: Lanza RecursoNoEncontradoException
        Note right of Service: La ejecución del servicio termina aquí.
        Service->>ExceptionHandler: Captura la excepción
        ExceptionHandler-->>Client: 404 Not Found con mensaje de error
    end

    deactivate Controller
    deactivate Service
    deactivate Repository
    deactivate Mapper

```

### **Flujo de Escritura (Ej: `POST /api/personas`)**

Este diagrama es más completo, ya que muestra las diferentes bifurcaciones del flujo: validación fallida, conflicto de negocio o creación exitosa.

```mermaid
sequenceDiagram
    participant Client as Cliente
    participant Spring as Framework Spring
    participant Controller as ControladorPersona
    participant Service as ServicioPersona
    participant Repository as RepositorioPersona
    participant DB as Base de Datos (H2)
    participant Mapper as ModelMapper
    participant ExceptionHandler as ManejadorExcepcionesGlobal

    Client->>+Spring: POST /api/personas con JSON Body
    Spring->>+Controller: crearPersona(@Valid @RequestBody PersonaCrearDTO)

    alt Validación de DTO Falla
        Spring-->>Spring: @Valid lanza MethodArgumentNotValidException
        Spring->>ExceptionHandler: Captura la excepción
        ExceptionHandler-->>Client: 400 Bad Request con detalles del error
    else Validación de DTO Exitosa
        Controller->>+Service: crearPersona(personaCrearDTO)
        Service->>+Repository: existsByCorreoElectronico("correo@ejemplo.com")
        Repository->>+DB: SELECT COUNT(*) FROM personas WHERE correo_electronico = ?
        DB-->>-Repository: Devuelve conteo
        Repository-->>-Service: Devuelve boolean

        alt Correo ya existe (Conflicto de Negocio)
            Service-->>Service: Lanza RecursoYaExisteException
            Note right of Service: La ejecución del servicio termina.
            Service->>ExceptionHandler: Captura la excepción
            ExceptionHandler-->>Client: 409 Conflict con mensaje de error
        else Correo es único (Camino Feliz)
            Service->>+Mapper: map(personaCrearDTO, Persona.class)
            Mapper-->>-Service: Devuelve nueva Entidad Persona (sin ID)
            Service->>+Repository: save(persona)
            Repository->>+DB: INSERT INTO personas (...)
            DB-->>-Repository: Devuelve Entidad Persona guardada (con ID)
            Repository-->>-Service: Devuelve Persona guardada
            Service->>+Mapper: map(personaGuardada, PersonaDetalleDTO.class)
            Mapper-->>-Service: Devuelve PersonaDetalleDTO
            Service-->>-Controller: Devuelve PersonaDetalleDTO creado
            Controller-->>-Client: 201 Created con Header 'Location' y JSON del DTO
        end
    end

    deactivate Controller
    deactivate Service
    deactivate Repository
    deactivate Mapper

```

## **🛠️ Stack Tecnológico**

- **Lenguaje:** Java 17+
- **Framework:** Spring Boot 3.x
- **Módulos Spring:** Spring MVC, Spring Data JPA, Spring Boot Starter Test
- **Persistencia:** Hibernate, H2 Database Engine
- **Utilidades:** Lombok, ModelMapper
- **Gestor de Dependencias:** Maven
- **Pruebas:** JUnit 5, Mockito

## **🚀 Cómo Empezar**

Sigue estos pasos para clonar, construir y ejecutar el proyecto en tu máquina local.

### **Prerrequisitos**

- JDK 17 o superior.
- Apache Maven 3.6+.
- Tu IDE favorito (IntelliJ IDEA, Eclipse, VS Code).

### **Instalación y Ejecución**

1. **Clona el repositorio:**

    ```
    git clone <URL_DEL_REPOSITORIO>
    cd integralproyecto
    
    ```

2. **Construye el proyecto con Maven:** Este comando descargará las dependencias y compilará el código.

    ```
    mvn clean install
    
    ```

3. **Ejecuta la aplicación:**

    ```
    java -jar target/integralproyecto-0.0.1-SNAPSHOT.jar
    
    ```

4. **¡Listo!** La aplicación estará corriendo en `http://localhost:8080`.

## **🌐 Uso de la Aplicación**

Una vez que la aplicación está en ejecución, puedes interactuar con ella de las siguientes maneras:

### **1. Consola de la Base de Datos H2**

Para inspeccionar la base de datos en tiempo real:

- **URL:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:integraldb`
- **User Name:** `sa`
- **Password:** `password`

### **2. API REST**

Puedes usar cualquier cliente API como Postman o Insomnia para probar los endpoints.

### **Endpoints de Proyectos (`/api/proyectos`)**

| **Método HTTP** | **Endpoint** | **Descripción** | **Cuerpo (Request Body)** | **Respuesta Exitosa** |
| --- | --- | --- | --- | --- |
| `GET` | `/` | Lista todos los proyectos. | N/A | `200 OK` con una lista de `ProyectoResumenDTO` |
| `GET` | `/{id}` | Obtiene un proyecto por ID. | N/A | `200 OK` con un `ProyectoDetalleDTO` |
| `POST` | `/` | Crea un nuevo proyecto. | `ProyectoCrearDTO` (JSON) | `201 Created` con el `ProyectoDetalleDTO` creado |
| `PUT` | `/{id}` | Actualiza un proyecto existente. | `ProyectoActualizarDTO` (JSON) | `200 OK` con el `ProyectoDetalleDTO` actualizado |
| `DELETE` | `/{id}` | Elimina un proyecto. | N/A | `204 No Content` |

**Ejemplo de `POST /api/proyectos` Body:**

```
{
  "nombre": "Desarrollo de API con Spring Boot",
  "descripcion": "Implementar las mejores prácticas de arquitectura."
}

```

### **Endpoints de Personas (`/api/personas`)**

| **Método HTTP** | **Endpoint** | **Descripción** | **Cuerpo (Request Body)** | **Respuesta Exitosa** |
| --- | --- | --- | --- | --- |
| `GET` | `/` | Lista todas las personas. | N/A | `200 OK` con una lista de `PersonaResumenDTO` |
| `GET` | `/{id}` | Obtiene una persona por ID. | N/A | `200 OK` con un `PersonaDetalleDTO` |
| `POST` | `/` | Crea una nueva persona. | `PersonaCrearDTO` (JSON) | `201 Created` con el `PersonaDetalleDTO` creado |
| `PUT` | `/{id}` | Actualiza una persona existente. | `PersonaActualizarDTO` (JSON) | `200 OK` con el `PersonaDetalleDTO` actualizado |
| `DELETE` | `/{id}` | Elimina una persona. | N/A | `204 No Content` |

**Ejemplo de `POST /api/personas` Body:**

```
{
  "nombre": "Ana García",
  "edad": 30,
  "correoElectronico": "ana.garcia@example.com"
}

```

## **🧪 Pruebas**

El proyecto está configurado con un conjunto de pruebas de integración para garantizar la calidad del código.

Para ejecutar todas las pruebas, utiliza el siguiente comando de Maven:

```
mvn test

```

Esto ejecutará:

- **Pruebas de Contexto:** Como `ApplicationTests.java`, que verifica que el contexto de Spring se cargue correctamente.
- **Pruebas de Integración con MockMvc:** Como `ClienteApiRest.java`, que simula llamadas HTTP a los controladores para verificar el comportamiento de extremo a extremo sin necesidad de un servidor real.