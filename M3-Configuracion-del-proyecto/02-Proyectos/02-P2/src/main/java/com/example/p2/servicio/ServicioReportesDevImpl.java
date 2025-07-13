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
import java.util.Arrays;
import java.util.List;


@Profile("dev")
@Service
public class ServicioReportesDevImpl implements IServicioReportes {

    private static final Logger REGISTRO = LoggerFactory.getLogger(ServicioReportesDevImpl.class);

    public ServicioReportesDevImpl() {
        REGISTRO.info("--- SE HA CARGADO: ServicioReporteDevImpl (Perfil DEV - En Memoria ---");
    }

    @Override
    public Reporte generarReporte(String tipo) {
        REGISTRO.info("GENERANDO REPORTE: '" + tipo + "' desde la implementacion DEV(datos en memoria)...");
        List<DetalleReporte> detalles = new ArrayList<>();
        detalles.add(new DetalleReporte("Elemento A", 100.50));
        detalles.add(new DetalleReporte("Elemento B", 200.00));
        detalles.add(new DetalleReporte("Elemento C", 300.25));

        // Concepto 4: Implementación diferente para el entorno de desarrollo
        return new Reporte("Reporte de DESARROLLO - " + tipo, LocalDateTime.now(),
                detalles, "DEV_MEMORIA");

    }
}
