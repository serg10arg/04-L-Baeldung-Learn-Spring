
# Sistema de Gestión de Inventario - Demo de Inyección de Dependencias en Spring

Este proyecto es una aplicación de demostración construida con Spring Boot para ilustrar conceptos avanzados de **Inyección de Dependencias (DI)**. Se enfoca en cómo construir un sistema modular y extensible, capaz de manejar múltiples implementaciones para una misma interfaz y gestionar dependencias opcionales de forma segura.

## Descripción General

El sistema simula el backend para la gestión de inventario de una tienda de electrónica. Permite registrar y consultar productos, utilizando diferentes mecanismos de almacenamiento y registrando las operaciones importantes a través de un sistema de logging.

El objetivo principal no es construir una aplicación completa, sino servir como un ejemplo claro y robusto de cómo Spring resuelve problemas comunes del mundo real, como la ambigüedad en la inyección de dependencias y el manejo de componentes opcionales.

## Objetivos y Conceptos Clave Demostrados

El propósito de este proyecto es ilustrar los siguientes conceptos fundamentales:

1.  **Inyección de Dependencias por Constructor**: Se utiliza para inyectar todas las dependencias, promoviendo la inmutabilidad de los componentes y garantizando que los objetos estén en un estado válido desde su creación.
2.  **Resolución de Ambigüedad de Beans**: Se demuestra cómo gestionar situaciones donde existen múltiples implementaciones para una misma interfaz (`RepositorioProducto`):
    * **`@Primary`**: Marca una implementación (`RepositorioProductoJpa`) como la opción por defecto, simplificando la configuración en la mayoría de los casos.
    * **`@Qualifier`**: Se usa para seleccionar explícitamente una implementación específica (`@Qualifier("repositorioJpa")`), demostrando cómo anular la opción por defecto cuando sea necesario. `@Qualifier` siempre tiene mayor precedencia.
3.  **Manejo de Dependencias Opcionales con `Optional<T>`**: Se utiliza el enfoque moderno y seguro para inyectar dependencias opcionales (`RegistradorInventario`). En lugar de usar setters o campos nulos, se inyecta un `Optional<RegistradorInventario>`, haciendo el contrato del componente explícito y evitando `NullPointerException`.
4.  **Programación Orientada a Interfaces (Desacoplamiento)**: El núcleo del diseño. Los componentes dependen de abstracciones (`ServicioProducto`, `RepositorioProducto`), no de implementaciones concretas. Esto permite intercambiar las implementaciones (por ejemplo, cambiar de JPA a CSV) sin modificar la lógica de negocio.
5.  **Logging Profesional con SLF4J**: Se reemplaza `System.out.println` por SLF4J, el estándar de facto para el logging en el ecosistema de Java, lo que permite un registro más flexible y configurable.
6.  **`CommandLineRunner` para Lógica de Arranque**: La lógica de demostración se aísla en un componente `EjecutorInventario`, manteniendo la clase principal de la aplicación limpia y enfocada únicamente en el arranque.

-----

## Diagrama de Interrelación

Este diagrama ilustra cómo el Contenedor IoC de Spring descubre, crea y conecta los diferentes componentes (beans) de la aplicación.

```
+--------------------------+
|  GestionInventarioApp  | --(1. Inicia)--> [ Spring IoC Container ]
+--------------------------+                      (El "Director de Orquesta")

 2. FASE DE INICIALIZACIÓN

    |
    +--> Escanea y crea Beans:
    |    - RepositorioProductoJpa (@Primary, @Qualifier("repositorioJpa"))
    |    - RepositorioProductoCsv (@Qualifier("repositorioCsv"))
    |    - RegistradorInventarioConsola
    |    - ServicioProductoImpl
    |    - EjecutorInventario
    |
    +--> Inyecta Dependencias (Ensamblaje):
         |
         +--> [RepositorioProductoJpa] --(@Qualifier tiene precedencia)--> ServicioProductoImpl
         |
         +--> [Optional<Registrador...>] --------------------------------> ServicioProductoImpl
         |
         +--> [ServicioProductoImpl] -------------------------------------> EjecutorInventario

 3. FASE DE USO

    +--> Ejecuta CommandLineRunner:
           - EjecutorInventario.run() --> Llama a los métodos de ServicioProducto
```

-----

## Estructura del Proyecto

El proyecto sigue una arquitectura organizada por funcionalidad, una práctica recomendada para la escalabilidad y el mantenimiento.

```
gestion-inventario/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── inventario/
        │               ├── GestionInventarioApp.java
        │               ├── EjecutorInventario.java
        │               │
        │               ├── almacenamiento/
        │               │   ├── RepositorioProducto.java
        │               │   └── impl/
        │               │       ├── RepositorioProductoCsv.java
        │               │       └── RepositorioProductoJpa.java
        │               │
        │               ├── producto/
        │               │   ├── ServicioProducto.java
        │               │   └── impl/
        │               │       └── ServicioProductoImpl.java
        │               │
        │               └── registro/
        │                   ├── RegistradorInventario.java
        │                   └── impl/
        │                       └── RegistradorInventarioConsola.java
        └── resources/
            └── application.properties
```

-----

## Cómo Ejecutar el Proyecto

### Prerrequisitos

* JDK 17 o superior.
* Apache Maven 3.8 o superior.

### Pasos para la Ejecución

1.  Clona este repositorio en tu máquina local.
2.  Abre una terminal o línea de comandos en el directorio raíz del proyecto.
3.  Ejecuta el siguiente comando de Maven para compilar y arrancar la aplicación:
    ```bash
    mvn spring-boot:run
    ```
4.  Observa la salida en la consola para ver el resultado de las operaciones y los logs.

### Salida Esperada

La salida en la consola demostrará que se está utilizando el `RepositorioProductoJpa` (gracias a `@Qualifier`) y que el `RegistradorInventario` está funcionando correctamente.

```
--- REALIZANDO OPERACIONES DE INVENTARIO ---
[main] INFO com.example.inventario.registro.impl.RegistradorInventarioConsola - [REGISTRO DE INVENTARIO] Producto añadido: Laptop Dell XPS 15
PRODUCTO GUARDADO (JPA): 'Laptop Dell XPS 15'
[main] INFO com.example.inventario.registro.impl.RegistradorInventarioConsola - [REGISTRO DE INVENTARIO] Búsqueda de producto: ID-12345
PRODUCTO ENCONTRADO (JPA): 'ID-12345'
--- FIN DE OPERACIONES ---
```