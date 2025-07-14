***
# Proyecto Demo: Profundizando en Spring Boot Actuator

Este proyecto es una demostración técnica y detallada de las capacidades de **Spring Boot Actuator**. Su objetivo es ilustrar cómo monitorear, gestionar y extender una aplicación Spring Boot utilizando las potentes herramientas que Actuator proporciona.

-----

## 🎯 Objetivo Principal

El propósito de este proyecto es servir como una guía práctica para entender y configurar características avanzadas de Spring Boot Actuator, incluyendo:

* Creación de indicadores de salud (`HealthIndicator`) personalizados.
* Personalización del endpoint de información (`/info`).
* Anulación de la auto-configuración de Spring Boot.
* Configuración avanzada de los endpoints de Actuator (rutas, exposición, etc.).

-----

## ✨ Características Demostradas

1.  **Indicador de Salud Personalizado**: Se implementa un `HealthIndicator` que simula la verificación de un servicio de persistencia externo, mostrando cómo enriquecer el endpoint `/health` con chequeos de negocio específicos.
2.  **Endpoint de Información a Medida**: El endpoint `/info` se personaliza a través de `application.properties` para exponer metadatos relevantes de la aplicación, como su nombre y descripción.
3.  **Anulación de Auto-Configuración**: Se demuestra cómo un bean definido por el usuario (un `ObjectMapper`) puede anular la configuración por defecto de Spring Boot. El reporte de auto-configuración se habilita para hacer visible este comportamiento.
4.  **Configuración Avanzada de Endpoints**:
    * Se cambia la ruta base de todos los endpoints de Actuator a `/monitoreo`.
    * Se mapea el endpoint `/info` a una ruta personalizada `/informacion`.
    * Se exponen explícitamente los endpoints `/health`, `/info` y `/loggers`.

-----

## 📊 Diagrama de Flujo de Actuator

El siguiente diagrama ilustra cómo Actuator se integra con los componentes personalizados y la configuración de la aplicación para responder a las peticiones de monitoreo.

```mermaid
graph TD
    subgraph "Usuario (Operador/Desarrollador)"
        U[Usuario]
    end

    subgraph "Aplicación Spring Boot"
        P[application.properties]
        A[Actuator Core]
        HI[IndicadorDeSaludDeTareas]
        INFO[InfoContributor (desde properties)]
    end

    P -- Configura rutas y exposición --> A
    P -- Provee datos para --> INFO

    HI -- Se registra con --> A
    INFO -- Se registra con --> A

    U -- "GET /monitoreo/health" --> A
    A -- Invoca HealthIndicator --> HI
    HI -- "Retorna estado DOWN" --> A
    A -- "Respuesta JSON (con detalles)" --> U

    U -- "GET /monitoreo/informacion" --> A
    A -- Invoca InfoContributor --> INFO
    INFO -- "Retorna info personalizada" --> A
    A -- "Respuesta JSON (con info del sistema)" --> U
```

-----

## 🛠️ Prerrequisitos

Para compilar y ejecutar este proyecto, necesitarás:

* Java JDK 17 o superior.
* Apache Maven 3.6+

-----

## 🚀 Cómo Ejecutar la Aplicación

1.  Clona este repositorio.
2.  Abre una terminal en el directorio raíz del proyecto.
3.  Ejecuta el siguiente comando de Maven para iniciar la aplicación:
    ```bash
    mvn spring-boot:run
    ```

La aplicación se iniciará en `http://localhost:8080`.

-----

## 🔬 Explorando los Endpoints de Actuator

Una vez que la aplicación esté en ejecución, puedes usar un navegador o una herramienta como `curl` para explorar los endpoints personalizados.

### 1\. Endpoint de Salud (`/health`)

Este endpoint muestra el estado de salud agregado de la aplicación, incluyendo nuestro indicador personalizado `ServicioDeTareas`.

* **URL:** `http://localhost:8080/monitoreo/health`
* **Comando `curl`:**
  ```bash
  curl -X GET http://localhost:8080/monitoreo/health
  ```
* **Respuesta Esperada:** Debido a que nuestro `IndicadorDeSaludDeTareas` está configurado para simular una falla, el estado general será `DOWN`. Gracias a `management.endpoint.health.show-details=ALWAYS`, veremos los detalles completos:
  ```json
  {
    "status": "DOWN",
    "components": {
      "diskSpace": {
        "status": "UP",
        "details": {
          "total": 250790412288,
          "free": 101191639040,
          "threshold": 10485760,
          "exists": true
        }
      },
      "ping": {
        "status": "UP"
      },
      "servicioDeTareas": {
        "status": "DOWN",
        "details": {
          "codigoError": 500,
          "razon": "Servicio de persistencia de tareas no disponible"
        }
      }
    }
  }
  ```

### 2\. Endpoint de Información (`/info`)

Este endpoint muestra la información personalizada definida en `application.properties`.

* **URL:** `http://localhost:8080/monitoreo/informacion`
* **Comando `curl`:**
  ```bash
  curl -X GET http://localhost:8080/monitoreo/informacion
  ```
* **Respuesta Esperada:**
  ```json
  {
    "sistema": {
      "nombre": "Sistema de Gestion de Tareas",
      "descripcion": "Aplicacion para organizar tareas diarias"
    }
  }
  ```

### 3\. Endpoint de Loggers (`/loggers`)

Permite ver y modificar los niveles de log de la aplicación en tiempo de ejecución.

* **URL:** `http://localhost:8080/monitoreo/loggers`

-----

## 💡 Verificando la Anulación de Auto-Configuración

Para confirmar que nuestro bean `miObjectMapperPersonalizado` ha anulado la configuración por defecto de Spring Boot, revisa los logs de la consola al iniciar la aplicación.

Gracias a la propiedad `logging.level.org.springframework.boot.autoconfigure=DEBUG`, verás el "Auto-configuration report". Busca la sección de `JacksonAutoConfiguration`. Deberías encontrar una condición que indica que la auto-configuración no se aplicó porque ya existía un bean de `ObjectMapper`.

**Ejemplo de lo que verás en el log:**

```log
JacksonAutoConfiguration:
    Did not match:
        - @ConditionalOnMissingBean (types: com.fasterxml.jackson.databind.ObjectMapper; SearchStrategy: all) found beans 'miObjectMapperPersonalizado' (OnBeanCondition)
```

Esto confirma que nuestra personalización ha tomado precedencia, demostrando un concepto clave del funcionamiento interno de Spring Boot.