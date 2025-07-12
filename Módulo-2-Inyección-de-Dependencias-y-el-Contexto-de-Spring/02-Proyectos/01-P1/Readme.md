
-----

# Sistema de Carrito de Compras - Demo de Spring Boot

Este proyecto es una aplicación de demostración construida con Spring Boot para ilustrar de manera práctica y clara los principios fundamentales del desarrollo de software moderno, como la Inversión de Control (IoC), la Inyección de Dependencias (DI) y el diseño de software desacoplado y testable.

## Descripción General

El sistema simula la funcionalidad de un carrito de compras para un e-commerce. Permite a un "usuario" (simulado a través de un runner) añadir productos a un carrito y calcular el coste total.

El objetivo principal no es construir una aplicación de e-commerce completa, sino servir como un ejemplo educativo claro sobre cómo y por qué Spring Boot es tan potente para construir aplicaciones robustas y mantenibles.

## Objetivos y Conceptos Clave Demostrados

El propósito de este proyecto es ilustrar los siguientes conceptos fundamentales:

1.  **Inversión de Control (IoC) y el Contenedor de Spring**:

    * En lugar de que nuestras clases creen sus propias dependencias (p. ej., `new InMemoryProductRepository()`), le cedemos ese control a Spring. El Contenedor IoC de Spring se encarga de instanciar, configurar y gestionar el ciclo de vida de nuestros objetos (los Beans).

2.  **Inyección de Dependencias (DI) por Constructor**:

    * Se demuestra la mejor práctica para la inyección de dependencias. La clase `ShoppingCartServiceImpl` no crea su `ProductRepository`; en su lugar, lo solicita en su constructor. Spring se encarga de "inyectar" la instancia correcta cuando crea el servicio. Esto hace que las dependencias sean explícitas y obligatorias.

3.  **Beans y Escaneo de Componentes**:

    * Se utilizan las anotaciones de estereotipo (`@Repository`, `@Service`, `@Component`) para marcar nuestras clases como candidatas a ser gestionadas por Spring. La anotación `@SpringBootApplication` activa el escaneo de componentes, que busca y registra automáticamente estos beans.

4.  **Diseño Desacoplado (Programación orientada a interfaces)**:

    * El `ShoppingCartServiceImpl` depende de la interfaz `ProductRepository`, no de su implementación concreta (`InMemoryProductRepository`). Esto significa que podríamos cambiar la implementación para usar una base de datos real (como PostgreSQL o MongoDB) sin tener que modificar una sola línea de código en el servicio del carrito.

5.  **Servicios sin Estado (Stateless Services)**:

    * El `ShoppingCartServiceImpl` está diseñado para no tener estado propio. Opera sobre un objeto `ShoppingCart` que se le pasa como parámetro. Esta es una práctica crucial en aplicaciones web para garantizar que el servicio pueda ser utilizado por múltiples usuarios simultáneamente sin mezclar sus datos.

## ¿Cómo Funciona el Proyecto?

El flujo de la aplicación está diseñado para mostrar cómo Spring conecta todo de manera transparente:

1.  **Arranque**: `EcommerceApplication` inicia el proceso. La anotación `@SpringBootApplication` le dice a Spring que comience a trabajar.
2.  **Escaneo y Creación de Beans**: Spring escanea los paquetes en busca de clases anotadas. Encuentra `InMemoryProductRepository` (`@Repository`), `ShoppingCartServiceImpl` (`@Service`) y `AppRunner` (`@Component`).
3.  **Inyección de Dependencias**:
    * Al crear el bean `ShoppingCartServiceImpl`, Spring ve que su constructor necesita un `ProductRepository`.
    * Busca en su contenedor un bean que cumpla con ese contrato y encuentra la instancia de `InMemoryProductRepository`.
    * "Inyecta" esa instancia en el constructor del servicio.
4.  **Ejecución de la Lógica**:
    * Una vez que todos los beans están creados y conectados, Spring busca componentes que implementen `CommandLineRunner`.
    * Encuentra `AppRunner` y ejecuta su método `run()`.
    * El `AppRunner` simula las acciones de un usuario: crea un carrito, añade productos usando el `cartService` y muestra el total.

## Estructura del Proyecto

El proyecto sigue una arquitectura por capas, donde las clases se organizan según su responsabilidad técnica.

```
ecommerce-project/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── ecommerceproject/
        │               ├── EcommerceApplication.java      <- Punto de entrada
        │               ├── AppRunner.java                 <- Lógica de demostración
        │               │
        │               ├── interfaz/                      <- Define los "contratos"
        │               │   ├── ProductRepository.java
        │               │   └── ShoppingCartService.java
        │               │
        │               ├── modelo/                        <- Clases de datos (POJOs)
        │               │   ├── Product.java
        │               │   ├── CartItem.java
        │               │   └── ShoppingCart.java
        │               │
        │               ├── repositorio/                   <- Implementaciones de acceso a datos
        │               │   └── InMemoryProductRepository.java
        │               │
        │               └── servicio/                      <- Implementaciones de lógica de negocio
        │                   └── ShoppingCartServiceImpl.java
        │
        └── resources/
            └── application.properties
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

Verás una salida que demuestra la simulación de añadir productos al carrito y el cálculo final del total.

```
--- Sistema de Carrito de Compras (Demo) ---
Añadido al carrito: Laptop (Cantidad: 1)
Añadido al carrito: Mouse (Cantidad: 2)
Añadido al carrito: Laptop (Cantidad: 1)
Producto con ID P999 no encontrado.

--- Resumen del Carrito ---
- Laptop (Cantidad: 2) - Subtotal: $2400.00
- Mouse (Cantidad: 2) - Subtotal: $50.00

TOTAL DEL CARRITO: $2450.00
---------------------------
```