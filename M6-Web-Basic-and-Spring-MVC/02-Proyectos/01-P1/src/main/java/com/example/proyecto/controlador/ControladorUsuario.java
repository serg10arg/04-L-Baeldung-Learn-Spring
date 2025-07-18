package com.example.proyecto.controlador;


import com.example.proyecto.dto.UsuarioDTO;
import com.example.proyecto.modelo.Usuario;
import com.example.proyecto.excepciones.UsuarioNoEncontradoException;
import com.example.proyecto.servicio.IServicioUsuario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// @RestController es una anotación de conveniencia para APIs RESTful.
// Combina @Controller y @ResponseBody.
@RestController
// @RequestMapping define la URL base para todos los métodos en este controlador.
@RequestMapping("/api/usuarios")
// @CrossOrigin permite solicitudes de origen cruzado, útil para frontends en otros dominios.
@CrossOrigin(origins = "http://localhost:4200") // Ejemplo, ajustar según tu frontend
@RequiredArgsConstructor // MEJORA: Inyección por constructor con Lombok para un código más limpio y robusto.
public class ControladorUsuario {

    // MEJORA: Usar inyección por constructor. Las dependencias son finales,
    // garantizando que el controlador no puede ser instanciado en un estado inválido.
    private final IServicioUsuario servicioUsuario;
    private final ModelMapper modelMapper;

    // GET /api/usuarios
    @GetMapping // CORRECCIÓN: Faltaba la anotación para mapear este método a una solicitud GET.
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosUsuarios() {
        List<Usuario> usuarios = servicioUsuario.encontrarTodosUsuarios();
        // Convierte la lista de entidades a DTOs usando streams y ModelMapper.
        List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDTO); // Retorna 200 OK con la lista de DTOs
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        // MEJORA: Se centraliza el manejo de excepciones. En lugar de lanzar una ResponseStatusException genérica,
        // se lanza la excepción de dominio específica. Spring la capturará y, gracias a @ResponseStatus,
        // la convertirá en una respuesta 404 Not Found.
        Usuario usuario = servicioUsuario.encontrarUsuarioPorId(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id));
        return ResponseEntity.ok(convertirA_DTO(usuario)); // Retorna 200 OK con el DTO del usuario
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        // Convierte el DTO a entidad para guardar en la base de datos.
        Usuario nuevoUsuario = convertirA_Entidad(usuarioDTO);
        Usuario usuarioGuardado = servicioUsuario.guardarUsuario(nuevoUsuario);
        return new ResponseEntity<>(convertirA_DTO(usuarioGuardado), HttpStatus.CREATED); // Retorna 201 Created con el DTO
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioActualizado = servicioUsuario.actualizarUsuario(id, convertirA_Entidad(usuarioDTO));
        return ResponseEntity.ok(convertirA_DTO(usuarioActualizado)); // Retorna 200 OK con el DTO actualizado
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        servicioUsuario.eliminarUsuarioPorId(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

    // Método para convertir de Entidad a DTO.
    private UsuarioDTO convertirA_DTO(Usuario usuario) {
        // ModelMapper.map() hace la mayor parte del trabajo de mapeo de campos.
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    // Método para convertir de DTO a Entidad.
    private Usuario convertirA_Entidad(UsuarioDTO usuarioDTO) {
        // ModelMapper se encarga de todo el mapeo. El ID del DTO se ignora para la creación (JPA lo genera)
        // y la capa de servicio ya usa el ID del Path para la actualización, por lo que no es necesario manejarlo aquí.
        return modelMapper.map(usuarioDTO, Usuario.class);
    }
}
