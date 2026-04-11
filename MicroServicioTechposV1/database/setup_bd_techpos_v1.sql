-- 1. Crear la base de datos con codificación moderna
CREATE DATABASE IF NOT EXISTS bd_techposv2
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE bd_techposv2;

-- 2. Tabla Usuario corregida (especialmente la columna 'activo')
CREATE TABLE IF NOT EXISTS usuario (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    rol ENUM('administrador', 'tecnico', 'recepcionista', 'inventario') NOT NULL,
    usuario VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    especialidad VARCHAR(100) NULL,
    -- BIT(1) es lo que Hibernate espera para un Boolean en Java con MySQL
    activo BIT(1) DEFAULT 1, 
    fechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 3. Tabla ConfiguracionSistema
CREATE TABLE IF NOT EXISTS ConfiguracionSistema (
    idConfiguracion INT AUTO_INCREMENT PRIMARY KEY,
    parametro VARCHAR(100) NOT NULL UNIQUE,
    valor TEXT NOT NULL,
    tipo ENUM('string', 'number', 'boolean', 'json') NOT NULL DEFAULT 'string',
    descripcion TEXT NULL,
    editable BIT(1) DEFAULT 1,
    seccion VARCHAR(50)
) ENGINE=InnoDB;

-- 4. Inserción de datos con el Hash Argon2 que generó tu consola (contraseña "123")
-- NOTA: He usado b'1' para el tipo BIT (equivalente a TRUE)
INSERT INTO usuario (nombre, rol, usuario, contrasena, activo) VALUES
('Admin General', 'administrador', 'admin', '$argon2id$v=19$m=16384,t=2,p=1$gsLrL7pYYI2snMGg4edD1w$Fkga5xjPdZsTItS9/becaZ7WbRBQlUwJ8wohGGJjeds', b'1'),
('Laura Recepcionista', 'recepcionista', 'laura', '$argon2id$v=19$m=16384,t=2,p=1$gsLrL7pYYI2snMGg4edD1w$Fkga5xjPdZsTItS9/becaZ7WbRBQlUwJ8wohGGJjeds', b'1'),
('Carlos Técnico', 'tecnico', 'carlos', '$argon2id$v=19$m=16384,t=2,p=1$gsLrL7pYYI2snMGg4edD1w$Fkga5xjPdZsTItS9/becaZ7WbRBQlUwJ8wohGGJjeds', b'1'),
('Inventario Encargado', 'inventario', 'inv', '$argon2id$v=19$m=16384,t=2,p=1$gsLrL7pYYI2snMGg4edD1w$Fkga5xjPdZsTItS9/becaZ7WbRBQlUwJ8wohGGJjeds', b'1');