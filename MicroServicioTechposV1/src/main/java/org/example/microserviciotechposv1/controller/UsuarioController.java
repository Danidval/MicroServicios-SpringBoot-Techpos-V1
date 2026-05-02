package org.example.microserviciotechposv1.controller;

import org.example.microserviciotechposv1.model.Usuario;
import org.example.microserviciotechposv1.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador de Gestión de Usuarios (API REST).
 *
 * Expone los servicios necesarios para la administración de cuentas de usuario,
 * permitiendo operaciones de consulta, registro, edición y eliminación.
 * Adaptado para comunicación asíncrona con React mediante Axios.
 *
 * @author Danid Vallejos
 * @version 1.2
 */
@RestController // Define que los métodos retornarán datos en formato JSON
@RequestMapping("/api/usuarios") // Punto de acceso base para el módulo en el frontend
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Lista todos los usuarios registrados en el sistema.
     * @return Lista de objetos Usuario en formato JSON.
     */
    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.findAll();
    }

    /**
     * Obtiene los detalles de un usuario específico mediante su ID.
     * @param id Identificador único del usuario.
     * @return ResponseEntity con el usuario encontrado o estado 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    /**
     * Procesa la creación de nuevos usuarios o la actualización de existentes.
     * Implementa lógica de seguridad para el manejo de contraseñas cifradas.
     *
     * @param usuario Objeto usuario mapeado desde el JSON de la petición.
     * @param authentication Contexto de seguridad del usuario que realiza la acción.
     * @return ResponseEntity con el resultado de la operación.
     */
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> guardar(@Valid @RequestBody Usuario usuario, Authentication authentication) {
        try {
            // Verificación dinámica de privilegios de administrador para cambios sensibles
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_administrador"));

            if (usuario.getIdUsuario() != null) {
                // --- Lógica de EDICIÓN ---
                Usuario usuarioExistente = usuarioService.findById(usuario.getIdUsuario());
                if (usuarioExistente == null) return ResponseEntity.notFound().build();

                // Si el campo contraseña está vacío en el formulario, se mantiene la actual
                if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
                    usuario.setContrasena(usuarioExistente.getContrasena());
                } else {
                    // Solo el administrador puede realizar cambios de contraseña de otros usuarios
                    if (!isAdmin) {
                        return ResponseEntity.status(403).body(Map.of("message", "No tienes permiso para cambiar contraseñas"));
                    }
                    // Aplicación de hashing Argon2 antes de persistir
                    usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
                }
            } else {
                // --- Lógica de NUEVO REGISTRO ---
                if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("message", "La contraseña es obligatoria para nuevos usuarios"));
                }
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }

            usuarioService.guardar(usuario);
            return ResponseEntity.ok(usuario);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Error al procesar la solicitud"));
        }
    }

    /**
     * Elimina un registro de usuario de la base de datos de forma permanente.
     * @param id Identificador del usuario a eliminar.
     * @return Mensaje de confirmación o error de servidor.
     */
    @DeleteMapping("/{id}") // Sigue la convención de verbos HTTP para estándares REST
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            usuarioService.eliminar(id);
            return ResponseEntity.ok(Map.of("message", "Usuario eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "No se pudo eliminar el usuario"));
        }
    }
}