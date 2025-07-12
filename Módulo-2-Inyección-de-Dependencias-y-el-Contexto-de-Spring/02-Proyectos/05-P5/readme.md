# Demo de Ámbitos de Beans en Spring Boot

Este proyecto es una aplicación de demostración construida con Spring Boot para ilustrar de manera clara y práctica la diferencia entre los ámbitos de bean **singleton** y **prototype**, dos conceptos fundamentales en el contenedor de Inversión de Control (IoC) de Spring.

---

## Objetivo del Proyecto

El objetivo es demostrar cómo Spring gestiona el ciclo de vida y la creación de instancias de un mismo componente (`RepositorioProyectoImpl`) cuando se define con diferentes ámbitos:

- **Singleton**: Una única instancia compartida para toda la aplicación. Ideal para componentes sin estado o para gestionar recursos compartidos como conexiones a bases de datos.
- **Prototype**: Una nueva instancia creada cada vez que el bean es solicitado o inyectado. Perfecto para componentes con estado que deben ser aislados entre diferentes tareas o hilos.

---

## ¿Cómo Funciona?

La arquitectura de la aplicación está diseñada para resaltar el comportamiento de los ámbitos de manera explícita:

1. **`ConfiguracionApp` (`@Configuration`)**: Esta clase actúa como el "plano de construcción". Define explícitamente dos beans a partir de la misma clase `RepositorioProyectoImpl`:
    - `repositorioCompartido`: Con el ámbito `singleton` (por defecto).
    - `repositorioTemporal`: Con el ámbito `prototype`.
2. **`RepositorioProyectoImpl`**: Cada vez que se crea una instancia de esta clase, se le asigna un ID único basado en `System.nanoTime()`. Esto nos permite verificar visualmente si dos variables de referencia apuntan al mismo objeto o a objetos diferentes.
3. **`ServicioProyectoImpl` (`@Service`)**: Este servicio es el consumidor. A través de la inyección por constructor, solicita a Spring cuatro dependencias:
    - Dos instancias del bean `repositorioCompartido`.
    - Dos instancias del bean repositorioTemporal.

      El uso de @Qualifier asegura que Spring sepa qué definición de bean usar para cada inyección.

4. **Verificación con `@PostConstruct`**: Inmediatamente después de que `ServicioProyectoImpl` es creado y sus dependencias son inyectadas, se ejecuta el método `verificarInstancias()`. Este método imprime los IDs de cada repositorio inyectado y compara las referencias de los objetos (`==`), demostrando de forma concluyente el comportamiento de cada ámbito.

---

## Salida Esperada

La salida en la consola demostrará claramente que las dos referencias al bean singleton apuntan al mismo objeto (mismo ID), mientras que las dos referencias al bean prototype apuntan a objetos completamente diferentes (IDs distintos).

Fragmento de código

```java
INFO: Definiendo bean 'repositorioCompartido' (Singleton)...
DEBUG: Nueva instancia de RepositorioProyectoImpl creada con ID: Repo-1234567890
INFO: Definiendo bean 'repositorioTemporal' (Prototype)...

DEBUG: Nueva instancia de RepositorioProyectoImpl creada con ID: Repo-2345678901
DEBUG: Nueva instancia de RepositorioProyectoImpl creada con ID: Repo-3456789012

--- VERIFICACIÓN DE ÁMBITOS DE BEAN (@PostConstruct) ---

--- Instancias Singleton ('repositorioCompartido') ---
  ID de repoSingleton1: Repo-1234567890
  ID de repoSingleton2: Repo-1234567890
  ¿Son el mismo objeto? -> true

--- Instancias Prototype ('repositorioTemporal') ---
  ID de repoPrototipo1: Repo-2345678901
  ID de repoPrototipo2: Repo-3456789012
  ¿Son el mismo objeto? -> false

--- FIN DE LA VERIFICACIÓN ---
```