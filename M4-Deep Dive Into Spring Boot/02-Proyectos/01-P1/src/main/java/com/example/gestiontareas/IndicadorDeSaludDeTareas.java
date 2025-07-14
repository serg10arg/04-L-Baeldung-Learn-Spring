package com.example.gestiontareas;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("ServicioDeTareas") // Pila 2: Nombre del nodo en el JSON sera 'servicioDeTareas'
public class IndicadorDeSaludDeTareas implements HealthIndicator {

    private final ServicioDePersistenciaDeTareas servicioDePersistencia;

    public IndicadorDeSaludDeTareas() {
        // En una aplicación real, esto seria inyectado (ej. @Autowired)
        this.servicioDePersistencia = new ServicioDePersistenciaDeTareas();
    }

    @Override
    public Health health() {
        // Pila 2: Logica para verificar el estado de nuestro servicio de persistencia
        if (servicioDePersistencia.estaDisponible()) {
            return Health.up()
                    .withDetail("mensaje", "Servicio de persistencia de tareas esta disponible")
                    .build();
        } else {
            return Health.down()
                    .withDetail("codigoError", 500)
                    .withDetail("razon", "Servicio de persistencia de tareas no disponible")
                    .build();
        }
    }

    // Clase interna simulada para el ejemplo
    private static class ServicioDePersistenciaDeTareas {
        // Este metodo simula el estado de una base de datos o servicio externo.
        // Lo configuramos a 'false' por defecto para ver el estado DOWN en el health check
        public boolean estaDisponible(){
            return false;
        }
    }
}
