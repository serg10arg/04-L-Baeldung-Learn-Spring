package com.example.proyecto.repositorio;

import com.example.proyecto.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {

    Usuario findByNombre(String nombre);

}
