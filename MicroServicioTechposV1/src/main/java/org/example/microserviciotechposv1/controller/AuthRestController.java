package org.example.microserviciotechposv1.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.microserviciotechposv1.model.Usuario;
import org.example.microserviciotechposv1.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST encargado de la autenticación de usuarios.
 *
 * Procesa las solicitudes de inicio de sesión, gestiona la persistencia de la
 * sesión mediante el repositorio de contexto de seguridad y retorna el perfil
 * de usuario necesario para el estado global de la aplicación en React.
 *
 * @author Danid Vallejos
 * @version 1.1
 */
@RestController
@RequestMapping("/api/auth")
// Configuración de CORS específica para permitir el flujo de credenciales (cookies) desde React
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Inyección del repositorio de contexto.
     * Esencial para persistir la sesión en el servidor y generar la cookie JSESSIONID.
     */
    @Autowired
    private SecurityContextRepository securityContextRepository;

    /**
     * Endpoint de inicio de sesión.
     *
     * @param loginRequest Objeto DTO con credenciales (usuario y contraseña).
     * @param request Petición HTTP para obtener la sesión actual.
     * @param response Respuesta HTTP para adjuntar la cookie de sesión.
     * @return Perfil del usuario autenticado o error 401.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        try {
            // 1. Ejecución de la autenticación mediante el Manager configurado con Argon2
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // 2. Creación manual del contexto de seguridad para el usuario autenticado
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // 3. PERSISTENCIA MANUAL: Guarda el contexto en el repositorio.
            // Este paso es el que genera la cookie de sesión que Axios recibirá y enviará de vuelta.
            securityContextRepository.saveContext(context, request, response);

            // 4. Recuperación de los detalles del usuario desde la base de datos
            Usuario usuario = usuarioRepository.findByUsuario(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 5. Construcción de la respuesta simplificada para el almacenamiento en el frontend
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("id", usuario.getIdUsuario());
            responseBody.put("nombre", usuario.getNombre());
            responseBody.put("usuario", usuario.getUsuario());
            responseBody.put("rol", usuario.getRol().name()); // Envía el nombre del Enum (ej: "administrador")
            responseBody.put("activo", usuario.getActivo());

            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            // Retorna 401 Unauthorized si las credenciales fallan o el usuario no existe
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
        }
    }
}