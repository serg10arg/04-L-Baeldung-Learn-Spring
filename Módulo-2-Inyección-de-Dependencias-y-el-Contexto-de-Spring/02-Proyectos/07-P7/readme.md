***
# Gestión Programática del ApplicationContext en Spring

Este proyecto es una aplicación de demostración construida con Spring Framework que ilustra cómo crear, configurar y gestionar el ciclo de vida de un `ApplicationContext` de forma programática, en lugar de depender del arranque automático proporcionado por `SpringApplication.run()`.

El objetivo es ofrecer una visión clara de los mecanismos internos de Spring y cómo un desarrollador puede tomar control total sobre el contenedor de Inversión de Control (IoC).

-----

## Conceptos Fundamentales Demostrados

Esta aplicación sirve como un caso de estudio práctico para los siguientes conceptos de Spring:

* **Creación Programática del Contexto**: Instanciación manual de `AnnotationConfigApplicationContext`.
* **Escaneo de Componentes**: Configuración del contexto para que descubra beans anotados con `@Service`, `@Component`, etc., usando `context.scan()`.
* **Ciclo de Vida del Contexto**: La importancia de `context.refresh()` para procesar las definiciones de beans y `context.close()` para una finalización limpia.
* **Ciclo de Vida de los Beans**: Uso de las anotaciones `@PostConstruct` y `@PreDestroy` para ejecutar lógica en momentos clave de la vida de un bean.
* **Interfaces "Aware"**: Implementación de `ApplicationContextAware` para que un bean obtenga una referencia al contexto que lo contiene.
* **Recuperación de Beans**: Obtención de instancias de beans desde el contexto usando `context.getBean()`.

-----

## Arquitectura y Flujo de Ejecución

El flujo de la aplicación está orquestado enteramente dentro del método `main` de la clase `EcommerceApp.java`, siguiendo estos pasos lógicos:

1.  **Creación del Contexto**: Se crea una instancia vacía de `AnnotationConfigApplicationContext`. En este punto, el contexto existe pero no contiene ningún bean.
2.  **Configuración y Refresco**:
    * Se invoca `context.scan("...")` para indicarle a Spring en qué paquete debe buscar clases anotadas para registrar como beans (en este caso, `OrderServiceImpl`).
    * Se llama a `context.refresh()`. Este es un paso crítico que le ordena al contexto procesar todas las definiciones de beans que ha encontrado, instanciarlos, inyectar dependencias y ejecutar los callbacks `@PostConstruct`.
3.  **Uso de Beans**:
    * Una vez que el contexto está activo, se recupera el bean `OrderServiceImpl` usando `context.getBean()`.
    * Se interactúa con el servicio para crear y obtener pedidos, demostrando que el bean está completamente funcional.
4.  **Cierre del Contexto**:
    * Finalmente, se llama a `context.close()`. Esta acción inicia el proceso de apagado del contenedor, que a su vez invoca todos los métodos de destrucción de los beans (anotados con `@PreDestroy`).

-----

## Diagrama de Flujo del Ciclo de Vida del Contexto

```mermaid
graph TD
    subgraph "Fase de Arranque"
        A[Inicio: main()] --> B(1. new AnnotationConfigApplicationContext)
        B --> C(2. context.scan)
        C --> D(3. context.refresh)
    end

    subgraph "Fase de Ejecución"
        D -- Dispara --> E[Crea 'OrderServiceImpl']
        E -- Dispara --> F(Ejecuta @PostConstruct en OrderServiceImpl)
        F --> G(4. context.getBean)
        G --> H(5. Usar el OrderService)
    end

    subgraph "Fase de Cierre"
        H --> I(6. context.close)
        I -- Dispara --> J(Ejecuta @PreDestroy en OrderServiceImpl)
        J --> K[Fin de la Aplicación]
    end

    style D fill:#28a745,stroke:#fff,stroke-width:2px,color:#fff
    style I fill:#dc3545,stroke:#fff,stroke-width:2px,color:#fff
```

-----

## Estructura del Proyecto

* **`EcommerceApp.java`**: Clase principal y punto de entrada que orquesta el ciclo de vida del `ApplicationContext`.
* **`service/OrderServiceImpl.java`**: Un bean `@Service` que gestiona pedidos en memoria. Implementa la lógica de negocio y los callbacks de ciclo de vida (`@PostConstruct`, `@PreDestroy`) y `ApplicationContextAware`.
* **`interfaces/IOrderService.java`**: Define el contrato para el servicio de pedidos, promoviendo el principio de "programar contra una interfaz".
* **`model/Order.java`**: Un POJO (Plain Old Java Object) que representa un pedido, utilizando Lombok para reducir el código repetitivo.
* **`pom.xml`**: Archivo de configuración de Maven con las dependencias de Spring Boot y Lombok.

-----

## Cómo Ejecutar el Proyecto

### Prerrequisitos

* JDK 17 o superior.
* Apache Maven 3.8 o superior.

### Pasos

1.  Clona este repositorio en tu máquina local.
2.  Abre una terminal o línea de comandos en el directorio raíz del proyecto.
3.  Ejecuta el siguiente comando de Maven para compilar y arrancar la aplicación:
    ```bash
    mvn spring-boot:run
    ```
4.  Observa la salida en la consola para verificar el comportamiento del ciclo de vida.

-----

## Salida Esperada en Consola

La salida en la consola demostrará claramente la secuencia de eventos descrita anteriormente.

```java
// Salida simplificada para ilustrar los puntos clave

INFO --- [main] c.e.g.EcommerceApp: Intentando crear un nuevo ApplicationContext...
INFO --- [main] c.e.g.EcommerceApp: Contexto creado con ID 'org.springframework.context.annotation.AnnotationConfigApplicationContext@...'
...
INFO --- [main] c.e.g.EcommerceApp: Contexto configurado para escanear com.example.gestiondepedidos.service
...
INFO --- [main] c.e.g.s.OrderServiceImpl: OrderServiceImpl: Contexto Spring con ID '...' establecido.
INFO --- [main] c.e.g.s.OrderServiceImpl: OrderServiceImpl: ¡Bean inicializado! (@PostConstruct).
...
INFO --- [main] c.e.g.EcommerceApp: Intentando recuperar el bean OrderService...
INFO --- [main] c.e.g.EcommerceApp: Bean OrderService recuperado con exito: com.example.gestiondepedidos.service.OrderServiceImpl
...
INFO --- [main] c.e.g.s.OrderServiceImpl: Buscando pedido con ID: 1
INFO --- [main] c.e.g.EcommerceApp: Pedido recuperado por ID (1L): Order[id=1, customerName='Alice Smith', totalAmount=150.0, status='COMPLETED']
...
INFO --- [main] c.e.g.EcommerceApp: Cerrando el ApplicationContext...
INFO --- [main] c.e.g.s.OrderServiceImpl: OrderServiceImpl: ¡Bean destruido! (@PreDestroy).
INFO --- [main] c.e.g.EcommerceApp: Contexto activo después de cerrar: false
INFO --- [main] c.e.g.EcommerceApp: Aplicación finalizada. Deberías ver el mensaje @PreDestroy
```