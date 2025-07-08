package com.example.gestionrepositorios.repositorio.impl;

import com.example.gestionrepositorios.repositorio.IRepositorioProyecto;

// No se anota con @Repository para permitir que la clase @Configuration
// tenga el control total sobre la creación y el ámbito de los beans.
public class RepositorioProyectoImpl implements IRepositorioProyecto {

    private final String idRepositorio;

    public RepositorioProyectoImpl() {
        // Generamos un ID Único basado en el tiempo para cada nueva instancia.
        // Esto nos permite verificar visualmente si dos referencias apuntan al mismo objeto.
        this.idRepositorio = "Repo-" + System.nanoTime();
        System.out.println("DEBUG: Nueva instancia de RepositorioProyectoImpl creada con ID: " + this.idRepositorio);
    }

    @Override
    public String obtenerIdRepositorio() {
        return this.idRepositorio;
    }

    @Override
    public void ejecutarOperacion(String operacion) {
        System.out.println("DEBUG: Ejecutando ' " + operacion + "' en repositorio con ID: " + this.idRepositorio);
    }
}
