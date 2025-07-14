***
# Proyecto Demo: Configuración con Perfiles de Spring Boot

Este proyecto es una demostración práctica y robusta de cómo utilizar **Perfiles de Spring Boot** para gestionar configuraciones específicas para diferentes entornos (desarrollo y producción). Muestra cómo las propiedades, los niveles de log y el comportamiento de la aplicación pueden cambiar dinámicamente según el perfil activo.

-----

## 🎯 Objetivo Principal

El objetivo es ilustrar cómo una única base de código puede comportarse de manera diferente en entornos de desarrollo (`dev`) y producción (`prod`) mediante la externalización de la configuración, una práctica fundamental en el desarrollo de software moderno.

-----

## ✨ Características Principales

* **Configuración por Perfiles**: Uso de archivos `application-dev.properties` y `application-prod.properties` para definir valores distintos.
* **Inyección de Valores**: Inyección de propiedades en los servicios de Spring usando la anotación `@Value`.
* **Logging Avanzado y Consciente del Perfil**: Configuración de `logback-spring.xml` para ajustar los niveles de log y los destinos (consola, archivo) según el perfil activo.
* **API RESTful**: Exposición de la funcionalidad a través de endpoints REST creados con Spring MVC.
* **Pruebas de Integración Aisladas**: Pruebas de integración dedicadas para cada perfil (`@ActiveProfiles`) que garantizan el comportamiento esperado en cada entorno.

-----

## 🛠️ Prerrequisitos

Para compilar y ejecutar este proyecto, necesitarás:

* Java JDK 17 o superior.
* Apache Maven 3.6+

-----

## ⚙️ Configuración

La magia de este proyecto reside en cómo gestiona la configuración.

### Archivos de Propiedades

* **`src/main/resources/application-dev.properties`**: Contiene las propiedades para el entorno de desarrollo.

  ```properties
  # Propiedades para el perfil 'dev'
  tarea.id.prefijo=DEV_ID
  tarea.id.sufijo=ENTORNO_DEV
  ```

* **`src/main/resources/application-prod.properties`**: Contiene las propiedades para el entorno de producción.

  ```properties
  # Propiedades para el perfil 'prod'
  tarea.id.prefijo=PROD_ID_APP
  tarea.id.sufijo=PROD_ENV
  # Nivel de log menos verboso para prod
  logging.level.com.baeldung.gestortareas=INFO
  ```

### Configuración de Logging

El archivo `src/main/resources/logback-spring.xml` utiliza la etiqueta `<springProfile>` para aplicar diferentes configuraciones de logging:

* **Perfil `prod`**: El nivel de log raíz se establece en `INFO` para reducir el "ruido" en producción.
* **Cualquier otro perfil (ej. `dev`)**: El nivel de log raíz se establece en `DEBUG` para obtener información detallada durante el desarrollo.

-----

## 📊 Diagrama de Flujo de la Aplicación

El siguiente diagrama ilustra el flujo de una petición para generar un ID de tarea y cómo el perfil de Spring activo influye en el resultado.

```mermaid
graph TD
    subgraph "Cliente"
        A[Usuario/Cliente]
    end

    subgraph "Aplicación Spring Boot"
        B[Endpoint GET /tarea/generar-id/{nombre}]
        C[ControladorTareas]
        D[ServicioTareas]
        E{¿Perfil 'prod' activo?}
        F[Cargar application-prod.properties]
        G[Cargar application-dev.properties]
        H["@Value prefijo = 'PROD_ID_APP'<br/>@Value sufijo = 'PROD_ENV'"]
        I["@Value prefijo = 'DEV_ID'<br/>@Value sufijo = 'ENTORNO_DEV'"]
        J[Generar ID de Tarea]
        K[Respuesta HTTP con ID]
    end

    A -- Petición HTTP --> B
    B --> C
    C -- Llama a generarIdDeTarea() --> D
    D -- Comprueba el perfil de Spring --> E
    E -- Sí --> F
    E -- No --> G
    F --> H
    G --> I
    H -- Inyecta valores --> J
    I -- Inyecta valores --> J
    J -- Devuelve ID --> C
    C --> K
    K -- Envía respuesta --> A
```

-----

## 🚀 Ejecución y Pruebas

### 1\. Pruebas Automatizadas (Método Recomendado)

El proyecto incluye una suite de pruebas de integración que valida el comportamiento de la aplicación para cada perfil de forma aislada.

Para ejecutar las pruebas, abre una terminal en la raíz del proyecto y ejecuta:

```bash
mvn clean test
```

Maven compilará el código y ejecutará las clases `ControladorTareasDevIntegracionTest` y `ControladorTareasProdIntegracionTest`, verificando que cada perfil carga la configuración correcta.

### 2\. Ejecución Manual

Puedes ejecutar la aplicación manualmente para interactuar con ella a través de sus endpoints.

**Paso 1: Empaquetar la aplicación**

```bash
mvn clean package
```

Este comando generará un archivo `.jar` ejecutable en el directorio `target/`.

**Paso 2: Ejecutar con un perfil específico**

* Para ejecutar con el perfil de desarrollo (`dev`):
  ```bash
  java -jar target/gestortareas-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
  ```
* Para ejecutar con el perfil de producción (`prod`):
  ```bash
  java -jar target/gestortareas-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
  ```

-----

## 🔌 Uso de la API

Una vez que la aplicación esté en ejecución, puedes interactuar con los siguientes endpoints usando un navegador o una herramienta como `curl`.

### Endpoint 1: Generar ID de Tarea

Genera un ID de tarea único basado en el prefijo y sufijo del perfil activo.

* **URL:** `GET /tarea/generar-id/{nombre}`
* **Ejemplo con `curl`:**
  ```bash
  curl http://localhost:8080/tarea/generar-id/MiPrimeraTarea
  ```

**Respuesta esperada (perfil `dev`):**

```text
DEV_ID-miprimeratarea-ENTORNO_DEV
```

**Respuesta esperada (perfil `prod`):**

```text
PROD_ID_APP-miprimeratarea-PROD_ENV
```

### Endpoint 2: Obtener Configuración Actual

Devuelve el prefijo y sufijo que se están utilizando actualmente.

* **URL:** `GET /tarea/configuracion`
* **Ejemplo con `curl`:**
  ```bash
  curl http://localhost:8080/tarea/configuracion
  ```

**Respuesta esperada (perfil `dev`):**

```text
Prefijo: DEV_ID, Sufijo: ENTORNO_DEV
```

**Respuesta esperada (perfil `prod`):**

```text
Prefijo: PROD_ID_APP, Sufijo: PROD_ENV
```