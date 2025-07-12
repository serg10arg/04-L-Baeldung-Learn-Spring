### Tarea 1: Conceptos Técnicos Fundamentales

He analizado las fuentes y he extraído 5 conceptos técnicos fundamentales relacionados con la Inyección de Dependencias en Spring:

### 1. Inyección de Dependencias (DI - Dependency Injection)

- **Definición Clara**: La Inyección de Dependencias es un patrón de diseño donde un **contenedor (como Spring) se encarga de "inyectar" o proporcionar las dependencias** que un componente necesita, en lugar de que el propio componente las cree o las busque. Esto facilita la **definición de relaciones entre beans** y fomenta el código modular y desacoplado.
- **Ejemplo Práctico**: Imagina que una clase `ProjectServiceImpl` necesita utilizar un `IProjectRepository`. En lugar de que `ProjectServiceImpl` cree una instancia de `IProjectRepository`, el contenedor de Spring le *inyecta* una instancia ya preparada de `IProjectRepository` cuando se crea `ProjectServiceImpl`.

### 2. Spring Beans y Application Context

- **Definición Clara**:
    - **Beans**: Son los **objetos fundamentales que forman la columna vertebral de una aplicación Spring**, instanciados, configurados y gestionados por el contenedor IoC (Inversion of Control) de Spring.
    - **Application Context**: Es el **contenedor de Spring**, el cual es responsable de la **creación, el ciclo de vida y la gestión de las relaciones de todos los beans** de la aplicación. Es el lugar donde los beans residen y son accesibles.
- **Ejemplo Práctico**: Una clase como `ProjectServiceImpl` marcada con `@Service` o una clase definida con `@Configuration` y métodos `@Bean` se convierten en beans gestionados por el `Application Context`. El contenedor se encarga de "fetch" (obtener) estos beans y sus dependencias.

### 3. Inyección Basada en Constructor (Constructor-Based DI)

- **Definición Clara**: Este método inyecta las dependencias en una clase **a través de los argumentos de su constructor**. Cada argumento en el constructor representa una dependencia necesaria para que la clase funcione correctamente, y Spring se encarga de proveerlas automáticamente.
- **Ejemplo Práctico**:

    ```java
    @Service
    public class ProjectServiceImpl implements IProjectService {
        private IProjectRepository projectRepository;
    
        // La dependencia 'projectRepository' es inyectada a través del constructor
        public ProjectServiceImpl(IProjectRepository projectRepository) {
            this.projectRepository = projectRepository;
        }
        // ...
    }
    
    ```

- **Insight de Senior Dev**: Esta es la **forma más limpia y preferida para inyectar dependencias requeridas**. Garantiza que el objeto esté en un estado completamente válido e inicializado desde su creación, ya que todas sus dependencias necesarias son pasadas en el constructor. Además, facilita la **testabilidad del código** fuera del contenedor Spring, ya que las dependencias pueden ser inyectadas manualmente en pruebas unitarias. La anotación `@Autowired` es opcional si solo hay un constructor.

### 4. Inyección Basada en Setter (Setter-Based DI)

- **Definición Clara**: En este método, las dependencias son inyectadas en una clase **mediante el uso de los métodos *setter* públicos** de los campos que representan las dependencias. Se utiliza la anotación `@Autowired` sobre el método *setter* para indicar a Spring que debe inyectar la dependencia. El contenedor realiza esta inyección *después* de que el constructor de la clase ha sido invocado.
- **Ejemplo Práctico**:

    ```java
    public class ProjectServiceImplSetterInjection implements IProjectService {
        private IProjectRepository projectRepository;
    
        @Autowired // La dependencia 'projectRepository' es inyectada a través de este setter
        public void setProjectRepository(IProjectRepository projectRepository) {
            this.projectRepository = projectRepository;
        }
        // ...
    }
    
    ```

- **Insight de Senior Dev**: Este método es adecuado para **dependencias opcionales** o para casos donde una dependencia podría ser cambiada en tiempo de ejecución. Proporciona flexibilidad, pero no garantiza que la dependencia esté presente al momento de la construcción del objeto, lo que podría llevar a un estado inconsistente si no se maneja cuidadosamente.

### 5. Resolución de Conflictos en DI (`@Qualifier` y `@Primary`)

- **Definición Clara**: Ocurre un **conflicto de inyección** cuando Spring encuentra **múltiples beans del mismo tipo** que podrían satisfacer una dependencia, y no puede decidir cuál inyectar, resultando en un error de arranque. Para resolver esto, se utilizan dos anotaciones clave:
    - **`@Qualifier`**: Permite especificar el **nombre exacto del bean** que se desea inyectar cuando hay varias implementaciones de la misma interfaz o tipo.
    - **`@Primary`**: Otorga una **mayor preferencia a un bean específico** del mismo tipo, marcándolo como el candidato por defecto para la inyección automática cuando no se especifica explícitamente otro.
- **Ejemplo Práctico (`@Qualifier`)**:

    ```java
    @Service
    public class ProjectServiceImpl implements IProjectService {
        private IProjectRepository projectRepository;
    
        // Inyecta el bean llamado "projectRepositoryImpl2"
        public ProjectServiceImpl(@Qualifier("projectRepositoryImpl2")
                                  IProjectRepository projectRepository) {
            this.projectRepository = projectRepository;
        }
        //...
    }
    
    ```

- **Ejemplo Práctico (`@Primary`)**: Si `ProjectRepositoryImpl2` debe ser la opción por defecto cuando hay varios, se anota directamente la clase de la siguiente manera:

    ```java
    @Repository
    @Primary // Marca este bean como el preferido
    public class ProjectRepositoryImpl2 implements IProjectRepository {
        // ...
    }
    
    ```

- **Insight de Senior Dev**: Estas anotaciones son **cruciales en aplicaciones complejas** donde es común tener múltiples estrategias o implementaciones para una misma funcionalidad (e.g., diferentes bases de datos, proveedores de servicios). `@Qualifier` ofrece un **control explícito y preciso**, mientras que `@Primary` es ideal para establecer un **comportamiento por defecto** que la mayoría de los consumidores utilizarán. La falta de uno de estos mecanismos cuando hay ambigüedad resultará en un fallo en el arranque de la aplicación.

---

### Tarea 2: Caso Práctico de Implementación - Sistema de Gestión de Inventario

### Problema a Resolver: Sistema de Inventario para Tienda de Electrónica

Necesitamos desarrollar un sistema de gestión de inventario para una tienda de electrónica. Los requisitos clave son:

- Registrar y consultar productos.
- Capacidad para persistir productos utilizando diferentes mecanismos de almacenamiento (ej., una base de datos principal y un sistema de archivos para copias de seguridad o datos históricos específicos).
- Registrar (loguear) las operaciones importantes realizadas sobre el inventario.

El objetivo es construir un sistema modular y extensible utilizando los conceptos de Inyección de Dependencias de Spring.

### Solución Paso a Paso y Fragmentos de Código

**Paso 1: Definición de Interfaces (Contratos de los Componentes)**

Definimos las interfaces que establecerán los contratos para nuestros servicios y repositorios. Estas interfaces serán implementadas por nuestros beans.

```java
// Contrato para la capa de persistencia de productos
package com.example.inventory.repository;

public interface ProductRepository {
    String save(String productDetails); // Guarda los detalles de un producto y retorna un mensaje de confirmación.
    String findById(String productId); // Busca un producto por su ID y retorna sus detalles.
}

// Contrato para la capa de lógica de negocio de productos
package com.example.inventory.service;

public interface ProductService {
    String addProduct(String productDetails); // Añade un nuevo producto al inventario.
    String getProduct(String productId); // Recupera un producto existente por su ID.
}

// Contrato para el componente de registro de operaciones de inventario
package com.example.inventory.logger;

public interface InventoryLogger {
    void log(String message); // Registra un mensaje de evento en el inventario.
}

```

**Paso 2: Implementaciones de Beans (ProductRepository y InventoryLogger)**

Implementamos las interfaces, creando beans que Spring gestionará. Crearemos dos implementaciones de `ProductRepository` para demostrar la resolución de conflictos.

```java
// Implementación principal del repositorio de productos, usando una "base de datos JPA"
package com.example.inventory.repository;

import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Primary; // Marca este bean como el preferido

@Repository("jpaProductRepository") // Define el nombre explícito del bean
@Primary // Este bean será el inyectado por defecto si no se especifica @Qualifier
public class JpaProductRepositoryImpl implements ProductRepository {
    @Override
    public String save(String productDetails) {
        return "Producto '" + productDetails + "' guardado en la base de datos JPA.";
    }
    @Override
    public String findById(String productId) {
        return "Producto '" + productId + "' encontrado en la base de datos JPA.";
    }
}

// Implementación alternativa del repositorio de productos, usando "archivos CSV"
package com.example.inventory.repository;

import org.springframework.stereotype.Repository;

@Repository("csvProductRepository") // Define el nombre explícito del bean
public class CsvProductRepositoryImpl implements ProductRepository {
    @Override
    public String save(String productDetails) {
        return "Producto '" + productDetails + "' guardado en archivo CSV.";
    }
    @Override
    public String findById(String productId) {
        return "Producto '" + productId + "' encontrado en archivo CSV.";
    }
}

// Implementación del logger de inventario, para mostrar mensajes en consola
package com.example.inventory.logger;

import org.springframework.stereotype.Component; // Marca esta clase como un componente genérico

@Component
public class ConsoleInventoryLogger implements InventoryLogger {
    @Override
    public void log(String message) {
        System.out.println("[LOG INVENTARIO]: " + message);
    }
}

```

**Paso 3: Implementación de Servicio con Inyección de Constructor y Setter**

La clase `ProductServiceImpl` contendrá la lógica de negocio. Utilizará la **inyección de constructor** para su dependencia principal (`ProductRepository`) y la **inyección por *setter*** para una dependencia opcional (`InventoryLogger`). Además, resolverá el posible conflicto de `ProductRepository`.

```java
package com.example.inventory.service;

import com.example.inventory.logger.InventoryLogger;
import com.example.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired; //
import org.springframework.beans.factory.annotation.Qualifier; //
import org.springframework.stereotype.Service; //

@Service // Indica que esta clase es un bean de servicio
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository; // Dependencia esencial e inmutable
    private InventoryLogger inventoryLogger; // Dependencia opcional, puede cambiar después de la construcción

    // 1. Inyección Basada en Constructor para dependencia requerida
    // Usamos @Qualifier para especificar qué implementación de ProductRepository inyectar.
    // Aunque jpaProductRepository está marcado como @Primary, @Qualifier tiene mayor precedencia.
    public ProductServiceImpl(@Qualifier("jpaProductRepository") ProductRepository productRepository) { //
        this.productRepository = productRepository;
    }

    // 2. Inyección Basada en Setter para dependencia opcional
    // 'required = false' permite que esta dependencia sea opcional; la inyección no fallará si no hay un bean de InventoryLogger.
    @Autowired(required = false) //
    public void setInventoryLogger(InventoryLogger inventoryLogger) {
        this.inventoryLogger = inventoryLogger;
    }

    @Override
    public String addProduct(String productDetails) {
        String result = productRepository.save(productDetails);
        if (inventoryLogger != null) { // Solo loguea si el logger ha sido inyectado
            inventoryLogger.log("Producto añadido: " + productDetails);
        }
        return result;
    }

    @Override
    public String getProduct(String productId) {
        String result = productRepository.findById(productId);
        if (inventoryLogger != null) {
            inventoryLogger.log("Producto buscado: " + productId);
        }
        return result;
    }
}

```

**Paso 4: Aplicación Principal (Demostración del Spring Context)**

La clase `InventoryApp` es nuestra aplicación Spring Boot. Iniciará el `Application Context` y obtendrá una instancia de `ProductService` para interactuar con el sistema de inventario. Esto demuestra cómo Spring ha gestionado y conectado todos los beans automáticamente.

```java
package com.example.inventory;

import com.example.inventory.service.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext; //

@SpringBootApplication // Anotación que configura y lanza la aplicación Spring Boot.
// Por defecto, escanea componentes en el paquete base y sus subpaquetes.
public class InventoryApp {

    public static void main(String[] args) {
        // El SpringApplication.run() inicializa el ApplicationContext de Spring,
        // descubriendo y configurando todos nuestros beans.
        ApplicationContext context = SpringApplication.run(InventoryApp.class, args);

        // Obtenemos el ProductService del ApplicationContext. Spring inyectó
        // automáticamente el ProductRepository y el InventoryLogger en él.
        ProductService productService = context.getBean(ProductService.class);

        System.out.println("--- Realizando Operaciones de Inventario ---");
        System.out.println(productService.addProduct("Laptop Dell XPS 15"));
        System.out.println(productService.getProduct("Laptop Dell XPS 15"));

        System.out.println(productService.addProduct("Smartphone Samsung S23"));
        System.out.println(productService.getProduct("Smartphone Samsung S23"));

        System.out.println("--- Fin de Operaciones ---");

        // Para demostrar el fallo de "múltiples beans" sin @Qualifier/@Primary:
        // 1. Remueve la anotación @Primary de 'JpaProductRepositoryImpl'.
        // 2. Remueve la anotación @Qualifier("jpaProductRepository") del constructor de 'ProductServiceImpl'.
        // 3. Al ejecutar 'InventoryApp', Spring fallará con un mensaje indicando que encontró 2 beans de tipo ProductRepository
        //    y no pudo decidir cuál inyectar.
    }
}

```

Este caso práctico integra y demuestra los 5 conceptos fundamentales: el `Application Context` gestiona los **beans** (`JpaProductRepositoryImpl`, `CsvProductRepositoryImpl`, `ConsoleInventoryLogger`, `ProductServiceImpl`), la **Inyección de Dependencias** conecta `ProductService` con sus dependencias. Se utiliza **Inyección por Constructor** para la dependencia requerida (`ProductRepository`) y **Inyección por Setter** para la opcional (`InventoryLogger`). Finalmente, `@Primary` y `@Qualifier` resuelven la ambigüedad cuando existen múltiples implementaciones de `ProductRepository`.