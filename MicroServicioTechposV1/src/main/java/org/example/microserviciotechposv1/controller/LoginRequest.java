package org.example.microserviciotechposv1.controller;

/**
 * Clase DTO (Data Transfer Object) para la solicitud de inicio de sesión.
 *
 * Se utiliza para mapear el cuerpo de la petición JSON enviada desde el frontend
 * (React) a un objeto Java, facilitando el transporte de credenciales de forma
 * estructurada hacia el AuthRestController.
 *
 * @author Danid Vallejos
 * @version 1.0
 */
public class LoginRequest {

    private String username;
    private String password;

    /**
     * Obtiene el nombre de usuario enviado en la petición.
     * @return String con el nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Define el nombre de usuario. Utilizado por Jackson para la deserialización del JSON.
     * @param username Nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña enviada en la petición.
     * @return String con la contraseña.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define la contraseña. Utilizado por Jackson para la deserialización del JSON.
     * @param password Contraseña.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}