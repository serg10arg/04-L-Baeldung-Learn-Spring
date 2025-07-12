package com.example.gestionrepositorios.config;

import com.example.gestionrepositorios.repositorio.IRepositorioProyecto;
import com.example.gestionrepositorios.repositorio.impl.RepositorioProyectoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ConfiguracionApp {

    /**
     * Define un bean para el repositorio compartido
     * El ámbito "singleton" es el predeterminado en Spring, lo que significa
     * que se creara una única instancia de este bean para toda la aplicación.
     * Le damos un nombre explícito para usarlo con @Qualifier.
     */
    @Bean("repositorioCompartido")
    // @Scope("singleton") // Esta anotación es opcional, ya que es el ámbito por defecto
    public IRepositorioProyecto repositorioProyectoCompartido() {
        System.out.println("INFO: Definiendo bean 'repositorioCompartido' (singleton)...");
        return new RepositorioProyectoImpl();
    }

    /**
     * Define un bean para el repositorio temporal.
     * El ámbito "prototype" le indica a Spring que debe crear una
     * nueva instancia de este bean cada vez que se solicite su inyección.
     */
    @Bean("repositorioTemporal")
    @Scope("prototype")
    public IRepositorioProyecto repositorioProyectoTemporal() {
        System.out.println("INFO: Definiendo bean 'repositorioTemporal' (Prototype)...");
        return new RepositorioProyectoImpl();
    }
}
