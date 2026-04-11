# MicroServicioTechposV1 - Sistema de Gestión TechPOS

Este es un **microservicio** desarrollado con **Spring Boot 3.2.5** diseñado para la gestión de usuarios y autenticación segura. A diferencia de las arquitecturas monolíticas tradicionales, este sistema está preparado para funcionar como un proveedor de recursos (API) y una interfaz administrativa desacoplada.

## 🚀 Tecnologías Utilizadas

| Tecnología                  | Versión      | Propósito                                      |
|-------------------------------|--------------|------------------------------------------------|
| Java                          | 21           | Lenguaje de programación                       |
| Spring Boot                   | 3.2.5        | Framework para Microservicios                  |
| Spring Security               | 6.x          | Autenticación y autorización (RBAC)            |
| Spring Data JPA               | 3.2.5        | Persistencia de datos y ORM                    |
| **REST API**                  | **JSON**     | **Intercambio de datos desacoplado**           |
| Thymeleaf                     | 3.1.x        | Motor de plantillas para el panel administrativo|
| MySQL                         | 8.0+         | Base de datos relacional                       |
| Argon2 (BouncyCastle)         | 1.76         | Hashing de seguridad de nivel bancario         |
| Bootstrap                     | 5.3          | Interfaz responsive                            |

## ⚙️ Arquitectura: Del Monolito al Microservicio

Para cumplir con los estándares modernos de desarrollo solicitados en la guía de aprendizaje, el proyecto implementa:

* **Separación de Responsabilidades:** Capas definidas de Controladores, Servicios, Repositorios y Modelos.
* **Endpoints RESTful:** Inclusión de controladores `@RestController` que retornan objetos en formato **JSON**, permitiendo que el sistema sea consumido por aplicaciones externas (Móvil, React, Angular).
* **Seguridad Desacoplada:** Configuración robusta de `SecurityFilterChain` para proteger tanto las vistas web como los puntos de acceso de la API.

## 📋 Características Principales

- **API REST:** Endpoint en `/api/usuarios` para consumo de datos.
- **Autenticación:** Sistema basado en base de datos con cifrado **Argon2id**.
- **Roles (RBAC):** Acceso granular para `administrador`, `tecnico`, `recepcionista` e `inventario`.
- **Panel Administrativo:** CRUD completo de usuarios con validaciones de integridad.
- **Seguridad:** Protección contra ataques comunes y manejo de errores 403 (Acceso denegado).

## 🛠️ Instalación y Configuración

### 1. Requisitos Previos
- **JDK 21**
- **MySQL Server 8.0+**
- Maven 3.9+

### 2. Configurar la Base de Datos
Crea la base de datos `bd_techposv2` y utiliza el script SQL incluido en la documentación del proyecto para generar la tabla de usuarios y los datos de prueba iniciales (cifrados con Argon2).

### 3. Ejecutar la Aplicación
```bash
mvnw.cmd spring-boot:run
La aplicación estará disponible en: http://localhost:8080

📁 Estructura del Proyecto (Actualizada)
text
MicroServicioTechposV1/
├── pom.xml
├── mvnw / mvnw.cmd
├── .gitignore
├── src/
│   ├── main/
│   │   ├── java/org/example/microserviciotechposv1/
│   │   │   ├── MicroServicioTechposV1Application.java
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── LoginController.java
│   │   │   │   ├── UsuarioController.java
│   │   │   │   └── UsuarioRestController.java   <-- ENDPOINT REST
│   │   │   ├── model/
│   │   │   │   ├── Rol.java
│   │   │   │   └── Usuario.java
│   │   │   ├── repository/
│   │   │   │   └── UsuarioRepository.java
│   │   │   └── service/
│   │   │       ├── CustomUserDetailsService.java
│   │   │       └── UsuarioService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
│   │           ├── dashboard.html
│   │           ├── login.html
│   │           └── usuarios/
│   │               ├── formulario.html
│   │               └── lista.html
│   └── test/
│       └── java/org/example/microserviciotechposv1/
│           └── MicroServicioTechposV1ApplicationTests.java
├── .idea/              (configuración del IDE)
├── .mvn/               (wrapper de Maven)
└── target/             (código compilado)
```
Nota: Se incluyen las carpetas .idea, .mvn y target para reflejar la estructura real, pero el foco del desarrollo está en src/main/java y src/main/resources.

🧪 Funcionalidades de Microservicio
Consumo de Datos JSON: Acceso a la lista de usuarios mediante /api/usuarios.

Prueba de Endpoint: Acceso directo a la data cruda mediante http://localhost:8080/api/usuarios (requiere rol de administrador).

Dashboard Dinámico: Personalización de la UI basada en el rol recuperado del contexto de seguridad.

Mensajería Flash: Feedback inmediato al usuario tras operaciones en la base de datos.

📄 Licencia
Este proyecto cumple con los requerimientos académicos del programa de Análisis y Desarrollo de Software del SENA.

✒️ Autor
Danid Esneider Vallejos Almeida GitHub: Danidval
