package org.example.microserviciotechposv1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Entidad de persistencia que representa la tabla "usuario" en la base de datos.
 *
 * Define la estructura de datos, las restricciones de integridad y las
 * reglas de validación para la gestión de cuentas. Ha sido ajustada para
 * soportar la lógica de edición de perfiles sin forzar el reingreso de contraseñas.
 *
 * @author Danid Vallejos
 * @version 1.1
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Integer idUsuario;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    @Column(nullable = false, length = 255)
    private String nombre;

    /**
     * Relación con el Enum Rol.
     * Se almacena como STRING en la base de datos para mayor legibilidad.
     */
    @NotNull(message = "Debe seleccionar un rol")
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 100, message = "El usuario debe tener entre 3 y 100 caracteres")
    @Column(unique = true, nullable = false, length = 100)
    private String usuario;

    /**
     * GESTIÓN DE SEGURIDAD (PASSWORD):
     * Se utiliza una expresión regular (Regex) que permite dos estados:
     * 1. Un String vacío (utilizado durante la edición en el frontend para mantener la clave actual).
     * 2. Una clave fuerte que cumpla con: 8+ caracteres, 1 mayúscula, 1 minúscula y 1 número.
     */
    @Pattern(regexp = "^$|^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d\\W_]{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres, incluyendo una mayúscula, una minúscula y un número")
    @Column(nullable = false, length = 255)
    private String contrasena;

    @Size(max = 100, message = "La especialidad no puede superar los 100 caracteres")
    @Column(length = 100)
    private String especialidad;

    @Column(name = "activo")
    private Boolean activo = true;

    /**
     * Auditoría: Almacena la fecha de registro.
     * Los atributos insertable=false y updatable=false delegan el control al motor de BD.
     */
    @Column(name = "fechaCreacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Usuario() {}

    // ==================== MÉTODOS DE ACCESO (GETTERS Y SETTERS) ====================

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}