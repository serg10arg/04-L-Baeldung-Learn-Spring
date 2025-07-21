# **Análisis Profesional: Pruebas de la Capa Web con `@WebMvcTest`**

## **1. Introducción: El Propósito y la Tecnología**

Este documento proporciona un análisis técnico detallado de la clase de prueba `ControladorProyectoWebTest.java`. El objetivo no es probar si la base de datos funciona o si la lógica de negocio del servicio es correcta. El objetivo es responder a estas preguntas clave sobre la capa web:

- ¿Responde el controlador a las URLs correctas (`/proyectos`, `/proyectos/nuevo`, etc.)?
- ¿Llama a los métodos correctos del servicio?
- ¿Maneja correctamente los datos de entrada (formularios, parámetros de URL)?
- ¿Coloca los datos correctos en el `Model` para que la vista los pueda usar?
- ¿Devuelve el nombre de la vista correcta o redirige a la URL adecuada?

Para lograr esto, se utilizan un conjunto de herramientas y anotaciones específicas que garantizan pruebas **rápidas, aisladas y fiables**.

## **2. El Entorno de Pruebas: Aislamiento y Velocidad**

La configuración de la clase de prueba es fundamental para su eficacia.

```
@WebMvcTest(ControladorProyectoWeb.class)
@Import(MapeadorDeProyectos.class)
@ActiveProfiles("test")
public class ControladorProyectoWebTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ServicioProyecto servicioProyecto;

    // ... métodos de prueba
}

```

### **Anotaciones Clave:**

- **`@WebMvcTest(ControladorProyectoWeb.class)`**: Esta es la anotación principal. Le dice a Spring Boot que configure un entorno de prueba que cargue **únicamente** los componentes de la capa web relacionados con `ControladorProyectoWeb`. No carga la capa de servicio completa ni la capa de base de datos. Esto hace que las pruebas sean extremadamente rápidas.
- **`@Import(MapeadorDeProyectos.class)`**: Como `@WebMvcTest` es tan específico, no carga todas las configuraciones (`@Configuration`). Necesitamos el bean `ModelMapper` en nuestra prueba, por lo que lo importamos explícitamente.
- **`@ActiveProfiles("test")`**: Una práctica profesional crucial. Fuerza a que esta prueba se ejecute con el perfil "test". Esto garantiza que cualquier configuración de desarrollo (como el `CommandLineRunner` anotado con `@Profile("dev")`) sea ignorada, asegurando un entorno de prueba limpio y predecible.
- **`@MockitoBean`**: Esta es la pieza clave del aislamiento. Le dice a Spring: "Encuentra el bean `ServicioProyecto` que el controlador necesita y **reemplázalo por una simulación (un 'mock')**". Esto nos da control total sobre el servicio. En nuestras pruebas, nosotros le diremos al mock qué debe devolver.

### **Herramientas Principales:**

- **`MockMvc mvc`**: Esta es nuestra herramienta principal. Nos permite simular peticiones HTTP (GET, POST, etc.) a nuestro controlador sin necesidad de levantar un servidor web real. Es el "cliente" en nuestras pruebas.
- **`ServicioProyecto servicioProyecto`**: Este es el mock que reemplaza al servicio real. En cada prueba, lo programaremos para que se comporte como queramos.

## **3. Análisis Detallado de los Casos de Prueba**

Cada método de prueba sigue el patrón clásico y altamente legible **Given-When-Then** (Dado-Cuando-Entonces).

### **`cuandoListarProyectos_entoncesRetornaVistaConProyectos`**

- **Propósito:** Verificar que la página principal de proyectos se muestra correctamente con datos.
- **Dado (Given):** Creamos una lista de `ProyectoVerDTO`. Luego, programamos el mock: `given(servicioProyecto.obtenerTodosLosProyectos()).willReturn(listaDeDTOs);`. Esto se traduce como: "Cuando el controlador llame al método `obtenerTodosLosProyectos`, quiero que tú, mock, devuelvas esta lista de DTOs".
- **Cuando (When):** Simulamos una petición `GET` a `/proyectos` usando `mvc.perform(...)`.
- **Entonces (Then):** Usamos una cadena de `andExpect()` para verificar el resultado:
    - `status().isOk()`: El código de estado HTTP fue 200.
    - `view().name("proyectos/listaProyectos")`: El controlador devolvió el nombre de la vista correcta.
    - `model().attribute(...)`: El modelo contiene un atributo llamado "proyectos" que es una lista de tamaño 2.
    - `content().string(containsString(...))`: El HTML renderizado final contiene los nombres de nuestros proyectos.

### **`cuandoCrearProyectoValido_entoncesRedireccionaYGuarda`**

- **Propósito:** Probar el "camino feliz" del envío de un formulario válido.
- **Dado (Given):** Programamos el mock para que, cuando reciba cualquier `ProyectoCrearDTO`, devuelva un `ProyectoVerDTO` que simula el proyecto ya guardado.
- **Cuando (When):** Simulamos una petición `POST` a `/proyectos/nuevo`, enviando el parámetro `nombre` como si viniera de un formulario.
- **Entonces (Then):** Verificamos que se sigue el patrón Post-Redirect-Get:
    - `status().is3xxRedirection()`: La respuesta es una redirección.
    - `redirectedUrl("/proyectos")`: Se redirige a la lista de proyectos.
    - `flash().attributeExists("mensajeExito")`: Se ha añadido un mensaje de éxito para la siguiente página.

### **`cuandoCrearProyectoInvalido_entoncesRetornaFormularioConErrores`**

- **Propósito:** Probar que la validación funciona.
- **Dado (Given):** No necesitamos configurar el mock, porque si la validación falla, el controlador nunca debería llegar a llamar al servicio.
- **Cuando (When):** Enviamos un `POST` con el campo `nombre` vacío.
- **Entonces (Then):** Verificamos que se nos devuelve al formulario:
    - `status().isOk()`: No hay redirección.
    - `view().name("proyectos/crearProyecto")`: Volvemos a la vista del formulario.
    - `model().attributeHasErrors(...)`: El modelo ahora contiene errores de validación para el campo `nombre`.

### **`cuandoVerDetallesProyectoExistente_entoncesRetornaVistaConDetalles`**

- **Propósito:** Probar que la página de detalles de un proyecto funciona.
- **Dado (Given):** Creamos un `ProyectoVerDTO` complejo, con `TareaVerDTO` anidados. Programamos el mock para que devuelva este DTO (envuelto en un `Optional`) cuando se le pida el proyecto con ID `1L`.
- **Cuando (When):** Hacemos un `GET` a `/proyectos/1`.
- **Entonces (Then):** Verificamos que la vista de detalles se muestra y que el modelo contiene un atributo "proyecto" con las propiedades correctas.

## **4. Conclusión**

Esta clase de prueba es un componente de software de alta calidad porque:

1. **Es Enfocada y Rápida:** Usa `@WebMvcTest` para probar solo la capa web, ignorando el resto de la aplicación.
2. **Está Aislada:** Usa `@MockitoBean` para simular dependencias, asegurando que solo estamos probando la lógica del controlador.
3. **Es Legible y Mantenible:** Sigue el patrón Given-When-Then, haciendo que la intención de cada prueba sea obvia.
4. **Es Completa:** Prueba tanto los "caminos felices" (operaciones exitosas) como los "caminos tristes" (errores de validación, recursos no encontrados), lo que garantiza la robustez del controlador.
5. **Es Robusta ante el Entorno:** Utiliza `@ActiveProfiles` para garantizar que la configuración de desarrollo no interfiera con las pruebas.

Es, en definitiva, la forma profesional y moderna de garantizar que la capa de controladores de una aplicación Spring se comporte exactamente como se espera.