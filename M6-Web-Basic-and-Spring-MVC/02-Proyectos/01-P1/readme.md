# **API REST de Gestión de Usuarios con Spring Boot**

## **1. Introducción**

Este proyecto es una implementación de una API RESTful para la gestión de usuarios. Ha sido diseñado y desarrollado con **Spring Boot**, siguiendo una arquitectura por capas limpia y aplicando las mejores prácticas de la industria. Sirve como un ejemplo canónico de cómo construir servicios web robustos, mantenibles y, sobre todo, bien probados.

## **2. Objetivo y Conceptos Demostrados**

El objetivo principal es demostrar un conjunto de patrones y tecnologías esenciales en el desarrollo de software backend moderno.

- **Arquitectura por Capas (Layered Architecture):**
    - **Controlador (`@RestController`):** Responsable de manejar las peticiones HTTP, validar la entrada y orquestar la respuesta. Es la puerta de entrada a la aplicación.
    - **Servicio (`@Service`):** Contiene la lógica de negocio principal. Actúa como intermediario entre el controlador y la persistencia, y gestiona las transacciones (`@Transactional`).
    - **Repositorio (`@Repository`):** Define la capa de acceso a datos utilizando **Spring Data JPA**. Abstrae las complejidades de la interacción con la base de datos.
- **Patrón DTO (Data Transfer Object):**
    - Se utiliza `UsuarioDTO` para definir el "contrato" de la API. Esto desacopla la representación externa de los datos de la estructura interna del modelo de dominio (`Usuario`), proporcionando seguridad y flexibilidad. La conversión se realiza con la librería **ModelMapper**.
- **Inyección de Dependencias (Dependency Injection):**
    - Se utiliza la inyección por constructor (facilitada por Lombok con `@RequiredArgsConstructor`) para asegurar que los componentes se creen en un estado válido y para facilitar las pruebas unitarias.
- **Persistencia de Datos con JPA/Hibernate:**
    - Se gestiona la persistencia de entidades en una base de datos en memoria (**H2**) a través de **Spring Data JPA**, que simplifica enormemente las operaciones CRUD.
- **Estrategia de Pruebas Completa (Testing Pyramid):**
    - El proyecto demuestra una estrategia de pruebas en múltiples niveles para garantizar la máxima calidad y confianza:
        - **Pruebas de Repositorio:** Validan el mapeo JPA y la interacción con la base de datos.
        - **Pruebas de Controlador (Slice Tests):** Prueban la capa web de forma aislada, con dependencias simuladas (mocks).
        - **Pruebas de Integración (End-to-End):** Verifican el flujo completo de la aplicación, desde la petición HTTP hasta la base de datos.

## **3. Estructura del Proyecto**

El código fuente está organizado lógicamente por funcionalidad dentro de la estructura de paquetes estándar de Maven.

```
src
├── main
│   ├── java/com/example/proyecto
│   │   ├── config/         # Clases de configuración de beans (@Configuration)
│   │   ├── controlador/    # RestControllers (Capa de API)
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── excepciones/    # Excepciones personalizadas del dominio
│   │   ├── modelo/         # Entidades JPA (Modelo de Dominio)
│   │   ├── repositorio/    # Interfaces de Spring Data JPA
│   │   └── servicio/       # Lógica de negocio (Capa de Servicio)
│   └── resources
│       ├── application.properties # Fichero principal de configuración
│       └── data.sql           # Script para la carga de datos iniciales
└── test
    └── java/com/example/proyecto
        ├── controlador/    # Pruebas de slice para los controladores
        ├── integracion/    # Pruebas de integración End-to-End
        └── repositorio/    # Pruebas de integración para la capa de persistencia

```

## **4. Diagrama de Dependencias**

El flujo de dependencias entre las capas sigue un patrón unidireccional, lo que garantiza un bajo acoplamiento y alta cohesión.

```mermaid
graph TD

    subgraph "Ciclo de Vida de la Petición (Runtime)"
        direction LR
        Client[Cliente Externo]
        Controller[Controlador @RestController]
        Service[Servicio @Service]
        Repository[Repositorio @Repository]
        Database[(Base de Datos H2)]
    end

    subgraph "Contenedor IoC y Dependencias (Startup)"
        direction TB
        IOCContainer[Spring IoC Container]
        ModelMapperBean[Bean ModelMapper]
        
        IOCContainer -->|Crea y gestiona| Controller
        IOCContainer -->|Crea y gestiona| Service
        IOCContainer -->|Crea y gestiona| Repository
        IOCContainer -->|Crea y gestiona| ModelMapperBean

        Controller -.->|Inyecta| Service
        Controller -.->|Inyecta| ModelMapperBean
        Service -.->|Inyecta| Repository
    end

    subgraph "Objetos de Datos"
        DTO[DTO (UsuarioDTO)]
        Entity[Entidad (Usuario)]
    end

    Client -->|1. Petición HTTP con DTO (JSON)| Controller
    Controller -->|2. Usa ModelMapper para convertir DTO a Entidad| Entity
    Controller -->|3. Invoca lógica de negocio| Service
    Service -->|4. Opera con la Entidad| Repository
    Repository -->|5. Persiste/Recupera la Entidad| Database
    Database -->|6. Retorna datos| Repository
    Repository -->|7. Retorna Entidad| Service
    Service -->|8. Retorna Entidad| Controller
    Controller -->|9. Usa ModelMapper para convertir Entidad a DTO| DTO
    Controller -->|10. Respuesta HTTP con DTO (JSON)| Client
```

## **5. Explicación del Flujo (Ej: Crear un Usuario)**

Para ilustrar cómo interactúan las capas, sigamos el flujo de una petición `POST` para crear un nuevo usuario:

1. **Petición HTTP:** Un cliente envía una petición `POST /api/usuarios` con un cuerpo JSON que representa un `UsuarioDTO`.
2. **Capa de Controlador:** `ControladorUsuario` recibe la petición. Spring deserializa automáticamente el JSON a un objeto `UsuarioDTO`.
3. **Mapeo DTO -> Entidad:** El controlador utiliza `ModelMapper` para convertir el `UsuarioDTO` recibido en una entidad de dominio `Usuario`.
4. **Llamada al Servicio:** Se invoca el método `guardarUsuario(usuario)` en la capa `ServicioUsuario`. Se inicia una transacción (`@Transactional`).
5. **Llamada al Repositorio:** El servicio delega la operación de guardado al `RepositorioUsuario`, llamando a su método `save(usuario)`.
6. **Interacción con la BD:** Spring Data JPA y Hibernate generan la sentencia SQL `INSERT` correspondiente y la ejecutan en la base de datos H2.
7. **Retorno de Datos:** La base de datos devuelve la entidad guardada (ahora con un ID autogenerado). Este objeto sube por la pila de llamadas hasta el controlador.
8. **Mapeo Entidad -> DTO:** El controlador recibe la entidad `Usuario` persistida y la vuelve a convertir en un `UsuarioDTO` para la respuesta.
9. **Respuesta HTTP:** El controlador construye una `ResponseEntity` con el código de estado `201 Created` y el `UsuarioDTO` del nuevo usuario en el cuerpo de la respuesta.

## **6. Ejecución y Pruebas**

### **Prerrequisitos**

- JDK 17 o superior.
- Apache Maven 3.6+

### **Ejecución de la Aplicación**

1. Clona el repositorio.
2. Navega a la raíz del proyecto desde tu terminal.
3. Ejecuta el siguiente comando Maven:

    ```
    mvn spring-boot:run
    
    ```


La API estará disponible en `http://localhost:8080`.

### **Ejecución de la Suite de Pruebas**

Para verificar la integridad y el correcto funcionamiento de todo el código, ejecuta la suite completa de pruebas:

```
mvn test

```