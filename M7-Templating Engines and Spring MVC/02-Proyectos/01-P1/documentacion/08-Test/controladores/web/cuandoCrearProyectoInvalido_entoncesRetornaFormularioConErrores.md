# **Análisis Profesional: Lógica Interna de un Método de Prueba (Camino de Error)**

## **1. Introducción**

Este documento proporciona un análisis técnico detallado de un método de prueba para la funcionalidad `crearProyecto` cuando se enfrenta a datos inválidos (el "camino triste"). El objetivo es desglosar su estructura, basada en el patrón **Given-When-Then**, para ilustrar cómo se valida la robustez de un controlador y su capacidad para guiar al usuario en la corrección de errores.

## **2. El "Dado" (Given): Un Escenario de Error (Implícito)**

En este caso, la fase "Given" es la más simple de todas: no hacemos nada.

> Análisis Senior: Esta falta de preparación es intencional y crucial. No necesitamos programar nuestro servicioProyecto (el mock) porque, si el controlador está bien escrito, la validación (@Valid) debería fallar antes de que se intente llamar a la capa de servicio. Si el controlador llamara al servicio con datos inválidos, sería un fallo de diseño. Por lo tanto, al no configurar el mock, también estamos probando implícitamente que el servicio nunca es invocado.
>

## **3. El "Cuando" (When): La Acción del Usuario (con Error)**

Esta es la acción que simula el error del usuario.

```java
// Cuando y Entonces
mvc.perform(post("/proyectos/nuevo")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("nombre", ""))

```

- **Simulando al Usuario:** Con `mvc.perform(...)`, estamos imitando a un usuario que, o bien dejó el campo de nombre en blanco, o lo borró, y luego presionó el botón "Guardar".
- `post("/proyectos/nuevo")`: Simulamos el envío del formulario.
- `.param("nombre", "")`: Esta es la clave. Estamos enviando el parámetro `nombre` con una cadena de texto vacía, lo que debería violar nuestra regla de validación `@NotBlank` en el `ProyectoCrearDTO`.

## **4. El "Entonces" (Then): La Cadena de Verificaciones (La Guía al Usuario)**

Aquí es donde verificamos que el controlador se comporta de manera inteligente y útil ante el error.

```java
.andExpect(status().isOk())
.andExpect(view().name("proyectos/crearProyecto"))
.andExpect(model().attributeHasErrors("proyectoCrearDTO"))
.andExpect(model().attributeHasFieldErrors("proyectoCrearDTO", "nombre"));

```

1. **`.andExpect(status().isOk())`**: "Afirmo que el servidor respondió con un código 200 (OK)."
    - **Análisis Senior:** Esto es muy importante. No esperamos una redirección (3xx) ni un error de cliente (4xx). Esperamos un 200 OK porque la acción correcta es volver a mostrar la misma página del formulario, pero esta vez con mensajes de error.
2. **`.andExpect(view().name("proyectos/crearProyecto"))`**: "Afirmo que el controlador decidió correctamente que la vista a mostrar es, de nuevo, la del formulario de creación." Esto confirma que no hemos perdido al usuario; lo mantenemos en la misma página para que pueda corregir su entrada.
3. **`.andExpect(model().attributeHasErrors("proyectoCrearDTO"))`**: "Afirmo que el 'paquete de datos' (`Model`) que se envía a la vista ahora contiene información de que hubo errores de validación en el objeto `proyectoCrearDTO`." Esto es lo que activa la lógica de `th:if="${#fields.hasErrors(...)}"` en Thymeleaf.
4. **`.andExpect(model().attributeHasFieldErrors("proyectoCrearDTO", "nombre"))`**: "Y para ser aún más específico, afirmo que el error de validación está asociado precisamente con el campo `nombre`."
    - **Análisis Senior:** Esta es la verificación más precisa. Confirma que el sistema no solo sabe que hubo un error, sino que sabe exactamente qué campo fue el culpable. Esto permite a la vista mostrar el mensaje de error justo al lado del campo de texto incorrecto, proporcionando una experiencia de usuario excelente.

## **5. Conclusión**

En lenguaje natural, la historia que cuenta este test es:

> "Cuando un usuario intenta crear un proyecto pero deja el nombre en blanco, quiero asegurarme de que el sistema no se rompe, sino que amablemente le vuelve a mostrar el formulario, indicando que hubo un error específicamente en el campo del nombre, para que pueda corregirlo fácilmente."
>

Es una prueba fundamental porque valida la robustez de la aplicación y su capacidad para manejar entradas incorrectas de manera predecible y útil, una característica clave del software de calidad profesional.