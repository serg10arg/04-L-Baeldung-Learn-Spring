# **Análisis Profesional: Lógica Interna de un Método de Prueba (Camino Triste)**

## **1. Introducción**

Este documento proporciona un análisis técnico detallado de un método de prueba para la funcionalidad `verDetallesProyecto` cuando el recurso solicitado **no existe** (el "camino triste"). El objetivo es desglosar su estructura, basada en el patrón **Given-When-Then**, para ilustrar cómo se valida el manejo de errores elegante y centrado en el usuario en un controlador Spring MVC.

## **2. El "Dado" (Given): Preparando el Escenario de la Ausencia**

Aquí estamos preparando el escenario para simular que el proyecto no existe.

```java
// Dado
given(servicioProyecto.obtenerProyectoPorId(99L)).willReturn(Optional.empty());

```

- **La Programación del Mock:** La línea `given(...)` es donde le damos las instrucciones a nuestro "actor doble", el `servicioProyecto` (la simulación). Le decimos:

  > "Oye, servicioProyecto, para esta prueba, cuando el Controlador te llame pidiendo el proyecto con el ID 99, quiero que le digas que no lo encontraste. No devuelvas null, que es peligroso. Devuelve una caja vacía (Optional.empty())."
>

> Análisis Senior: Usar Optional.empty() es una práctica moderna y segura. Obliga al código que lo consume (el controlador) a manejar explícitamente el caso de "no encontrado", eliminando por completo el riesgo de un NullPointerException, que es uno de los errores más comunes en Java.
>

## **3. El "Cuando" (When): La Acción del Usuario**

Esta es la acción que pone en marcha la prueba.

```java
// Cuando y Entonces
mvc.perform(get("/proyectos/99"))

```

- **Simulando al Usuario:** Con `mvc.perform(...)`, estamos imitando a un usuario que intenta acceder a una URL de un proyecto que no existe, por ejemplo, `http://localhost:8080/proyectos/99`.

## **4. El "Entonces" (Then): La Cadena de Verificaciones (La Gestión Elegante del Error)**

Aquí es donde verificamos que el controlador, en lugar de romperse, gestiona el error de una manera profesional y amigable para el usuario.

```java
.andExpect(status().is3xxRedirection())
.andExpect(redirectedUrl("/proyectos"))
.andExpect(flash().attributeExists("mensajeError"));

```

1. **`.andExpect(status().is3xxRedirection())`**: "Afirmo que la respuesta del servidor no fue una página de error (como un 404 Not Found), sino una orden de redirección (un código de estado 3xx)."
    - **Análisis Senior:** Esta es una decisión de diseño clave para la experiencia de usuario. En lugar de dejar al usuario en una página sin salida, lo vamos a guiar de vuelta a un lugar seguro.
2. **`.andExpect(redirectedUrl("/proyectos"))`**: "Y para ser más específico, afirmo que se le dijo al navegador que redirigiera al usuario de vuelta a la página principal, a la lista de proyectos (`/proyectos`)." Esto confirma que lo llevamos a un lugar conocido y funcional.
3. **`.andExpect(flash().attributeExists("mensajeError"))`**: "Finalmente, afirmo que el controlador preparó un mensaje de error para la siguiente página."
    - **Análisis Senior:** Esta es la guinda del pastel. Un "atributo flash" es un mensaje que sobrevive a una única redirección. Esto prueba que el controlador no solo redirige, sino que también le da al usuario una explicación de lo que pasó, mostrando un mensaje como "¡Proyecto no encontrado!" en la página de la lista.

## **5. Conclusión**

En lenguaje natural, la historia que cuenta este test es:

> "Dado un escenario donde un proyecto con un ID específico no existe, cuando un usuario intenta acceder a la página de detalles de ese proyecto, quiero asegurarme de que el sistema no muestra un error feo, sino que amablemente lo redirige de vuelta a la lista principal de proyectos y le muestra un mensaje claro explicando por qué no pudo ver los detalles."
>

Es una prueba fundamental porque valida que la aplicación es **robusta, a prueba de errores y que prioriza una experiencia de usuario fluida y sin callejones sin salida.**