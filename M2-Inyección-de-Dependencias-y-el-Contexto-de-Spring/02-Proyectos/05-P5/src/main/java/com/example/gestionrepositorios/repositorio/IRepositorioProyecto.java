package com.example.gestionrepositorios.repositorio;

/**
 * Contrato para un repositorio de proyectos.
 */
public interface IRepositorioProyecto {

    /**
     * Obtiene el identificador único de la instancia del repositorio
     * @return El ID de la instancia.
     */
    String obtenerIdRepositorio();

    /**
     * Simula la ejecución de una operación en el repositorio
     * @param operacion
     */
    void ejecutarOperacion(String operacion);
}
