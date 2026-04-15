-- 1. Crear la base de datos (collation por defecto, no afecta a la columna si se especifica)
CREATE DATABASE IF NOT EXISTS bd_techposv2
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE bd_techposv2;

-- 2. Tabla Usuario con la columna 'usuario' case‑sensitive
CREATE TABLE IF NOT EXISTS usuario (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    rol ENUM('administrador', 'tecnico', 'recepcionista', 'inventario') NOT NULL,
    -- La siguiente línea hace que la comparación distinga mayúsculas/minúsculas
    usuario VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    especialidad VARCHAR(100) NULL,
    activo BIT(1) DEFAULT 1,
    fechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 3. Tabla ConfiguracionSistema (sin cambios)
CREATE TABLE IF NOT EXISTS ConfiguracionSistema (
    idConfiguracion INT AUTO_INCREMENT PRIMARY KEY,
    parametro VARCHAR(100) NOT NULL UNIQUE,
    valor TEXT NOT NULL,
    tipo ENUM('string', 'number', 'boolean', 'json') NOT NULL DEFAULT 'string',
    descripcion TEXT NULL,
    editable BIT(1) DEFAULT 1,
    seccion VARCHAR(50)
) ENGINE=InnoDB;

-- 4. Inserción de datos (mismos valores, ahora 'admin' solo coincidirá con minúsculas)
INSERT INTO usuario (nombre, rol, usuario, contrasena, activo) VALUES
('Admin General', 'administrador', 'admin', '$argon2id$v=19$m=16384,t=2,p=1$gsLrL7pYYI2snMGg4edD1w$Fkga5xjPdZsTItS9/becaZ7WbRBQlUwJ8wohGGJjeds', b'1'),
('Laura Recepcionista', 'recepcionista', 'laura', '$argon2id$v=19$m=16384,t=2,p=1$gsLrL7pYYI2snMGg4edD1w$Fkga5xjPdZsTItS9/becaZ7WbRBQlUwJ8wohGGJjeds', b'1'),
('Carlos Técnico', 'tecnico', 'carlos', '$argon2id$v=19$m=16384,t=2,p=1$gsLrL7pYYI2snMGg4edD1w$Fkga5xjPdZsTItS9/becaZ7WbRBQlUwJ8wohGGJjeds', b'1'),
('Inventario Encargado', 'inventario', 'inv', '$argon2id$v=19$m=16384,t=2,p=1$gsLrL7pYYI2snMGg4edD1w$Fkga5xjPdZsTItS9/becaZ7WbRBQlUwJ8wohGGJjeds', b'1');
