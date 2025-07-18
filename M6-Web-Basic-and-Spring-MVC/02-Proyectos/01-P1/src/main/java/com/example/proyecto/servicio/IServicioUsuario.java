package com.example.proyecto.servicio;

import com.example.proyecto.modelo.Usuario;

import java.util.List;
import java.util.Optional;

public interface IServicioUsuario {

    List<Usuario> encontrarTodosUsuarios();
    Optional<Usuario> encontrarUsuarioPorId(Long id);
    Usuario guardarUsuario(Usuario usuario);
    void eliminarUsuarioPorId(Long id);
    Usuario actualizarUsuario(Long id, Usuario usuario);
}
