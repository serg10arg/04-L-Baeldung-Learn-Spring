package com.example.p2.servicio;

import com.example.p2.interfaz.IServicioReportes;
import com.example.p2.modelo.Reporte;
import com.example.p2.modelo.DetalleReporte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Profile("!dev & !qa")
@Service
public class ServicioReportesDefaultImpl implements IServicioReportes {

    private static final Logger REGISTRO = LoggerFactory.getLogger(ServicioReportesDefaultImpl.class);

    public ServicioReportesDefaultImpl(){
        REGISTRO.info("--- Se ha cargado: ServicioReportesDefaultImpl (Perfil DEFAULT - Modo Seguro) ---");
    }

    @Override
    public Reporte generarReporte(String tipo) {
        REGISTRO.info("Generando Reporte: '" + tipo + "' desde la implementacion DEFAULT (datos estatico)...");
        List<DetalleReporte> detalles = new ArrayList<>();
        detalles.add(new DetalleReporte("Item default 1", 10.00));
        detalles.add(new DetalleReporte("Item default 2", 20.00));

        // Concepto 4: Implementación de fallback para un inicio seguro
        return new Reporte("Reporte por defecto - " + tipo, LocalDateTime.now(),
                detalles, "DEFAULT_ESTATICO");
    }
}
