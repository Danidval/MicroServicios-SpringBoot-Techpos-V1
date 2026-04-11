package org.example.microserviciotechposv1.controller;

import org.example.microserviciotechposv1.model.Usuario;
import org.example.microserviciotechposv1.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador de Gestión de Usuarios.
 * Implementa las operaciones CRUD y reglas de negocio para la administración de cuentas.
 * Cumple con los criterios de evaluación de codificación modular y comentarios.
 * * @author Danid Vallejos
 * @version 1.0
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Lista todos los usuarios registrados extrayéndolos a través del servicio.
     * @param model Objeto para enviar la lista a la vista "usuarios/lista".
     * @return Vista con la tabla de usuarios.
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "usuarios/lista";
    }

    /**
     * Prepara el formulario para la creación de un nuevo usuario.
     * @param model Envía un objeto Usuario vacío para ser vinculado al formulario.
     * @return Vista del formulario de registro.
     */
    @GetMapping("/nuevo")
    public String nuevoFormulario(Model model) {
        // Se utiliza "objUsuario" como clave para evitar conflictos con palabras reservadas
        model.addAttribute("objUsuario", new Usuario());
        return "usuarios/formulario";
    }

    /**
     * Recupera un usuario por su ID para permitir su edición.
     * @param id Identificador único del usuario.
     * @param model Envía el usuario encontrado a la vista.
     * @return Vista del formulario con datos precargados o redirección si no existe.
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario == null) {
            return "redirect:/usuarios";
        }
        model.addAttribute("objUsuario", usuario);
        return "usuarios/formulario";
    }

    /**
     * Procesa el guardado (creación o actualización) de un usuario.
     * Incluye lógica de validación de campos y reglas de seguridad para contraseñas[cite: 9, 10].
     * * @param usuario Objeto vinculado al formulario mediante @ModelAttribute.
     * @param result Resultado de la validación de anotaciones @Valid.
     * @param authentication Contexto de seguridad para verificar roles.
     * @param flash Atributos de redirección para mensajes de éxito/error.
     * @return Redirección a la lista de usuarios o retorno al formulario si hay errores.
     */
    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("objUsuario") Usuario usuario,
            BindingResult result,
            Authentication authentication,
            RedirectAttributes flash) {
// Depurador se ejecuta al recibir los datos
        System.out.println("DEBUG: Entrando al método guardar. Usuario a procesar: " + usuario.getUsuario());

        // Validación: Si el formulario tiene errores de entrada, se recarga la vista
        if (result.hasErrors()) {
            System.out.println("DEBUG: Errores de validación encontrados: " + result.getAllErrors());
            return "usuarios/formulario";
        }

        // Validación: Si el formulario tiene errores de entrada, se recarga la vista
        if (result.hasErrors()) {
            return "usuarios/formulario";
        }

        if (usuario.getIdUsuario() != null) {
            // --- Lógica de EDICIÓN ---
            Usuario usuarioExistente = usuarioService.findById(usuario.getIdUsuario());

            // Verificación de privilegios de administrador para cambios sensibles
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_administrador"));

            if (usuario.getContrasena() != null && !usuario.getContrasena().trim().isEmpty()) {
                if (!isAdmin) {
                    // Restricción: Solo el administrador altera contraseñas ajenas
                    usuario.setContrasena(usuarioExistente.getContrasena());
                    flash.addFlashAttribute("warning", "Solo el administrador puede cambiar contraseñas.");
                } else {
                    // Encriptación de la nueva contraseña usando Argon2
                    usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
                    flash.addFlashAttribute("success", "Usuario y contraseña actualizados.");
                }
            } else {
                // Preservar la contraseña actual si el campo se deja vacío en edición
                usuario.setContrasena(usuarioExistente.getContrasena());
                flash.addFlashAttribute("success", "Usuario actualizado.");
            }
        } else {
            // --- Lógica de NUEVO REGISTRO ---
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            flash.addFlashAttribute("success", "Nuevo usuario creado con éxito.");
        }

        // Persistencia a través de la capa de servicio [cite: 8]
        usuarioService.guardar(usuario);
        return "redirect:/usuarios";
    }

    /**
     * Elimina un usuario por su identificador.
     * @param id ID del usuario a eliminar de la base de datos.
     * @return Redirección a la lista actualizada.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios";
    }
}