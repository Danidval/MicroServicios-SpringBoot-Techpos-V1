package org.example.microserviciotechposv1.controller;

import org.example.microserviciotechposv1.model.Usuario;
import org.example.microserviciotechposv1.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de exponer la lógica de negocio como un servicio de datos.
 * A diferencia de un controlador tradicional, este componente no devuelve vistas HTML,
 * sino recursos en formato JSON, cumpliendo con los estándares de un Microservicio.
 * * @author Danid Vallejos
 * @version 1.0
 */
@RestController
@RequestMapping("/api/usuarios") // Ruta base para el acceso a recursos de la API
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint que recupera la colección completa de usuarios del sistema.
     * Spring Boot utiliza Jackson internamente para serializar la lista de objetos
     * Java a una respuesta estándar en formato JSON.
     * * @return List<Usuario> Colección de usuarios convertida automáticamente a JSON.
     */
    @GetMapping
    public List<Usuario> listarUsuarios() {
        /*
         * Se invoca la capa de servicio para obtener los datos.
         * Este enfoque desacopla la presentación de los datos de la lógica de persistencia.
         */
        return usuarioService.findAll();
    }
}