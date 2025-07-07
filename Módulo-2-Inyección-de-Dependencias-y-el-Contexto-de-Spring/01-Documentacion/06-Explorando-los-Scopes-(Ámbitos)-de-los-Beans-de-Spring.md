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


---

### **Caso Práctico de Implementación: Gestión de Repositorios de Proyectos**

### **Problema a resolver**

Necesitamos gestionar las instancias de un repositorio de proyectos (`ProjectRepositoryImpl`) dentro de una aplicación Spring. Se requiere que el repositorio de proyectos se comporte de dos maneras distintas:

1. **Una instancia compartida (Singleton)**: Para operaciones globales o configuraciones de acceso a datos que deben ser consistentes y únicas para toda la aplicación.
2. **Múltiples instancias (Prototype)**: Para tareas o procesos específicos que requieren su propia "copia" del repositorio, garantizando aislamiento y evitando conflictos entre diferentes ejecuciones concurrentes.

### **Propuesta de solución paso a paso**

1. **Definir la interfaz y la implementación del repositorio**: Creamos `IProjectRepository` y `ProjectRepositoryImpl`. La implementación incluirá un identificador único para cada instancia, permitiendo verificar visualmente si se trata del mismo objeto o de uno nuevo.
2. **Configurar los Beans en Spring**: En una clase `@Configuration`, definiremos dos beans del tipo `IProjectRepository`: uno con ámbito `singleton` (explícita o implícitamente) y otro con ámbito `prototype`.
3. **Crear un Servicio que Inyecte los Repositorios**: Un servicio (`ProjectServiceImpl`) que simule el uso de estos repositorios inyectará múltiples instancias de cada tipo de bean (`singleton` y `prototype`) para demostrar su comportamiento.
4. **Verificar el Comportamiento de los Ámbitos**: Utilizaremos un método anotado con `@PostConstruct` en el servicio para comparar las referencias de las instancias inyectadas y confirmar si son el mismo objeto o diferentes, de acuerdo con su ámbito.

### **Fragmentos de Código (Java con Spring Boot)**

**1. Interfaz y Implementación del Repositorio**

`IProjectRepository.java`

```java
// src/main/java/com/example/repository/IProjectRepository.java
package com.example.repository;

public interface IProjectRepository {
    String getRepositoryId(); // Método para obtener un ID único de la instancia
    void performOperation(String operation); // Método de ejemplo
}

```

`ProjectRepositoryImpl.java`

```java
// src/main/java/com/example/repository/ProjectRepositoryImpl.java
package com.example.repository;

// NOTA: Según las fuentes, se debe comentar la anotación @Repository para evitar conflictos de inyección.
// import org.springframework.stereotype.Repository;

// @Repository // Comentado para la demostración de beans definidos con @Bean
public class ProjectRepositoryImpl implements IProjectRepository {
    private String repositoryId;

    public ProjectRepositoryImpl() {
        // Generamos un ID único para cada instancia creada
        this.repositoryId = "Repo-" + System.nanoTime();
        System.out.println("DEBUG: Instancia de ProjectRepositoryImpl creada con ID: " + this.repositoryId);
    }

    @Override
    public String getRepositoryId() {
        return repositoryId;
    }

    @Override
    public void performOperation(String operation) {
        System.out.println("DEBUG: Ejecutando '" + operation + "' en repositorio con ID: " + this.repositoryId);
    }
}

```

**2. Clase de Configuración de Spring (`@Configuration`)**

`LsAppConfig.java`

```java
// src/main/java/com/example/config/LsAppConfig.java
package com.example.config;

import com.example.repository.IProjectRepository;
import com.example.repository.ProjectRepositoryImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory; // Opcional para la constante
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration // Indica que esta clase define beans de Spring
public class LsAppConfig {

    // Definición de un bean con ámbito Singleton (instancia única, por defecto o explícita)
    @Bean
    // @Scope("singleton") // Es el ámbito por defecto, pero se puede especificar explícitamente
    // @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON) // Otra forma explícita
    public IProjectRepository sharedProjectRepository() {
        System.out.println("INFO: Definiendo bean 'sharedProjectRepository' (Singleton)...");
        return new ProjectRepositoryImpl();
    }

    // Definición de un bean con ámbito Prototype (nueva instancia por cada solicitud)
    @Bean
    @Scope("prototype") // Define el ámbito como prototype
    public IProjectRepository temporaryProjectRepository() {
        System.out.println("INFO: Definiendo bean 'temporaryProjectRepository' (Prototype)...");
        return new ProjectRepositoryImpl();
    }
}

```

**3. Servicio que Inyecta los Repositorios (`@Service`, `@Autowired`, `@PostConstruct`)**

`IProjectService.java`

```java
// src/main/java/com/example/service/IProjectService.java
package com.example.service;

public interface IProjectService {
    void runRepositoryDemo();
}

```

`ProjectServiceImpl.java`

```java
// src/main/java/com/example/service/ProjectServiceImpl.java
package com.example.service;

import com.example.repository.IProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct; // Para el método que se ejecuta después de la inyección

@Service // Marca esta clase como un componente de servicio de Spring
public class ProjectServiceImpl implements IProjectService {

    // Inyección de dos referencias al bean 'sharedProjectRepository' (Singleton)
    @Autowired // Inyecta una dependencia
    private IProjectRepository singletonRepo1;

    @Autowired // Inyecta otra dependencia del mismo tipo
    private IProjectRepository singletonRepo2;

    // Inyección de dos referencias al bean 'temporaryProjectRepository' (Prototype)
    @Autowired
    private IProjectRepository prototypeRepo1;

    @Autowired
    private IProjectRepository prototypeRepo2;

    @PostConstruct // Este método se ejecuta después de que todas las dependencias han sido inyectadas
    public void afterDependenciesInjected() {
        System.out.println("\n--- VERIFICACIÓN DE INSTANCIAS EN ProjectServiceImpl (@PostConstruct) ---");

        // Verificación de beans Singleton
        System.out.println("\n--- Instancias Singleton ---");
        System.out.println("  ID de singletonRepo1: " + singletonRepo1.getRepositoryId());
        System.out.println("  ID de singletonRepo2: " + singletonRepo2.getRepositoryId());
        // Se espera que sean el mismo objeto
        System.out.println("  ¿singletonRepo1 y singletonRepo2 son el mismo objeto? " + (singletonRepo1 == singletonRepo2));
        singletonRepo1.performOperation("Operación Global A");
        singletonRepo2.performOperation("Operación Global B");

        // Verificación de beans Prototype
        System.out.println("\n--- Instancias Prototype ---");
        System.out.println("  ID de prototypeRepo1: " + prototypeRepo1.getRepositoryId());
        System.out.println("  ID de prototypeRepo2: " + prototypeRepo2.getRepositoryId());
        // Se espera que sean objetos diferentes
        System.out.println("  ¿prototypeRepo1 y prototypeRepo2 son el mismo objeto? " + (prototypeRepo1 == prototypeRepo2));
        prototypeRepo1.performOperation("Tarea Temporal X");
        prototypeRepo2.performOperation("Tarea Temporal Y");

        System.out.println("\n--- FIN DE LA VERIFICACIÓN ---");
    }

    @Override
    public void runRepositoryDemo() {
        System.out.println("\nProjectService está en ejecución, los resultados de la inyección se muestran arriba.");
    }
}

```

**4. Clase Principal de la Aplicación Spring Boot**

`SpringBeanScopeDemoApplication.java`

```java
// src/main/java/com/example/SpringBeanScopeDemoApplication.java
package com.example;

import com.example.service.IProjectService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan; // Para asegurar que Spring escanee los paquetes correctos

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.config", "com.example.service", "com.example.repository"}) // Escanear nuestros paquetes
public class SpringBeanScopeDemoApplication {

    public static void main(String[] args) {
        // Inicializa el contexto de la aplicación Spring Boot
        ApplicationContext context = SpringApplication.run(SpringBeanScopeDemoApplication.class, args);

        // Obtenemos el servicio para asegurar que se cargue y se active el @PostConstruct
        IProjectService projectService = context.getBean(IProjectService.class);
        projectService.runRepositoryDemo();

        // Para observar el comportamiento, se pueden agregar puntos de interrupción en el método @PostConstruct
        // y ejecutar la aplicación en modo depuración.
    }
}

```

Al ejecutar esta aplicación, se observará en la consola lo siguiente:

- Se verá que `ProjectRepositoryImpl` solo se instancia una vez para el bean `sharedProjectRepository` (Singleton), y su ID será el mismo para `singletonRepo1` y `singletonRepo2`.
- Se verá que `ProjectRepositoryImpl` se instancia dos veces (o tantas veces como se inyecte) para el bean `temporaryProjectRepository` (Prototype), y sus IDs serán diferentes para `prototypeRepo1` y `prototypeRepo2`.

Este caso práctico integra y demuestra claramente los cinco conceptos fundamentales extraídos de las fuentes, mostrando cómo los ámbitos `singleton` y `prototype` impactan la gestión de instancias de beans a través de la inyección de dependencias en una aplicación Spring.