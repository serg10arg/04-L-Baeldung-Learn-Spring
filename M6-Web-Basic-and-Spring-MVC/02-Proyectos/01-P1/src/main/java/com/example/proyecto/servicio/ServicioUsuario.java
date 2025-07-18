package com.example.proyecto.servicio;

import com.example.proyecto.excepciones.UsuarioNoEncontradoException;
import com.example.proyecto.modelo.Usuario;
import com.example.proyecto.repositorio.RepositorioUsuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioUsuario implements IServicioUsuario { // CORRECCIÓN: Se implementa una interfaz, no se extiende.

    private final RepositorioUsuario repositorioUsuario;

    public ServicioUsuario(RepositorioUsuario repositorioUsuario) {
        // CORRECCIÓN CRÍTICA: Se debe asignar al campo de la clase (this.repositorioUsuario).
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    @Transactional(readOnly = true) // MEJORA: Indica que es una transacción de solo lectura para optimizar.
    public List<Usuario> encontrarTodosUsuarios() {
        return repositorioUsuario.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> encontrarUsuarioPorId(Long id) {
        return repositorioUsuario.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
        return repositorioUsuario.save(usuario);
    }

    @Override
    @Transactional
    public void eliminarUsuarioPorId(Long id) {
        // MEJORA: Verificar si el usuario existe antes de intentar borrarlo para poder manejar el error.
        if (!repositorioUsuario.existsById(id)) {
            throw new UsuarioNoEncontradoException("No se puede eliminar. Usuario no encontrado con ID: " + id);
        }
         repositorioUsuario.deleteById(id);
    }

    @Override
    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        return repositorioUsuario.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
            usuarioExistente.setCorreoElectronico(usuarioActualizado.getCorreoElectronico());
            return repositorioUsuario.save(usuarioExistente);
            // MEJORA: Usar una excepción específica en lugar de una genérica.
        }).orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id));
    }
}
