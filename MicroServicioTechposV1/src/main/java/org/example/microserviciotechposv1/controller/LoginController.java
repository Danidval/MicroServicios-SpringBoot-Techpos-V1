package org.example.microserviciotechposv1.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador encargado de la gestión de rutas de acceso y visualización del panel principal.
 * Maneja la interacción entre el motor de plantillas y el contexto de seguridad.
 * * @author Danid Vallejos
 * @version 1.0
 */
@Controller
public class LoginController {

    /**
     * Mapea la petición GET para la página de inicio de sesión.
     * @return El nombre de la plantilla HTML "login".
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Gestiona el acceso al panel de control (Dashboard).
     * Recupera información del usuario autenticado para personalizar la interfaz.
     * * @param model Objeto para pasar datos a la vista (Frontend).
     * @param authentication Objeto que contiene los detalles del usuario que inició sesión.
     * @return El nombre de la plantilla HTML "dashboard".
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        // Inyectamos el nombre de usuario en el modelo para mostrarlo en la interfaz
        model.addAttribute("username", authentication.getName());

        /* * Lógica para extraer el rol del usuario:
         * Se obtiene la primera autoridad (rol) de la colección y se envía como atributo.
         * Esto permite realizar renderizado condicional en el frontend basado en permisos.
         */
        String rolActual = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("rol", rolActual); // Ejemplo: "ROLE_administrador"

        return "dashboard";
    }
}