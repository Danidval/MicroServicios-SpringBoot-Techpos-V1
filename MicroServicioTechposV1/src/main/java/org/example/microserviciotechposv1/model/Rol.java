package org.example.microserviciotechposv1.model;

/**
 * Enumeración que define los roles de usuario permitidos en el sistema TechPOS.
 * Estos roles determinan los niveles de acceso y permisos dentro de los
 * diferentes módulos de la aplicación (Web y Móvil).
 * * Basado en los casos de uso y requerimientos de seguridad del proyecto.
 * * @author [Tu Nombre/Aprendiz]
 * @version 1.0
 */
public enum Rol {
    /** Rol con acceso total a la configuración y gestión de usuarios. */
    administrador,

    /** Rol destinado al personal encargado de soporte o mantenimiento técnico. */
    tecnico,

    /** Rol con permisos para atención al cliente y registro de ingresos. */
    recepcionista,

    /** Rol enfocado en la gestión de stock y control de productos. */
    inventario
}