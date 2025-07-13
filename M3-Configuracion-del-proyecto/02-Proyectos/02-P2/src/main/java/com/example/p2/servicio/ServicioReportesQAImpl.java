package com.example.p2.servicio;

import com.example.p2.interfaz.IServicioReportes;
import com.example.p2.modelo.DetalleReporte;
import com.example.p2.modelo.Reporte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Profile("qa")
@Service
public class ServicioReportesQAImpl implements IServicioReportes {

    private static final Logger REGISTRO = LoggerFactory.getLogger(ServicioReportesQAImpl.class);

    public ServicioReportesQAImpl() {
        REGISTRO.info("--- Se ha cargado: ServicioReportesQAImpl (Perfil QA - Validación Estructural) ---");
    }

    @Override
    public Reporte generarReporte(String tipo) {
        REGISTRO.info("Generando reporte '" + tipo + "' desde la implementación QA (validando estructura)...");
        // Esta implementación podría realizar validaciones de esquema o formato
        List<DetalleReporte> detalles = Collections.singletonList(
                new DetalleReporte("Datos Validos", 1.0)
        );
        return new Reporte("Reporte de QA - " + tipo, LocalDateTime.now(), detalles, "QA_ESTRUCTURA");
    }
}
