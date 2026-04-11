package org.example.microserviciotechposv1.service;

import org.example.microserviciotechposv1.model.Usuario;
import org.example.microserviciotechposv1.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Servicio de Negocio para la gestión de Usuarios.
 * Proporciona métodos para la manipulación de datos, aplicando reglas de seguridad
 * y gestión de transacciones.
 * * @author [Tu Nombre/Aprendiz]
 * @version 1.0
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Guarda o actualiza un usuario en la base de datos.
     * Incluye una validación de seguridad para asegurar que la contraseña se guarde
     * con el hash correcto (Argon2) y evitar re-encriptaciones innecesarias.
     * * @param usuario Objeto usuario con los datos a persistir.
     * @return El usuario guardado con sus datos actualizados.
     */
    @Transactional
    public Usuario guardar(Usuario usuario) {
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            String pass = usuario.getContrasena();

            // Verificación del formato del hash para evitar encriptar un hash existente.
            // Los hashes de Argon2 en Spring Security suelen iniciar con estos prefijos.
            boolean yaTieneHash = pass.startsWith("$argon2id$") ||
                    pass.startsWith("{argon2}");

            if (!yaTieneHash) {
                // Se realiza el proceso de encriptación solo si la contraseña llega en texto plano
                usuario.setContrasena(passwordEncoder.encode(pass));
            }
        }
        return usuarioRepository.save(usuario);
    }

    /**
     * Recupera la lista completa de usuarios registrados.
     * @return List de entidades Usuario.
     */
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario específico por su identificador único.
     * @param id Identificador del usuario.
     * @return El usuario encontrado.
     * @throws RuntimeException Si el ID no corresponde a ningún registro.
     */
    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Usuario no encontrado"));
    }

    /**
     * Elimina un registro de usuario de la base de datos por su ID.
     * @param id Identificador del registro a remover.
     */
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }
}