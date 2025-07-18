package com.example.proyecto.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration le indica a Spring que esta clase contiene definiciones de beans.
@Configuration
public class AppConfig {

    // @Bean le dice a Spring que este método produce un bean para ser gestionado por el contenedor de Spring.
    // El nombre del método (modelMapper) es el nombre por defecto del bean.
    @Bean
    public ModelMapper modelMapper() {

        //Usar una estrategia de coincidencia estricta
        // Esto ayuda a ModelMapper a mapear correctamente entre clases POJO
        // y Records de Java, ya que se basa en los nombres de las propiedades
        // en lugar de depender estrictamente de los metodos get/set
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}