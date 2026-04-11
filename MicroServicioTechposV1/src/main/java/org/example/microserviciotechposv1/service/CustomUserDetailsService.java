package org.example.microserviciotechposv1.service;

import org.example.microserviciotechposv1.model.Usuario;
import org.example.microserviciotechposv1.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

/**
 * Servicio personalizado para la gestión de detalles de usuario.
 * Implementa UserDetailsService para integrar la lógica de autenticación de Spring Security
 * con la base de datos del proyecto TechPOS.
 * * @author [Tu Nombre/Aprendiz]
 * @version 1.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Localiza al usuario en la base de datos mediante su nombre de usuario.
     * Transforma la entidad 'Usuario' en un objeto 'UserDetails' que Spring Security puede procesar.
     * * @param username El nombre de usuario introducido en el formulario de login.
     * @return UserDetails objeto que contiene las credenciales y roles del usuario.
     * @throws UsernameNotFoundException Si el usuario no existe en la persistencia.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Búsqueda del usuario utilizando el repositorio JPA integrado [cite: 9]
        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario: " + username));

        /* * Retorna una instancia de User (clase de Spring Security).
         * Se antepone "ROLE_" al nombre del rol para cumplir con la convención
         * de autoridades de Spring Security (hasRole).
         * La contraseña recuperada es el hash Argon2 guardado en la DB[cite: 9].
         */
        return new User(
                usuario.getUsuario(),
                usuario.getContrasena(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }
}