package com.example.p2;

import com.example.p2.interfaz.IServicioReportes;
import com.example.p2.modelo.Reporte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class GestorReportesAplicacion implements CommandLineRunner {

    private static final Logger REGISTRO = LoggerFactory.getLogger(GestorReportesAplicacion.class);
    private final IServicioReportes servicioReportes;
    private final Environment ambiente;

    public GestorReportesAplicacion(IServicioReportes servicioReportes, Environment ambiente){
        this.servicioReportes = servicioReportes;
        this.ambiente = ambiente;
    }

    @Override
    public void run(String... args) throws Exception {
        REGISTRO.info("\n--- Iniciando Gestor de Reportes ---");

        // Concepto 5: Acceder y mostrar información de perfiles
        REGISTRO.info("Perfiles activos en la aplicacion: " + Arrays.toString(ambiente.getActiveProfiles()));
        REGISTRO.info("Perfile por defecto en spring: " + Arrays.toString(ambiente.getDefaultProfiles()));
        REGISTRO.info("--- Información de Perfiles Finalizada ---\n");

        // Generar un reporte usando el servicio inyectado, que será específico del perfil activo
        Reporte miReporte = servicioReportes.generarReporte("Manual de ventas");
        REGISTRO.info("\nReporte Generado: " + miReporte.getTitulo());
        REGISTRO.info("Generado por: " + miReporte.getGenerador());
        miReporte.getDetalles().forEach(detalle -> REGISTRO.info(" - " + detalle.getItem() + ": "
                + detalle.getValor()));

        REGISTRO.info("\n--- Gestor de Reportes Finalizado ---");
    }
}
