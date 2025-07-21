# **Análisis Profesional: Lógica Interna de un Método de Prueba**

## **1. Introducción**

Este documento proporciona un análisis técnico detallado de un método de prueba para la funcionalidad `mostrarFormularioCreacion`. El objetivo es desglosar su estructura, basada en el patrón **Given-When-Then**, para ilustrar cómo se construye una prueba de controlador robusta, legible y aislada en un entorno Spring Boot.

## **2. El "Dado" (Given): Un Escenario Limpio (Implícito)**

En este caso, la fase "Given" es implícita y muy simple: no necesitamos preparar ningún dato. No hay que programar nuestro `servicioProyecto` (el mock) porque, si el controlador está bien diseñado, nunca debería llamar al servicio para mostrar un formulario vacío. La prueba se ejecuta en un entorno limpio y predecible, y eso es todo lo que necesitamos.

## **3. El "Cuando" (When): La Acción del Usuario**

Esta es la acción central de la prueba.

```java
// Cuando...
mvc.perform(get("/proyectos/nuevo"))

```

- **Simulando al Usuario:** Con `mvc.perform(...)`, estamos imitando a un usuario que hace clic en un botón de "Crear Nuevo Proyecto" o que escribe `http://localhost:8080/proyectos/nuevo` en su navegador. Estamos simulando una petición `GET` a esa URL específica.

## **4. El "Entonces" (Then): La Cadena de Verificaciones**

Aquí es donde comprobamos, con precisión quirúrgica, que el controlador hizo exactamente su trabajo. Leemos la cadena de `andExpect` como una lista de afirmaciones que deben ser todas verdaderas.

```java
.andExpect(status().isOk())
.andExpect(view().name("proyectos/crearProyecto"))
.andExpect(model().attributeExists("proyectoCrearDTO"))
.andExpect(model().attribute("proyectoCrearDTO", hasProperty("nombre", nullValue())));

```

1. **`.andExpect(status().isOk())`**: "Afirmo que la petición fue exitosa. El servidor respondió con un código HTTP 200 (OK), lo que significa que encontró la página y no hubo errores."
2. **`.andExpect(view().name("proyectos/crearProyecto"))`**: "Afirmo que el controlador decidió correctamente que la plantilla HTML que se debe mostrar es la que se llama `crearProyecto.html` y que está en la carpeta `proyectos`." Esto verifica que la lógica de enrutamiento del controlador es correcta.
3. **`.andExpect(model().attributeExists("proyectoCrearDTO"))`**: "Afirmo que el controlador preparó un 'paquete de datos' (`Model`) para la vista, y que dentro de ese paquete hay un objeto con la etiqueta `proyectoCrearDTO`."
    - **¿Por qué es esto crucial?** Porque el formulario de Thymeleaf (`crearProyecto.html`) usa `th:object="${proyectoCrearDTO}"` para enlazar sus campos. Sin este objeto en el modelo, el formulario no funcionaría.
4. **`.andExpect(model().attribute("proyectoCrearDTO", hasProperty("nombre", nullValue())))`**: "Y para ser aún más preciso, afirmo que el objeto `proyectoCrearDTO` que se envió a la vista está vacío. Específicamente, su propiedad `nombre` es nula."
    - **Análisis Senior:** Esta es una verificación excelente. No solo confirma que el objeto existe, sino que confirma que es un "lienzo en blanco", un objeto nuevo sin datos preexistentes. Esto garantiza que el usuario siempre verá un formulario limpio, sin datos residuales de operaciones anteriores.

## **5. Conclusión**

En lenguaje natural, la historia que cuenta este test es:

> "Cuando un usuario solicita la página para crear un nuevo proyecto, quiero asegurarme de que el sistema responde correctamente, le muestra la vista del formulario de creación, y le proporciona un objeto de datos vacío y limpio para que el formulario pueda funcionar correctamente."
>

Es una prueba perfecta porque es **enfocada, rápida y precisa**. Verifica una única pieza de funcionalidad del controlador sin depender de ninguna otra parte del sistema, lo que la hace extremadamente fiable y fácil de mantener.