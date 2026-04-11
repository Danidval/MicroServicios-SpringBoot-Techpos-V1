package org.example.microserviciotechposv1.model;

// Importaciones necesarias para JPA (persistencia)
import jakarta.persistence.*;
// Importaciones para validación de campos (Bean Validation)
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
// Importación para manejo de fechas/horas
import java.time.LocalDateTime;

/**
 * Entidad de persistencia que representa la tabla "usuario" en la base de datos.
 * Define la estructura de datos y reglas de validación para TechPOS.
 *
 * * @author Danid Vallejos
 * @version 1.1
 */
@Entity // Indica que esta clase es una entidad JPA (se mapea a una tabla)
@Table(name = "usuario") // Especifica el nombre exacto de la tabla en la base de datos
public class Usuario {

    // ==================== ATRIBUTOS (CAMPOS DE LA TABLA) ====================

    @Id // Marca este campo como clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental (MySQL)
    @Column(name = "idUsuario") // Nombre de la columna en la tabla (camelCase)
    private Integer idUsuario; // Identificador único de cada usuario

    @NotBlank(message = "El nombre no puede estar vacío") // El nombre no puede ser nulo, vacío o solo espacios
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres") // Longitud válida
    @Column(nullable = false, length = 255) // Columna NOT NULL y longitud máxima 255
    private String nombre; // Nombre completo del usuario

    @NotNull(message = "Debe seleccionar un rol") // El rol no puede ser nulo
    @Enumerated(EnumType.STRING) // Guarda el nombre del enum (ej: "administrador") en lugar del índice
    @Column(name = "rol", nullable = false) // Columna NOT NULL
    private Rol rol; // Rol del usuario (administrador, técnico, recepcionista, inventario)

    @NotBlank(message = "El nombre de usuario es obligatorio") // No puede estar vacío
    @Size(min = 3, max = 100, message = "El usuario debe tener entre 3 y 100 caracteres") // Ajustado para 'inv'
    @Column(unique = true, nullable = false, length = 100) // Único, NOT NULL, longitud 100
    private String usuario; // Nombre de usuario para el login

    /**
     * Contraseña encriptada.
     * Se usa una expresión regular para validar la fortaleza solo si se ingresa.
     */
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") // Longitud mínima
    @Pattern(regexp = "^$|^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d\\W_]{8,}$",
            message = "Mínimo 8 caracteres, incluyendo mayúscula, minúscula, número y permite símbolos")
    // La expresión regular exige al menos una minúscula, una mayúscula y un dígito.
    // El "^$|" permite que la validación se salte si el campo está vacío (útil en edición).
    @Column(nullable = false, length = 255) // NOT NULL, almacena el hash (no la contraseña plana)
    private String contrasena; // Contraseña (se guarda hasheada con Argon2)

    @Size(max = 100, message = "La especialidad no puede superar los 100 caracteres") // Longitud máxima
    @Column(length = 100) // Columna con longitud 100 (puede ser NULL)
    private String especialidad; // Especialidad del técnico (opcional)

    @Column(name = "activo") // Nombre de la columna
    private Boolean activo = true; // Indica si la cuenta está habilitada (por defecto true)

    @Column(name = "fechaCreacion", insertable = false, updatable = false)
    // insertable=false: no se incluye en INSERT, la BD asigna valor por defecto
    // updatable=false: no se modifica en UPDATE
    private LocalDateTime fechaCreacion; // Fecha de creación (automática en BD)

    // ==================== CONSTRUCTOR VACÍO ====================
    // Requerido por JPA para crear instancias mediante reflexión
    public Usuario() {}

    // ==================== GETTERS Y SETTERS ====================
    // Permiten acceder y modificar los atributos privados

    public Integer getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}