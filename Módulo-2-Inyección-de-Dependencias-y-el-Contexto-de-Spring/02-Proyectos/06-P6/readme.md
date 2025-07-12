# Demo del Ciclo de Vida de Beans en Spring Boot

Este proyecto es una aplicación de demostración construida con Spring Boot para ilustrar de manera clara y práctica cómo extender y personalizar el ciclo de vida de los beans utilizando los "puntos de extensión" (hooks) del contenedor de Inversión de Control (IoC) de Spring.

---

## Objetivo del Proyecto

El objetivo es demostrar cómo un desarrollador puede intervenir en el proceso de creación de beans para:

1. Modificar la definición de un bean (`BeanDefinition`) antes de su creación, utilizando la interfaz `BeanFactoryPostProcessor`.
2. Ejecutar lógica de auditoría o modificación después de que un bean ha sido instanciado, pero antes y después de su inicialización, utilizando la interfaz `BeanPostProcessor`.
3. Controlar el orden de ejecución cuando existen múltiples post-procesadores, a través de la interfaz `Ordered`.
4. Asegurar la inicialización temprana de estos componentes de infraestructura registrándolos como métodos `static @Bean` en una clase de configuración.

---

## ¿Cómo Funciona?

La arquitectura de la aplicación está diseñada para resaltar el comportamiento de cada componente en el momento preciso del ciclo de vida.

1. **`ConfiguradorSkuProductoFactory` (`BeanFactoryPostProcessor`)**:
    - Actúa como un "configurador de recetas". Se ejecuta muy temprano, antes de que se cree cualquier instancia de bean.
    - Su única misión es encontrar la definición del bean `productoPrincipal` y añadirle una propiedad (`sku`) por defecto.
    - Tiene un `order` de 1, dándole la máxima prioridad.
2. **`AuditorProductoPostProcesador` (`BeanPostProcessor`)**:
    - Actúa como un "auditor". Se ejecuta para cada bean que se crea en la aplicación.
    - Filtra los beans para actuar solo sobre los que son de tipo `Producto`.
    - Registra un mensaje justo antes y justo después de que se ejecute la lógica de inicialización del `Producto` (su método `@PostConstruct`).
    - Tiene un `order` de 10.
3. **`LogDetalladoProductoPostProcesador` (`BeanPostProcessor`)**:
    - Simula otro sistema de auditoría o logging.
    - También actúa sobre los beans de tipo `Producto`.
    - Tiene un `order` de 20, lo que garantiza que se ejecutará después del `AuditorProductoPostProcesador` (ya que 10 < 20).
4. **`ConfiguracionEcommerce` (`@Configuration`)**:
    - Es el centro de configuración. Define los beans de la aplicación (`productoPrincipal` y `accesorioProducto`).
    - Registra los tres post-procesadores como métodos `static @Bean`. El modificador `static` es crucial, ya que le indica a Spring que debe crear estos beans de infraestructura primero, antes de procesar completamente la propia clase de configuración.

---

## Cómo Ejecutar el Proyecto

### Prerrequisitos

- JDK 17 o superior.
- Apache Maven 3.8 o superior.

### Pasos para la Ejecución

1. Clona este repositorio en tu máquina local.
2. Abre una terminal o línea de comandos en el directorio raíz del proyecto.
3. Ejecuta el siguiente comando de Maven para compilar y arrancar la aplicación:Bash

   # 

   `mvn spring-boot:run`

4. Observa la salida en la consola para verificar el comportamiento del ciclo de vida.

---

## Salida Esperada

La salida en la consola demostrará claramente la secuencia de eventos. Notarás que:

- El `ConfiguradorSkuProductoFactory` se ejecuta una sola vez, al principio de todo.
- Los `BeanPostProcessor` se ejecutan para cada `Producto`, respetando el orden definido.
- El SKU del `productoPrincipal` se establece correctamente, mientras que el de `accesorioProducto` permanece `null`.

Fragmento de código

```java
// Salida simplificada para mayor claridad

⚙️ Ejecutando ConfiguradorSkuProductoFactory (BeanFactoryPostProcessor). Modificando BeanDefinition...
✅ Propiedad 'sku' de 'productoPrincipal' modificada a 'DEFAULT-SKU-ECOMMERCE'.

Auditor General 🕵️‍♂️: Antes de inicializar el bean: 'productoPrincipal' (ID: PROD-001)
Log Detallado 📊: Iniciando procesamiento para bean: 'productoPrincipal'
➡️ Producto 'Laptop Pro' (ID: PROD-001) ha sido inicializado. SKU actual: DEFAULT-SKU-ECOMMERCE
Auditor General 🕵️‍♂️: Después de inicializar el bean: 'productoPrincipal' (ID: PROD-001)
Log Detallado 📊: Finalizado procesamiento para bean: 'productoPrincipal'

Auditor General 🕵️‍♂️: Antes de inicializar el bean: 'accesorioProducto' (ID: ACC-001)
Log Detallado 📊: Iniciando procesamiento para bean: 'accesorioProducto'
➡️ Producto 'Mouse Inalámbrico' (ID: ACC-001) ha sido inicializado. SKU actual: null
Auditor General 🕵️‍♂️: Después de inicializar el bean: 'accesorioProducto' (ID: ACC-001)
Log Detallado 📊: Finalizado procesamiento para bean: 'accesorioProducto'

--- La aplicación ha finalizado su inicialización. ---
```