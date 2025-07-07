
-----

# Sistema de Tienda Online - Demo del Ciclo de Vida de Beans en Spring

Este proyecto es una aplicación de demostración construida con Spring Boot para ilustrar de manera práctica y clara cómo gestionar el ciclo de vida de los beans. Se enfoca en cómo inicializar recursos de manera segura y liberarlos correctamente al finalizar la aplicación.

## Descripción General

El sistema simula el backend de una tienda online con tres componentes principales:

* **ServicioPedidos**: Procesa los pedidos de los clientes.
* **GestorInventario**: Gestiona el stock de productos.
* **ServicioNotificacion**: Envía notificaciones (confirmaciones de pedido, alertas de stock).

El objetivo principal es demostrar cómo los *hooks* del ciclo de vida (`@PostConstruct` y `@PreDestroy`) permiten a los componentes gestionar sus propios recursos (como conexiones o datos en memoria) de forma robusta y desacoplada.

## Objetivos y Conceptos Clave Demostrados

El propósito de este proyecto es ilustrar los siguientes conceptos fundamentales:

1.  **Ciclo de Vida del Bean**: El concepto central. Se demuestra el flujo completo:

    * **Instanciación**: Creación de la instancia del objeto.
    * **Inyección de Dependencias**: Spring "conecta" los beans entre sí.
    * **Inicialización (`@PostConstruct`)**: Un método se ejecuta justo después de que el bean está completamente construido y sus dependencias inyectadas. Ideal para cargar configuraciones, establecer conexiones o preparar datos.
    * **En Uso**: El bean está activo y ejecutando la lógica de negocio.
    * **Destrucción (`@PreDestroy`)**: Un método se ejecuta justo antes de que el bean sea eliminado del contexto. Ideal para cerrar conexiones, guardar estados o liberar recursos.

2.  **Anotaciones de Ciclo de Vida (`@PostConstruct` y `@PreDestroy`)**: Se utiliza el enfoque moderno y estándar para definir la lógica de inicialización y limpieza directamente dentro de cada componente, haciéndolos más autocontenidos.

3.  **Configuración Explícita con `@Bean`**: Se muestra cómo definir beans y sus dependencias en una clase central `@Configuration`, lo que proporciona un control total sobre su creación e interconexión.

4.  **Inyección de Dependencias por Constructor**: Se utiliza la mejor práctica para asegurar que los componentes reciban sus dependencias obligatorias de forma explícita y segura.

5.  **`CommandLineRunner`**: Se usa para ejecutar la lógica de la aplicación de forma limpia once que el contexto de Spring está completamente inicializado.

## Diagrama de Interrelación y Ciclo de Vida

Este diagrama ilustra cómo el Contenedor IoC de Spring gestiona el ciclo de vida completo de los beans.

```
+-------------------+
|  TiendaOnlineApp  | --(1. Inicia)--> [ Spring IoC Container ]
+-------------------+                      (El "Director de Orquesta")

FASE DE INICIALIZACIÓN
|
+--> Crea Beans: [GestorInventario], [ServicioNotificacion], [ServicioPedidos], [EjecutorTienda]
|
+--> Ejecuta Hooks @PostConstruct:
|      - GestorInventario.cargarInventarioInicial()
|      - ServicioNotificacion.conectar()
|
+--> Inyecta Dependencias:
       (GestorInventario, ServicioNotificacion) -> ServicioPedidos -> EjecutorTienda

FASE DE USO
|
+--> Ejecuta CommandLineRunner:
       EjecutorTienda.run() --> Llama a ServicioPedidos.realizarPedido()

FASE DE DESTRUCCIÓN (Iniciada por context.close())
|
+--> Ejecuta Hooks @PreDestroy:
|      - GestorInventario.guardarInventarioRestante()
|      - ServicioNotificacion.desconectar()
|
+--> Elimina los Beans del Contexto
```

## Estructura del Proyecto

El proyecto sigue una arquitectura organizada por funcionalidad ("feature"), una práctica recomendada para la escalabilidad y el mantenimiento.

```
tienda-online/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── tiendaonline/
        │               ├── TiendaOnlineApp.java
        │               ├── EjecutorTienda.java
        │               ├── config/
        │               │   └── ConfiguracionApp.java
        │               ├── inventario/
        │               │   └── GestorInventario.java
        │               ├── notificacion/
        │               │   └── ServicioNotificacion.java
        │               └── pedido/
        │                   └── ServicioPedidos.java
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
4.  Observa la salida en la consola para ver el ciclo de vida completo en acción.

### Salida Esperada

La salida en la consola demostrará claramente el orden de las fases de inicialización, uso y destrucción.

```
--- Iniciando la aplicación de la tienda online... ---
  [GestorInventario]: Constructor invocado.
  [ServicioNotificacion]: Constructor invocado.
  [GestorInventario]: Hook @PostConstruct - Cargando inventario inicial...
  [GestorInventario]: Inventario inicial cargado: {Laptop Gamer=5, Teclado Mecánico=15, Monitor Curvo=0}
  [ServicioNotificacion]: Hook @PostConstruct - Estableciendo conexión con sistema de mensajería...
  [ServicioNotificacion]: Conexión establecida.
  [ServicioPedidos]: Constructor invocado. Listo para procesar pedidos.

--- FASE DE USO: Simulando operaciones de la tienda... ---
  [ServicioPedidos]: Intentando procesar pedido de 1 x Laptop Gamer...
  [GestorInventario]: Stock de 'Laptop Gamer' actualizado a 4
  [ServicioNotificacion]: Enviando confirmación de pedido: Laptop Gamer x1
  [ServicioPedidos]: Pedido de 'Laptop Gamer' procesado exitosamente.
  [ServicioPedidos]: Intentando procesar pedido de 2 x Teclado Mecánico...
  [GestorInventario]: Stock de 'Teclado Mecánico' actualizado a 13
  [ServicioNotificacion]: Enviando confirmación de pedido: Teclado Mecánico x2
  [ServicioPedidos]: Pedido de 'Teclado Mecánico' procesado exitosamente.
  [ServicioPedidos]: Intentando procesar pedido de 1 x Monitor Curvo...
  [ServicioPedidos]: ¡ERROR! No hay suficiente stock para 'Monitor Curvo'. Pedido fallido.
  [ServicioNotificacion]: Enviando alerta de stock bajo para: Monitor Curvo
--- Fin de la simulación de uso. ---

  [GestorInventario]: Hook @PreDestroy - Guardando inventario restante...
  [GestorInventario]: Inventario final guardado: {Laptop Gamer=4, Teclado Mecánico=13, Monitor Curvo=0}
  [ServicioNotificacion]: Hook @PreDestroy - Cerrando conexión con sistema de mensajería...
  [ServicioNotificacion]: Conexión cerrada.
--- Aplicación de la tienda online finalizada. ---
```