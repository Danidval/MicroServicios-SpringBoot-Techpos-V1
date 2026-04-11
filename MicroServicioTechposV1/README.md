```markdown
# MicroServicioTechposV1 - Sistema de Gestión TechPOS

Este es un microservicio desarrollado con **Spring Boot 3.2.5** para la gestión de usuarios y autenticación segura en una plataforma POS (Point of Sale) llamada **TechPOS**.

Incluye sistema de login, control de roles y un CRUD completo de usuarios con restricciones según el rol.

## 🚀 Tecnologías Utilizadas

| Tecnología                     | Versión      | Propósito                                      |
|-------------------------------|--------------|------------------------------------------------|
| Java                          | 21           | Lenguaje de programación                       |
| Spring Boot                   | 3.2.5        | Framework principal                            |
| Spring Security               | 6.x          | Autenticación y autorización                   |
| Spring Data JPA               | 3.2.5        | Persistencia con Hibernate                     |
| Thymeleaf                     | 3.1.x        | Motor de plantillas HTML                       |
| MySQL                         | 8.0+         | Base de datos relacional                       |
| Argon2 (BouncyCastle)         | 1.76         | Hashing seguro de contraseñas                  |
| Bootstrap                     | 5.3          | Diseño responsive del frontend                 |
| Maven                         | 3.9+         | Gestión de dependencias y build                |

## 📋 Características Principales

- Autenticación personalizada con base de datos y **Argon2id**
- Control de acceso basado en roles: `administrador`, `tecnico`, `recepcionista`, `inventario`
- CRUD completo de usuarios (solo accesible por **administrador**)
- Encriptación fuerte de contraseñas
- Interfaz moderna y responsive con Bootstrap 5
- Página de acceso denegado personalizada (403)
- Logout seguro con invalidación de sesión y eliminación de cookies
- Validaciones en backend y frontend

## 🛠️ Instalación y Configuración

### 1. Requisitos Previos

- **JDK 21** instalado y configurado
- **MySQL Server 8.0+** en ejecución (XAMPP, Laragon, etc.)
- Maven 3.9+ o usar el Maven Wrapper incluido (`mvnw`)

### 2. Clonar el Repositorio

```bash
git clone https://github.com/Danidval/springboot-techpos.git
cd MicroServicioTechposV1
```

### 3. Configurar la Base de Datos

Ejecuta el siguiente script SQL en MySQL:

```sql
CREATE DATABASE IF NOT EXISTS bd_techposv2
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE bd_techposv2;

CREATE TABLE IF NOT EXISTS usuario (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    rol ENUM('administrador', 'tecnico', 'recepcionista', 'inventario') NOT NULL,
    usuario VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    especialidad VARCHAR(100) NULL,
    activo BIT(1) DEFAULT 1,
    fechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Usuarios de prueba (contraseña: 123 para todos)
INSERT INTO usuario (nombre, rol, usuario, contrasena, activo) VALUES
('Admin General', 'administrador', 'admin', '$argon2id$v=19$m=16384,t=2,p=1$gsLrL7pYYI2snMGg4edD1w$Fkga5xjPdZsTItS9/becaZ7WbRBQlUwJ8wohGGJjeds', b'1'),
('Laura Recepcionista', 'recepcionista', 'laura', '$argon2id$v=19$m=16384,t=2,p=1$gsLrL7pYYI2snMGg4edD1w$Fkga5xjPdZsTItS9/becaZ7WbRBQlUwJ8wohGGJjeds', b'1'),
('Carlos Técnico', 'tecnico', 'carlos', '$argon2id$v=19$m=16384,t=2,p=1$gsLrL7pYYI2snMGg4edD1w$Fkga5xjPdZsTItS9/becaZ7WbRBQlUwJ8wohGGJjeds', b'1'),
('Inventario Encargado', 'inventario', 'inv', '$argon2id$v=19$m=16384,t=2,p=1$gsLrL7pYYI2snMGg4edD1w$Fkga5xjPdZsTItS9/becaZ7WbRBQlUwJ8wohGGJjeds', b'1');
```

### 4. Configurar conexión a la Base de Datos

Edita el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=3333   # ← Cambia por tu contraseña de MySQL
```

### 5. Ejecutar la Aplicación

Opción recomendada (Maven Wrapper):

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

La aplicación estará disponible en: http://localhost:8080

## 🔐 Credenciales de Prueba

| Usuario | Contraseña | Rol            |
|---------|------------|----------------|
| admin   | 123        | Administrador  |
| laura   | 123        | Recepcionista  |
| carlos  | 123        | Técnico        |
| inv     | 123        | Inventario     |

> **Nota:** Solo el usuario administrador puede acceder a la gestión de usuarios (`/usuarios`).

## 📁 Estructura del Proyecto

```
MicroServicioTechposV1/
├── src/main/java/org/example/microserviciotechposv1/
│   ├── MicroServicioTechposV1Application.java
│   ├── config/SecurityConfig.java
│   ├── controller/
│   │   ├── LoginController.java
│   │   └── UsuarioController.java
│   ├── model/
│   │   ├── Rol.java
│   │   └── Usuario.java
│   ├── repository/UsuarioRepository.java
│   └── service/
│       ├── CustomUserDetailsService.java
│       └── UsuarioService.java
└── src/main/resources/
    ├── application.properties
    └── templates/
        ├── login.html
        ├── dashboard.html
        └── usuarios/
            ├── lista.html
            └── formulario.html
```

## 🧪 Funcionalidades Principales

- Login con validación de credenciales
- Dashboard personalizado según el rol del usuario
- Gestión de usuarios (solo administrador):
    - Listar usuarios
    - Crear nuevo usuario
    - Editar usuario (con opción de cambiar o mantener contraseña)
    - Eliminar usuario
- Control de acceso: Los demás roles no pueden acceder a `/usuarios/**`
- Mensajes flash de éxito y advertencia

## 📄 Licencia

Este proyecto fue desarrollado con fines académicos para el programa de Análisis y Desarrollo de Software del SENA.

## ✒️ Autor

**Danid Esneider Vallejos Almeida**  
GitHub: [Danidval](https://github.com/Danidval)
```