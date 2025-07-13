package com.example.p2.servicio;

import com.example.p2.interfaz.IServicioReportes;
import com.example.p2.modelo.DetalleReporte;
import com.example.p2.modelo.Reporte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Profile("prod")
@Service
public class ServicioReportesProdImpl implements IServicioReportes {

    private static final Logger REGISTRO = LoggerFactory.getLogger(ServicioReportesProdImpl.class);

    public ServicioReportesProdImpl() {
        REGISTRO.info("--- Se ha cargado: ServicioReportesProdImpl (Perfil PROD - Base de Datos Real) ---");
    }

    @Override
    public Reporte generarReporte(String tipo) {
        REGISTRO.info("Generando reporte '" + tipo + "' desde la implementación PROD (consultando base de datos real)...");
        // Aquí iría la lógica real para consultar la base de datos de producción
        List<DetalleReporte> detalles = new ArrayList<>();
        detalles.add(new DetalleReporte("Ventas Totales", 150000.50));
        detalles.add(new DetalleReporte("Usuarios Activos", 25000.00));
        detalles.add(new DetalleReporte("Ingresos Netos", 75000.00));

        // Concepto 4: Implementación diferente para el entorno de producción
        return new Reporte("Reporte de produccion - " + tipo, LocalDateTime.now(),
                detalles, "PROD_BASE_DATOS");
    }

}
