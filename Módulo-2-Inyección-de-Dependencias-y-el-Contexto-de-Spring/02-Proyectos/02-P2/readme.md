
-----

# Sistema de Gestión de Pedidos - Demo de Spring Boot

Este proyecto es una aplicación de demostración construida con Spring Boot para ilustrar de manera práctica y clara los principios fundamentales del desarrollo de software moderno, como la Inversión de Control (IoC), la Inyección de Dependencias (DI) y el diseño de software desacoplado y testable.

## Descripción General

El sistema simula una funcionalidad básica de un backend para comercio electrónico: la gestión de productos. Permite consultar la lista de productos y añadir nuevos productos a un catálogo simulado en memoria.

El objetivo principal no es construir una aplicación de e-commerce completa, sino servir como un ejemplo educativo claro sobre cómo y por qué Spring Boot es tan potente para construir aplicaciones robustas y mantenibles.

## Objetivos y Conceptos Clave Demostrados

El propósito de este proyecto es ilustrar los siguientes conceptos fundamentales:

1.  **Inversión de Control (IoC) y el Contenedor de Spring**:

    * En lugar de que nuestras clases creen sus propias dependencias (p. ej., `new RepositorioProductoImpl()`), le cedemos ese control a Spring. El Contenedor IoC de Spring se encarga de instanciar, configurar y gestionar el ciclo de vida de nuestros objetos (los Beans).

2.  **Inyección de Dependencias (DI) por Constructor**:

    * Se demuestra la mejor práctica para la inyección de dependencias. La clase `ServicioProductoImpl` no crea su `RepositorioProducto`; en su lugar, lo solicita en su constructor. Spring se encarga de "inyectar" la instancia correcta cuando crea el servicio.

3.  **Beans y Escaneo de Componentes**:

    * Se utilizan las anotaciones de estereotipo (`@Repository`, `@Service`, `@Component`) para marcar nuestras clases como candidatas a ser gestionadas por Spring. La anotación `@SpringBootApplication` activa el escaneo de componentes, que busca y registra automáticamente estos beans.

4.  **Diseño Desacoplado (Programación orientada a interfaces)**:

    * El `ServicioProductoImpl` depende de la interfaz `RepositorioProducto`, no de su implementación concreta. Esto significa que podríamos cambiar la implementación para usar una base de datos real (como PostgreSQL o MongoDB) sin tener que modificar una sola línea de código en el servicio.

5.  **Lógica de Arranque con `CommandLineRunner`**:

    * Se utiliza la interfaz `CommandLineRunner` en la clase `EjecutorAplicacion` para ejecutar código de demostración una vez que la aplicación ha arrancado y todas las dependencias han sido inyectadas.

## Diagrama de Interrelación

Este diagrama ilustra cómo el Contenedor IoC de Spring descubre, crea y conecta los diferentes componentes (beans) de la aplicación.

```
+------------------------+
|   AplicacionPedidos    | --(inicia)--> [ Spring IoC Container ]
| (@SpringBootApplication) |               (La "Caja de Herramientas" de Spring)
+------------------------+
         |
         | 1. Escanea y crea los Beans
         |
+-----------------V------------------+
|                                    |
+---------------------------+       +--------------------------+
| RepositorioProductoImpl   |       |   ServicioProductoImpl   |
|      (@Repository)        |       |        (@Service)        |
+---------------------------+       +--------------------------+
         ^                                     ^
         | 2. Inyecta Dependencias             |
+-------------------------------------+
         |
+---------------------------+                     |
|    EjecutorAplicacion     | --------------------+
|       (@Component)        |
+---------------------------+
         |
         | 3. Ejecuta el método run()
         V
+---------------------------+
| Lógica de la Aplicación   |
| (Imprime en consola)      |
+---------------------------+

```

### Explicación del Diagrama:

1.  **Inicio y Escaneo**: `AplicacionPedidos` arranca el Contenedor IoC de Spring, que escanea los paquetes y crea instancias (beans) de `RepositorioProductoImpl`, `ServicioProductoImpl` y `EjecutorAplicacion`.
2.  **Inyección de Dependencias**: Spring ve que `ServicioProductoImpl` necesita un `RepositorioProducto`, así que le inyecta la instancia que acaba de crear. Luego, ve que `EjecutorAplicacion` necesita un `ServicioProducto` y le inyecta la instancia del servicio ya configurada.
3.  **Ejecución**: Una vez que todo está conectado, Spring ejecuta el método `run()` de `EjecutorAplicacion`, que utiliza el servicio inyectado para realizar las operaciones.

## Estructura del Proyecto

El proyecto sigue una arquitectura por capas, donde las clases se organizan según su responsabilidad técnica.

```
.
└── src
    └── main
        └── java
            └── com
                └── example
                    └── pedidosonline
                        ├── AplicacionPedidos.java
                        ├── EjecutorAplicacion.java
                        ├── interfaces
                        │   ├── RepositorioProducto.java
                        │   └── ServicioProducto.java
                        ├── modelo
                        │   └── Producto.java
                        └── servicio
                            └── ServicioProductoImpl.java
```

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

4.  Observa la salida en la consola.

### Salida Esperada

Verás una salida que demuestra la ejecución de la lógica de arranque, listando los productos iniciales, añadiendo uno nuevo y volviendo a listar el catálogo actualizado.

```
--- EJECUTANDO LÓGICA DE ARRANQUE ---
1. Listando productos iniciales:
- Laptop Gaming ($1200.0)
- Teclado Mecánico ($85.5)
- Ratón Inalámbrico ($45.0)

2. Añadiendo un nuevo producto...

3. Listando productos actualizados:
- Laptop Gaming ($1200.0)
- Monitor 4k ($350.0)
- Teclado Mecánico ($85.5)
- Ratón Inalámbrico ($45.0)

--- LÓGICA DE ARRANQUE FINALIZADA ---
```