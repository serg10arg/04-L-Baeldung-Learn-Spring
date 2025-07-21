# **Gestión de Proyectos - Aplicación Web**

Una aplicación web robusta construida con Spring Boot para la gestión básica de proyectos y sus tareas asociadas. Este proyecto demuestra una arquitectura limpia y desacoplada, siguiendo las mejores prácticas de la industria para el desarrollo de aplicaciones empresariales con Spring.

## **Tabla de Contenidos**

1. [Acerca del Proyecto](https://www.google.com/search?q=%231-acerca-del-proyecto)
    - [Características Principales](https://www.google.com/search?q=%23caracter%C3%ADsticas-principales)
    - [Tecnologías Utilizadas](https://www.google.com/search?q=%23tecnolog%C3%ADas-utilizadas)
2. [Cómo Empezar](https://www.google.com/search?q=%232-c%C3%B3mo-empezar)
    - [Prerrequisitos](https://www.google.com/search?q=%23prerrequisitos)
    - [Instalación y Ejecución](https://www.google.com/search?q=%23instalaci%C3%B3n-y-ejecuci%C3%B3n)
3. [Estructura del Proyecto](https://www.google.com/search?q=%233-estructura-del-proyecto)
4. [Funcionalidades Web](https://www.google.com/search?q=%234-funcionalidades-web)
5. [Ejecución de Pruebas](https://www.google.com/search?q=%235-ejecuci%C3%B3n-de-pruebas)
6. [Decisiones de Arquitectura](https://www.google.com/search?q=%236-decisiones-de-arquitectura)

## **1. Acerca del Proyecto**

Este proyecto es una aplicación web MVC (Modelo-Vista-Controlador) que permite a los usuarios crear, ver y gestionar proyectos. Cada proyecto puede tener múltiples tareas asociadas, cada una con su propio estado (Pendiente, En Progreso, etc.).

El objetivo principal es servir como un ejemplo claro y profesional de cómo construir aplicaciones Spring Boot, con un fuerte énfasis en:

- **Arquitectura por Capas:** Separación clara de responsabilidades (Controlador, Servicio, Repositorio).
- **Seguridad y Desacoplamiento:** Uso del patrón DTO (Data Transfer Object) para controlar los datos que se exponen y reciben.
- **Calidad de Código y Pruebas:** Cobertura de pruebas unitarias y de integración para garantizar la fiabilidad.

### **Características Principales**

- Listar todos los proyectos existentes.
- Crear nuevos proyectos a través de un formulario web.
- Ver los detalles de un proyecto específico, incluyendo todas sus tareas asociadas.
- Validación de datos en el backend para asegurar la integridad.
- Manejo de errores y redirecciones para una experiencia de usuario fluida.

### **Tecnologías Utilizadas**

- **Framework:** Spring Boot 3
- **Lenguaje:** Java 17+
- **Acceso a Datos:** Spring Data JPA / Hibernate
- **Base de Datos:** H2 In-Memory Database (para desarrollo y pruebas)
- **Motor de Plantillas:** Thymeleaf
- **Utilidades:** Lombok, ModelMapper
- **Pruebas:** JUnit 5, Mockito, AssertJ
- **Build Tool:** Maven

## **2. Cómo Empezar**

Sigue estos pasos para tener una copia local del proyecto en funcionamiento.

### **Prerrequisitos**

Asegúrate de tener instalado lo siguiente:

- JDK 17 o superior.
- Apache Maven 3.6 o superior.
- Tu IDE favorito (IntelliJ IDEA, Eclipse, VSCode).

### **Instalación y Ejecución**

1. **Clona el repositorio:**

    ```
    git clone https://github.com/tu-usuario/gestion-proyectos.git
    cd gestion-proyectos
    
    ```

2. **Ejecuta la aplicación usando Maven:**

    ```
    mvn spring-boot:run
    
    ```

   La aplicación se iniciará y estará disponible en `http://localhost:8080`.

3. **Accede a la aplicación:** Abre tu navegador y ve a `http://localhost:8080/proyectos`. Verás la lista de proyectos de ejemplo que se cargan al inicio gracias al perfil `dev`.

## **3. Estructura del Proyecto**

El proyecto sigue una estructura de paquetes por funcionalidad, promoviendo una arquitectura limpia y mantenible.

```
com.example.gestionproyectos
├── config/         # Clases de configuración (ej. ModelMapper).
├── controlador/    # Controladores Spring MVC que manejan las peticiones web.
├── dto/            # Data Transfer Objects para la comunicación entre capas.
├── excepciones/    # Clases de excepciones personalizadas.
├── modelos/        # Entidades JPA que representan el modelo de dominio.
├── repositorios/   # Interfaces de Spring Data JPA para el acceso a datos.
└── servicio/       # Lógica de negocio de la aplicación.

```

## **4. Funcionalidades Web**

La aplicación expone las siguientes vistas a través del navegador:

- `GET /proyectos`: Muestra una página con la lista de todos los proyectos.
- `GET /proyectos/nuevo`: Muestra un formulario para crear un nuevo proyecto.
- `POST /proyectos/nuevo`: Procesa los datos del formulario para crear un proyecto. Realiza validación y redirige en caso de éxito.
- `GET /proyectos/{id}`: Muestra una página con los detalles de un proyecto específico y sus tareas.

## **5. Ejecución de Pruebas**

El proyecto cuenta con una suite de pruebas robusta para asegurar la calidad del código.

Para ejecutar todas las pruebas, utiliza el siguiente comando de Maven:

```
mvn test

```

Esto ejecutará:

- **Pruebas de Integración (`@DataJpaTest`):** Verifican que la capa de persistencia (repositorios y entidades JPA) se integra correctamente con la base de datos.
- **Pruebas de la Capa Web (`@WebMvcTest`):** Verifican que los controladores responden correctamente a las peticiones HTTP, manejan los datos y devuelven las vistas adecuadas, todo ello de forma aislada y sin levantar la aplicación completa.

## **6. Decisiones de Arquitectura**

Este proyecto implementa varios patrones de diseño clave para lograr una arquitectura profesional:

- **Patrón DTO (Data Transfer Object):** Se utilizan DTOs (`ProyectoCrearDTO`, `ProyectoVerDTO`) para desacoplar la capa web del modelo de dominio. Esto previene la exposición de detalles internos de las entidades JPA y resuelve problemas de serialización (como las referencias circulares), resultando en una API más segura y estable.
- **Inyección de Dependencias por Constructor:** Todas las dependencias (como servicios en controladores o repositorios en servicios) se inyectan a través del constructor. Esto hace que los componentes sean más fáciles de probar y asegura que sus dependencias esenciales sean inmutables.
- **Capa de Servicio:** Toda la lógica de negocio reside en la capa de servicio (`ServicioProyecto`). El controlador actúa como un orquestador delgado, delegando el trabajo al servicio. Esto mantiene el código organizado y facilita su reutilización.
- **Perfiles de Spring (`@Profile`):** Se utiliza el perfil `dev` para el `CommandLineRunner` que siembra la base de datos con datos de ejemplo. Esto es una práctica crucial que permite que la configuración de conveniencia para el desarrollo no interfiera con el entorno limpio y controlado de las pruebas automatizadas.