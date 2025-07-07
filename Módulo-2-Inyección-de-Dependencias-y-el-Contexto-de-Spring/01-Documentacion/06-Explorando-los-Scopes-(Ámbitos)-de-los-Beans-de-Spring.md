### **Conceptos Técnicos Fundamentales**

Basado en las fuentes proporcionadas, a continuación, se presentan cinco conceptos técnicos fundamentales sobre los beans de Spring:

### **1. Spring Beans**

- **Definición**: Un **Spring Bean** es, fundamentalmente, un objeto que es instanciado, ensamblado y gestionado por el contenedor de Spring. El objetivo principal de los beans de Spring es permitirnos **controlar el ciclo de vida y la visibilidad** de estos componentes dentro de nuestra aplicación. Los beans se definen típicamente en una clase decorada con la anotación `@Configuration`.
- **Ejemplo práctico**: Se puede definir un bean simple dentro de una clase de configuración como `LsAppConfig.java`. Si no se especifica explícitamente un ámbito, por defecto será `singleton`:

    ```java
    @Configuration
    public class LsAppConfig {
        @Bean // Esta anotación define un bean
        public IProjectRepository singletonBean() {
            return new ProjectRepositoryImpl();
        }
    }
    ```


### **2. Ámbitos de los Beans (Bean Scopes)**

- **Definición**: Los **ámbitos de los beans de Spring** son un mecanismo que nos permite **controlar el ciclo de vida y la visibilidad** de los beans que gestiona el contenedor de Spring. Al momento de crear un bean, su ámbito puede ser definido mediante la anotación `@Scope`. Existen seis tipos de ámbitos en Spring: `singleton`, `prototype`, `request`, `session`, `application` y `websocket`. Los ámbitos `request`, `session`, `application` y `websocket` son específicos de contextos de aplicaciones web y se usan con menor frecuencia en la práctica general.
- **Ejemplo práctico**: El concepto general de ámbitos se aplica al especificar un ámbito para un bean. Por ejemplo, al definir un bean `prototype`, se usa `@Scope("prototype")`.

### **3. Ámbito Singleton**

- **Definición**: El ámbito **`singleton`** es el ámbito por defecto en Spring si no se especifica ningún otro. Con este ámbito, el contenedor de Spring crea **una única instancia de un bean**. Todas las solicitudes posteriores para ese bean devolverán la **misma instancia de objeto**, la cual es cacheada por el contenedor.
- **Ejemplo práctico**:
    - Definición por defecto (implícita):

        ```java
        @Bean
        public IProjectRepository defaultSingletonBean() {
            return new ProjectRepositoryImpl();
        }
        ```

    - Definición explícita usando `@Scope` o una constante:

        ```java
        @Bean
        @Scope("singleton") // O @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
        public IProjectRepository explicitSingletonBean() {
            return new ProjectRepositoryImpl();
        }
        ```

    - Demostración: Si se inyectan dos referencias al mismo bean `singleton`, ambas variables **apuntarán al mismo objeto** en memoria. Esto se comprueba al depurar el código y observar que `projectRepository` y `projectRepository2` se refieren al mismo objeto.

### **4. Ámbito Prototype**

- **Definición**: El ámbito **`prototype`** indica al contenedor de Spring que debe **crear una nueva instancia del bean cada vez que sea solicitado**. A diferencia del `singleton`, no se cachea una única instancia, sino que se genera una nueva para cada inyección o `getBean()`.
- **Ejemplo práctico**: Para definir un bean con ámbito `prototype`:

    ```java
    @Bean
    @Scope("prototype")
    public IProjectRepository prototypeBean() {
        return new ProjectRepositoryImpl();
    }
    ```

    - Demostración: Si se inyectan dos referencias al mismo bean con ámbito `prototype`, esas variables se referirán a **objetos diferentes** en memoria. Esto es la "completa concordancia con la definición del ámbito prototype".

### **5. Inyección de Dependencias con `@Autowired`**

- **Definición**: La inyección de dependencias es un patrón fundamental en Spring donde el contenedor se encarga de proporcionar las instancias de los objetos (beans) que una clase necesita. La anotación **`@Autowired`** se utiliza para marcar un punto de inyección, permitiendo que Spring automáticamente encuentre y asigne una instancia compatible del bean requerido. Es esencial para conectar los diferentes componentes (beans) de una aplicación.
- **Ejemplo práctico**: En la clase `ProjectServiceImpl`, se inyectan dos instancias de `IProjectRepository` usando `@Autowired`. Además, se puede utilizar la anotación `@PostConstruct` en un método para ejecutar código una vez que todas las dependencias hayan sido inyectadas, lo cual es útil para la verificación:

    ```java
    @Service
    public class ProjectServiceImpl implements IProjectService {
        @Autowired
        private IProjectRepository projectRepository; // Primera inyección
        @Autowired
        private IProjectRepository projectRepository2; // Segunda inyección
    
        @PostConstruct
        public void after() {
            // Aquí se pueden verificar si projectRepository y projectRepository2 son el mismo objeto
            // o diferentes, dependiendo del ámbito del bean inyectado.
        }
        // ...
    }
    ```