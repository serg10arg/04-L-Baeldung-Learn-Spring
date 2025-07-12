package com.example.gestionrepositorios.servicio.impl;

import com.example.gestionrepositorios.repositorio.IRepositorioProyecto;
import com.example.gestionrepositorios.servicio.IServicioProyecto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ServicioProyectoImpl implements IServicioProyecto {

    // Dependencias que serán inyectadas por Spring.
    // Se hacen 'final' para promover la inmutabilidad, una buena práctica.
    private final IRepositorioProyecto repoSingleton1;
    private final IRepositorioProyecto repoSingleton2;
    private final IRepositorioProyecto repoPrototipo1;
    private final IRepositorioProyecto repoPrototipo2;

    /**
     * Inyecccion por constructor (práctica recomendada).
     * Spring inyectará las dependencias basándose en los @Qualifier.
     */
    public ServicioProyectoImpl(
            @Qualifier("repositorioCompartido") IRepositorioProyecto repoSingleton1,
            @Qualifier("repositorioCompartido") IRepositorioProyecto repoSingleton2,
            @Qualifier("repositorioTemporal") IRepositorioProyecto repoPrototipo1,
            @Qualifier("repositorioTemporal") IRepositorioProyecto repoPrototipo2) {
        this.repoSingleton1 = repoSingleton1;
        this.repoSingleton2 = repoSingleton2;
        this.repoPrototipo1 = repoPrototipo1;
        this.repoPrototipo2 = repoPrototipo2;
    }

    /**
     * Este metodo se ejecuta automáticamente después de que el bean ha sido construido
     * y todas sus dependencias han sido inyectadas. Es el lugar perfecto para
     * verificar el estado inicial o realizar tareas de inicialización
     */
    @PostConstruct
    public void verificarInstancias() {
        System.out.println("\n--- VERIFICACIÓN DE ÁMBITOS DE BEAN (@PostConstruct) ---");

        // Verificación de beans Singleton
        System.out.println("\n--- Instancias Singleton ('repositorioCompartido') ---");
        System.out.println("  ID de repoSingleton1: " + repoSingleton1.obtenerIdRepositorio());
        System.out.println("  ID de repoSingleton2: " + repoSingleton2.obtenerIdRepositorio());
        System.out.println("  ¿Son el mismo objeto? -> " + (repoSingleton1 == repoSingleton2)); // Se espera 'true'
        repoSingleton1.ejecutarOperacion("Operación Global A");

        // Verificación de beans Prototype
        System.out.println("\n--- Instancias Prototype ('repositorioTemporal') ---");
        System.out.println("  ID de repoPrototipo1: " + repoPrototipo1.obtenerIdRepositorio());
        System.out.println("  ID de repoPrototipo2: " + repoPrototipo2.obtenerIdRepositorio());
        System.out.println("  ¿Son el mismo objeto? -> " + (repoPrototipo1 == repoPrototipo2)); // Se espera 'false'
        repoPrototipo1.ejecutarOperacion("Tarea Aislada X");
        repoPrototipo2.ejecutarOperacion("Tarea Aislada Y");

        System.out.println("\n--- FIN DE LA VERIFICACIÓN ---");
    }
}
