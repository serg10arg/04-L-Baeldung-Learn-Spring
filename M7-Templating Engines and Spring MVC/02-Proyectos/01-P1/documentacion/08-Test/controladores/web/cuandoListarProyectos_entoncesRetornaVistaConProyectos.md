# **Análisis Profesional: Lógica Interna de un Método de Prueba**

## **1. Introducción**

Este documento proporciona un análisis técnico detallado de un método de prueba específico, `cuandoListarProyectos_entoncesRetornaVistaConProyectos`. El objetivo es desglosar su estructura, basada en el patrón **Given-When-Then**, para ilustrar cómo se construye una prueba de controlador robusta, legible y aislada en un entorno Spring Boot.

## **2. El "Dado" (Given): Preparando el Escenario**

Aquí estamos preparando el mundo en el que vivirá nuestra prueba.

```java
// Dado: Creamos los DTOs que el servicio DEBE devolver
ProyectoVerDTO dto1 = new ProyectoVerDTO(1L, "Proyecto Uno", LocalDate.now(), new HashSet<>());
ProyectoVerDTO dto2 = new ProyectoVerDTO(2L, "Proyecto Dos", LocalDate.now(), new HashSet<>());
List<ProyectoVerDTO> listaDeDTOs = Arrays.asList(dto1, dto2);

// El mock ahora devuelve el tipo correcto: List<ProyectoVerDTO>
given(servicioProyecto.obtenerTodosLosProyectos()).willReturn(listaDeDTOs);

```

- **La Preparación de Datos:** Primero, creamos dos objetos `ProyectoVerDTO`. Punto clave de la arquitectura: no creamos entidades `Proyecto`, sino DTOs. ¿Por qué? Porque estamos probando el Controlador, y el contrato del Servicio dice que le entregará al controlador una lista de `ProyectoVerDTO`. La prueba debe respetar y verificar ese contrato.
- **La Programación del Mock:** La línea `given(...)` es la más importante de esta sección. Estamos hablando con nuestro "actor doble", el `servicioProyecto` que es un mock (una simulación). Le damos una instrucción muy clara:

  > "Oye, servicioProyecto, en esta prueba, cuando el Controlador te llame y ejecute tu método obtenerTodosLosProyectos(), quiero que ignores tu lógica real (que no tienes) y simplemente devuelvas esta listaDeDTOs que acabo de crear. No preguntes, solo hazlo."
>

Con esto, hemos aislado completamente al controlador. No nos importa cómo el servicio obtiene los datos; solo nos importa cómo reacciona el controlador cuando el servicio le entrega un resultado conocido.

## **3. El "Cuando" (When): La Acción Principal**

Esta es la acción que desencadena todo.

```java
// Cuando y Entonces
mvc.perform(get("/proyectos")
        .contentType(MediaType.TEXT_HTML))
// ... Siguen las verificaciones

```

- **Simulando al Usuario:** Usamos `mvc.perform(...)` para simular a un usuario que abre su navegador y escribe `http://localhost:8080/proyectos`. Estamos ejecutando una petición `GET` a la ruta `/proyectos`.
- **Definiendo Expectativas:** Con `.contentType(MediaType.TEXT_HTML)`, le estamos diciendo al sistema que esperamos recibir una página web (HTML) como respuesta.

## **4. El "Entonces" (Then): La Verificación de Resultados**

Esta es la parte donde, como detectives, verificamos que todo sucedió como esperábamos. Leemos la cadena de `andExpect` como una lista de afirmaciones que deben ser todas verdaderas:

```java
.andExpect(status().isOk())
.andExpect(view().name("proyectos/listaProyectos"))
.andExpect(model().attributeExists("proyectos"))
.andExpect(model().attribute("proyectos", hasSize(2)))
.andExpect(content().string(containsString("Proyecto Uno")))
.andExpect(content().string(containsString("Proyecto Dos")));

```

1. **`status().isOk()`**: "Afirmo que la respuesta HTTP tuvo un código de estado 200 (OK). La petición fue exitosa."
2. **`view().name("proyectos/listaProyectos")`**: "Afirmo que el controlador, al terminar, decidió que la vista correcta a mostrar es la que se llama `proyectos/listaProyectos`." Esto verifica que la lógica de enrutamiento interno del controlador es correcta.
3. **`model().attributeExists("proyectos")`**: "Afirmo que el controlador colocó algo en el 'maletín' (`Model`) que se pasa a la vista, y que ese algo tiene la etiqueta 'proyectos'."
4. **`model().attribute("proyectos", hasSize(2))`**: "Afirmo que lo que el controlador puso en el modelo no es cualquier cosa, sino una colección que tiene exactamente 2 elementos, que coincide con lo que nuestro mock devolvió."
5. **`content().string(containsString(...))`**: "Finalmente, afirmo que si tomamos la página HTML final que se generó, el texto 'Proyecto Uno' y 'Proyecto Dos' aparecen en alguna parte." Esta es la verificación definitiva, ya que confirma que los datos no solo llegaron al modelo, sino que la vista (Thymeleaf) los usó correctamente para renderizar el resultado final.

## **5. Conclusión**

En resumen, este método de prueba cuenta una historia muy completa:

> "Dado un escenario donde el servicio tiene dos proyectos, cuando un usuario visita la página principal de proyectos, entonces espero recibir una respuesta exitosa, que se me muestre la vista de la lista, que el modelo contenga esos dos proyectos y que sus nombres aparezcan en la página final."
>

Es una prueba de alta calidad porque verifica cada paso de la responsabilidad del controlador para este flujo de trabajo específico.