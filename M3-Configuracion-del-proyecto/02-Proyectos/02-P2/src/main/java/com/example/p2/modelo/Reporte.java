package com.example.p2.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {
    private String titulo;
    private LocalDateTime fechaGeneracion;
    private List<DetalleReporte> detalles;
    private String generador; // para indicar que implementación genero el reporte

}
