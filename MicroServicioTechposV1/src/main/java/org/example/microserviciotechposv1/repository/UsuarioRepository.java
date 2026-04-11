package org.example.microserviciotechposv1.repository;

import org.example.microserviciotechposv1.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio de Persistencia para la entidad Usuario.
 * Proporciona los métodos necesarios para realizar operaciones CRUD y consultas
 * personalizadas en la base de datos utilizando Spring Data JPA.
 * * Cumple con el requerimiento de integración de herramientas para almacenamiento de datos.
 * * @author [Tu Nombre/Aprendiz]
 * @version 1.0
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Realiza una búsqueda de usuario por su nombre de acceso (login).
     * Este método es fundamental para el proceso de autenticación de Spring Security.
     * * @param usuario El nombre de usuario (String) a buscar.
     * @return Un objeto Optional que contiene el Usuario si se encuentra en la base de datos.
     */
    Optional<Usuario> findByUsuario(String usuario);
}