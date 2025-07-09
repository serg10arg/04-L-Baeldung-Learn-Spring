### **Tarea 1: Conceptos Técnicos Fundamentales**

Basándome en las fuentes proporcionadas, he extraído los cinco conceptos técnicos fundamentales que te permitirán entender cómo Spring permite la personalización del ciclo de vida de los beans.

### **1. Puntos de Extensión del Contenedor Spring**

- **Definición Clara:** Spring ofrece puntos de extensión específicos que permiten a los desarrolladores **"engancharse" e intervenir en el proceso de inicialización del contexto** y la gestión de beans. Estos puntos brindan la capacidad de modificar beans o sus configuraciones, o de ejecutar lógica personalizada en momentos clave del ciclo de vida del contenedor.
- **Ejemplo Práctico:** Las fuentes mencionan explícitamente las interfaces `BeanPostProcessor` y `BeanFactoryPostProcessor` como los principales puntos de extensión para esta lección. Además, la interfaz `FactoryBean` se presenta como otro punto de extensión para enchufar fábricas personalizadas para la creación de beans complejos.

### **2. `BeanPostProcessor`**

- **Definición Clara:** Es una interfaz de Spring que permite **modificar un bean o ejecutar código personalizado *después de que el bean ha sido instanciado*** por el contenedor. El contenedor invoca este post-procesador **para cada uno de los beans** que inicializa. Ofrece dos métodos de callback predeterminados:
    - `postProcessBeforeInitialization`: Se invoca *antes* de cualquier método de inicialización del bean (como `@PostConstruct`).
    - `postProcessAfterInitialization`: Se invoca *después* de los métodos de inicialización del bean.
- **Ejemplo Práctico:** Se muestra cómo crear una clase `MyBeanPostProcessor` que implementa `BeanPostProcessor` y está anotada con `@Component`. Esta clase puede registrar (log) el nombre del bean que se está inicializando antes y después de su inicialización, demostrando su invocación para cada bean. Por ejemplo, un log podría mostrar "Before initialising the bean: [nombreDelBean]" y "After initialising the bean: [nombreDelBean]".

### **3. `BeanFactoryPostProcessor`**

- **Definición Clara:** Es una interfaz que permite **leer la metadata de configuración de un bean y, potencialmente, modificarla *antes de que el contenedor haya instanciado* cualquiera de los beans**. A diferencia de `BeanPostProcessor`, este opera a nivel de la definición del bean (BeanDefinition), no sobre la instancia del bean ya creada. Contiene el método `postProcessBeanFactory`, que proporciona acceso a la `ConfigurableListableBeanFactory` para obtener y modificar las `BeanDefinition`s.
- **Ejemplo Práctico:** Las fuentes ilustran cómo crear un `MyBeanFactoryPostProcessor`. Este puede modificar el valor de una propiedad (ej., `foo`) de un bean (`BeanA`) *antes* de que `BeanA` sea instanciado. Esto se logra obteniendo la `BeanDefinition` del bean y añadiendo un valor a sus `PropertyValues`. La verificación de que esto ocurre antes de la instanciación se demuestra mediante un `breakpoint` en `postProcessBeanFactory` donde se observa que ningún bean ha sido instanciado aún.

### **4. La Interfaz `Ordered`**

- **Definición Clara:** Spring proporciona la interfaz `Ordered` para permitir el **control del orden de ejecución de múltiples `BeanPostProcessor`s y `BeanFactoryPostProcessor`s** cuando hay más de uno en el contexto de la aplicación. Aquellos con un valor de orden **inferior se ejecutan primero**.
- **Ejemplo Práctico:** Se pueden tener dos `BeanPostProcessor`s, como `MyBeanPostProcessor` y `CustomBeanPostProcessor`. Si `MyBeanPostProcessor` implementa `Ordered` y su método `getOrder()` devuelve `1`, y `CustomBeanPostProcessor` devuelve `2`, `MyBeanPostProcessor` se ejecutará antes que `CustomBeanPostProcessor`. Las fuentes muestran esto a través de mensajes de log que confirman la secuencia de invocación.

### **5. Definición de Post-Procesadores como Métodos `static @Bean`**

- **Definición Clara:** Además de registrar `BeanPostProcessor`s y `BeanFactoryPostProcessor`s con `@Component` en sus clases, se pueden definir como **métodos `@Bean` estáticos dentro de una clase de configuración (`@Configuration`)**. La palabra clave `static` es crucial aquí porque asegura que estos beans de post-procesador se inicialicen **muy temprano en el ciclo de vida del contenedor**, incluso antes de que la propia clase de configuración que los contiene sea completamente inicializada.
- **Propósito:** Este enfoque es útil para evitar que otras partes de la configuración se activen prematuramente antes de que los post-procesadores estén listos para interceptar los beans.
- **Ejemplo Práctico:** En una clase `AppConfig`, se puede declarar un método `@Bean` para un `MyBeanPostProcessor` como `public static MyBeanPostProcessor beanPostProcessor()`.

---

### **Tarea 2: Caso Práctico de Implementación - Sistema de Gestión de Productos de E-commerce**

**Problema a Resolver:**
Un sistema de e-commerce necesita una gestión del ciclo de vida de los productos que asegure:

1. **Auditoría de Creación:** Registrar con detalle cuándo un bean de producto (`Producto`) comienza su inicialización y cuándo la finaliza.
2. **Configuración Predeterminada de SKU:** Asignar un prefijo de SKU por defecto a los productos *antes* de su instanciación, para garantizar una consistencia inicial en los datos.
3. **Orden de Auditoría:** Si hay varios mecanismos de auditoría o personalización, deben ejecutarse en un orden predefinido.
4. **Inicialización Temprana:** Asegurar que los componentes de auditoría y configuración se activen lo antes posible en el ciclo de vida de la aplicación para no perder ninguna acción de inicialización de productos.

**Propuesta de Solución Paso a Paso:**

Implementaremos una aplicación Spring Boot para simular un sistema de gestión de productos que utiliza los puntos de extensión del contenedor de Spring para lograr los objetivos descritos.

### **1. Definir la Clase `Producto`**

Esta clase representará nuestro bean de producto. Incluirá propiedades y un método `@PostConstruct` para demostrar cuándo se completa su inicialización.

```java
// src/main/java/com/ejemplo/ecommerce/modelo/Producto.java
package com.ejemplo.ecommerce.modelo;

import jakarta.annotation.PostConstruct; // Importación para Spring Boot 3+
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producto {
    private static final Logger LOG = LoggerFactory.getLogger(Producto.class);

    private String id;
    private String nombre;
    private String sku; // Esta propiedad será modificada por el BeanFactoryPostProcessor

    public Producto() {
        // Constructor predeterminado
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    @PostConstruct // Este método se invoca después de la inyección de dependencias
    public void inicializarProducto() {
        LOG.info("➡️ Producto '{}' (ID: {}) ha sido inicializado. SKU actual: {}", this.nombre, this.id, this.sku);
    }
}

```

- **Nota del Desarrollador Senior:** Para `jakarta.annotation.PostConstruct`, asegúrate de tener la dependencia `jakarta.annotation-api` en tu `pom.xml` si usas Spring Boot 3+ y Java 17+. Para versiones anteriores (Spring Boot 2.x, Java 8/11), se usaría `javax.annotation.PostConstruct`. Esto es conocimiento adicional fuera de las fuentes proporcionadas, pero esencial para la compilación.

### **2. Crear un `BeanFactoryPostProcessor` para Configuración de SKU**

Este post-procesador interceptará la definición del bean `Producto` y le asignará un `sku` por defecto *antes* de que Spring cree la instancia real del objeto.

```java
// src/main/java/com/ejemplo/ecommerce/config/ConfiguradorSkuProductoFactory.java
package com.ejemplo.ecommerce.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor; // Se ajusta a la fuente
import org.springframework.core.Ordered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfiguradorSkuProductoFactory implements BeanFactoryPostProcessor, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(ConfiguradorSkuProductoFactory.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        LOG.info("⚙️ Ejecutando ConfiguradorSkuProductoFactory (BeanFactoryPostProcessor). Modificando BeanDefinition antes de la instanciación.");

        // Obtener la BeanDefinition del bean llamado "productoPrincipal"
        // Asegúrate de que el nombre aquí coincida con el nombre del bean en tu @Configuration
        if (beanFactory.containsBeanDefinition("productoPrincipal")) { // Verificamos si el bean existe
            BeanDefinition bd = beanFactory.getBeanDefinition("productoPrincipal");
            // Modificar la propiedad 'sku' de la definición del bean
            bd.getPropertyValues().add("sku", "DEFAULT-SKU-ECOMMERCE");
            LOG.info("✅ Propiedad 'sku' de 'productoPrincipal' modificada a 'DEFAULT-SKU-ECOMMERCE'.");
        } else {
            LOG.warn("⚠️ BeanDefinition para 'productoPrincipal' no encontrada. La configuración de SKU no se aplicará.");
        }
    }

    @Override
    public int getOrder() {
        return 1; // Prioridad alta, se ejecuta primero
    }
}

```

### **3. Crear un `BeanPostProcessor` para Auditoría General**

Este post-procesador registrará mensajes de auditoría para *cada* bean de `Producto` antes y después de su inicialización.

```java
// src/main/java/com/ejemplo/ecommerce/config/AuditorProductoPostProcesador.java
package com.ejemplo.ecommerce.config;

import com.ejemplo.ecommerce.modelo.Producto;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditorProductoPostProcesador implements BeanPostProcessor, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(AuditorProductoPostProcesador.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Producto) {
            LOG.info("Auditor General 🕵️‍♂️: Antes de inicializar el bean: '{}' (ID: {})", beanName, ((Producto) bean).getId());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Producto) {
            LOG.info("Auditor General 🕵️‍♂️: Después de inicializar el bean: '{}' (ID: {})", beanName, ((Producto) bean).getId());
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return 10; // Un orden intermedio, se ejecuta antes del detallado si el orden es menor
    }
}

```

### **4. Crear Otro `BeanPostProcessor` para Log Detallado (con Orden Específico)**

Este segundo `BeanPostProcessor` simulará un log más detallado, y su orden de ejecución será posterior al auditor general.

```java
// src/main/java/com/ejemplo/ecommerce/config/LogDetalladoProductoPostProcesador.java
package com.ejemplo.ecommerce.config;

import com.ejemplo.ecommerce.modelo.Producto;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogDetalladoProductoPostProcesador implements BeanPostProcessor, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(LogDetalladoProductoPostProcesador.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Producto) {
            LOG.debug("Log Detallado 📊: Iniciando procesamiento detallado para bean: '{}'", beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Producto) {
            LOG.debug("Log Detallado 📊: Finalizado procesamiento detallado para bean: '{}'", beanName);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return 20; // Se ejecuta después de AuditorProductoPostProcesador (10 < 20)
    }
}

```

### **5. Clase de Configuración de Spring (`ConfiguracionEcommerce`)**

Aquí definiremos nuestros beans de `Producto` y registraremos nuestros post-procesadores utilizando métodos `static @Bean` para asegurar su inicialización temprana.

```java
// src/main/java/com/ejemplo/ecommerce/config/ConfiguracionEcommerce.java
package com.ejemplo.ecommerce.config;

import com.ejemplo.ecommerce.modelo.Producto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Indica que esta clase es una fuente de definición de beans
public class ConfiguracionEcommerce {

    @Bean // Define un bean de tipo Producto
    public Producto productoPrincipal() { // Nombre del bean usado en ConfiguradorSkuProductoFactory
        Producto producto = new Producto();
        producto.setId("PROD-001");
        producto.setNombre("Laptop Pro");
        // El 'sku' se establecerá por ConfiguradorSkuProductoFactory
        return producto;
    }

    @Bean // Otro bean de Producto para demostrar que los BeanPostProcessor se aplican a todos los beans
    public Producto accesorioProducto() {
        Producto producto = new Producto();
        producto.setId("ACC-001");
        producto.setNombre("Mouse Inalámbrico");
        // Este bean no será afectado por ConfiguradorSkuProductoFactory, ya que solo busca "productoPrincipal"
        return producto;
    }

    // Registramos nuestro BeanFactoryPostProcessor como un método estático @Bean
    @Bean
    public static ConfiguradorSkuProductoFactory configuradorSkuProductoFactory() {
        return new ConfiguradorSkuProductoFactory();
    }

    // Registramos nuestros BeanPostProcessors como métodos estáticos @Bean
    @Bean
    public static AuditorProductoPostProcesador auditorProductoPostProcesador() {
        return new AuditorProductoPostProcesador();
    }

    @Bean
    public static LogDetalladoProductoPostProcesador logDetalladoProductoPostProcesador() {
        return new LogDetalladoProductoPostProcesador();
    }
}

```

### **6. Clase Principal de la Aplicación Spring Boot**

Esta clase es el punto de entrada para iniciar el contexto de la aplicación.

```java
// src/main/java/com/ejemplo/ecommerce/EcommerceApplication.java
package com.ejemplo.ecommerce;

import com.ejemplo.ecommerce.config.ConfiguracionEcommerce;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ConfiguracionEcommerce.class, args);
        // Opcional: Obtener un bean para interactuar con él si es necesario
        // Producto p = context.getBean("productoPrincipal", Producto.class);
        // System.out.println("Producto principal obtenido del contexto: " + p.getNombre() + " (SKU: " + p.getSku() + ")");
    }
}

```

**Verificación y Salida Esperada:**

Al ejecutar la aplicación, observarás la siguiente secuencia en los logs (el orden exacto de los logs de debug/info puede variar ligeramente dependiendo de la configuración de tu logger, pero la secuencia lógica será):

1. **Primero**, verás el log del `ConfiguradorSkuProductoFactory` (nuestro `BeanFactoryPostProcessor`) indicando que está modificando la `BeanDefinition`. Este log aparecerá *antes* que cualquier log de inicialización de los `Producto`.
2. **Luego**, para cada instancia de `Producto` (Producto 'Laptop Pro' y 'Mouse Inalámbrico'):
    - Verás los logs de "Auditor General" `postProcessBeforeInitialization`.
    - Verás los logs de "Log Detallado" `postProcessBeforeInitialization` (esto confirmaría el orden basado en `Ordered`).
    - Aparecerá el log del `@PostConstruct` de `Producto`, donde la "Laptop Pro" mostrará el `sku` "DEFAULT-SKU-ECOMMERCE", confirmando que el `BeanFactoryPostProcessor` funcionó. El "Mouse Inalámbrico" no tendrá este `sku` modificado ya que nuestro `BeanFactoryPostProcessor` solo apunta a "productoPrincipal".
    - Finalmente, verás los logs de "Auditor General" `postProcessAfterInitialization`.
    - Y los logs de "Log Detallado" `postProcessAfterInitialization` (manteniendo el orden).

Este caso práctico demuestra cómo los puntos de extensión del contenedor de Spring (`BeanFactoryPostProcessor`, `BeanPostProcessor` y la interfaz `Ordered`), junto con la técnica de los métodos `@Bean` estáticos, permiten un control granular y una personalización poderosa sobre el ciclo de vida de los beans en una aplicación Spring Boot.