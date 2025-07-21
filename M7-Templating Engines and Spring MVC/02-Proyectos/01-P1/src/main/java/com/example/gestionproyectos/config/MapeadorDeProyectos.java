package com.example.gestionproyectos.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Clase de configuración para el mapeo de Entidades a DTOs y viceversa.
 * Se utiliza ModelMapper para simplificar este proceso.
 */
@Configuration
public class MapeadorDeProyectos {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // RECOMENDACIÓN: Usar una estrategia de coincidencia estricta.
        // Esto asegura que ModelMapper solo mapee campos que coincidan exactamente
        // en nombre y tipo, evitando mapeos accidentales y ambiguos.
        // Es una configuración más segura y predecible.
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }

}