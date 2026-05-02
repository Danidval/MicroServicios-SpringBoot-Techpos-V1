# 🚀 MicroServicioTechposV1 - Sistema de Gestión TechPOS (Backend Híbrido)

Este es un **microservicio** desarrollado con **Spring Boot 3.2.5** que actúa como un backend híbrido: provee una **API REST** para ser consumida por frontends modernos (React, Angular, Vue, apps móviles) y, al mismo tiempo, mantiene un **panel administrativo** con Thymeleaf para gestión interna.

---

## ⚙️ Arquitectura: Del Monolito al Microservicio Desacoplado

El sistema ha evolucionado para cumplir con los estándares modernos de desarrollo:

| Capa | Tecnología | Propósito |
|------|------------|-----------|
| **API REST** | `@RestController`, JSON | Exponer recursos para aplicaciones externas (React, móvil) |
| **Interfaz Web** | Thymeleaf, Bootstrap | Panel de control para administradores |
| **Seguridad** | Spring Security 6.x, Argon2id | Autenticación robusta y control de acceso por roles (RBAC) |
| **Persistencia** | Spring Data JPA, MySQL | Almacenamiento de datos |
| **Comunicación cross‑origin** | CORS configurado | Permitir peticiones desde `http://localhost:5173` (frontend típico de React/Vite) |

### 🆕 Principales mejoras respecto a la versión anterior

- **Controlador REST específico** (`UsuarioRestController`) que devuelve JSON en lugar de vistas HTML.
- **Endpoint de autenticación para frontends externos** (`AuthRestController` con `POST /api/auth/login`) que devuelve los datos del usuario y maneja la sesión manualmente.
- **Configuración CORS explícita** en `SecurityConfig` para permitir peticiones desde `localhost:5173` (React).
- **Gestión manual del contexto de seguridad** mediante `SecurityContextRepository` para que la sesión funcione correctamente con peticiones AJAX.
- **Nuevo DTO `LoginRequest`** para recibir credenciales en JSON.
- **Separación clara de rutas:** `/api/**` para la API REST, las vistas Thymeleaf siguen en `/dashboard`, `/usuarios`, etc.

---

## 📂 Estructura del Proyecto (Actual - Basada en el código real)

```text
MicroServicioTechposV1/
│
├── pom.xml
├── mvnw, mvnw.cmd
├── .gitignore, .gitattributes
├── README.md
│
├── src/main/java/org/example/microserviciotechposv1/
│   ├── MicroServicioTechposV1Application.java
│   ├── config/
│   │   └── SecurityConfig.java               ← CORS, filtros, repositorio de sesión
│   ├── controller/
│   │   ├── LoginController.java              ← Vistas: /login, /dashboard
│   │   ├── UsuarioController.java            ← API REST (JSON) para CRUD de usuarios
│   │   ├── AuthRestController.java           ← Login para frontends externos
│   │   └── LoginRequest.java                 ← DTO para credenciales
│   ├── model/
│   │   ├── Rol.java                          ← Enum (administrador, tecnico, etc.)
│   │   └── Usuario.java                      ← Entidad JPA
│   ├── repository/
│   │   └── UsuarioRepository.java
│   └── service/
│       ├── CustomUserDetailsService.java     ← Carga de usuarios para Spring Security
│       └── UsuarioService.java               ← Lógica de negocio y encriptación
│
├── src/main/resources/
│   ├── application.properties                ← Configuración MySQL, JPA, Thymeleaf
│   ├── static/
│   └── templates/
│       ├── dashboard.html
│       ├── login.html
│       └── usuarios/
│           ├── formulario.html
│           └── lista.html
│
├── src/test/                                 ← Pruebas unitarias
├── .idea/                                    ← Configuración de IntelliJ (ignorada por Git)
├── .mvn/                                     ← Wrapper de Maven
├── database/
│   └── setup_bd_techpos_v1.sql               ← Script SQL con usuarios de prueba (clave: 123)
│
└── target/                                   ← Compilado (ignorado)
```

---

## 📋 Características Técnicas Principales

| Funcionalidad | Descripción |
|---------------|-------------|
| **API REST** | Endpoints en `/api/usuarios` (CRUD completo) y `/api/auth/login` (autenticación). |
| **Seguridad** | Contraseñas hasheadas con **Argon2id** (estándar bancario). |
| **Control de acceso** | Basado en roles: `administrador`, `tecnico`, `recepcionista`, `inventario`. |
| **Persistencia** | MySQL con Hibernate. El DDL está en modo `validate` (no altera la estructura). |
| **CORS** | Configurado para aceptar peticiones desde `http://localhost:5173` con credenciales. |
| **Sesiones** | Uso de `HttpSessionSecurityContextRepository` para que funcione el login vía API. |

---

## 🔐 Seguridad y Endpoints

### 🧪 Endpoints de la API REST

| Método | Endpoint | Descripción | Requiere rol |
|--------|----------|-------------|--------------|
| POST | `/api/auth/login` | Autenticar usuario (envía JSON con `username`/`password`) | Público |
| GET | `/api/usuarios` | Listar todos los usuarios (JSON) | `administrador` |
| GET | `/api/usuarios/{id}` | Obtener un usuario por ID | `administrador` |
| POST / PUT | `/api/usuarios` | Crear o actualizar usuario (desde JSON) | `administrador` |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario | `administrador` |

### 🌐 Endpoints de la Interfaz Web (Thymeleaf)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/login` | Página de login |
| GET | `/dashboard` | Panel principal (muestra nombre y rol) |
| GET | `/usuarios` | Listado de usuarios (solo administradores) |
| GET | `/usuarios/nuevo` | Formulario de creación |
| GET | `/usuarios/editar/{id}` | Formulario de edición |
| POST | `/usuarios/guardar` | Guardar cambios |
| GET | `/usuarios/eliminar/{id}` | Eliminar usuario |

---

## 🛠️ Configuración y Ejecución

### Requisitos previos

- **JDK 21** o superior
- **MySQL 8.0** (o MariaDB 10.6+)
- **Maven 3.9+** (o usar el wrapper `./mvnw`)

### Pasos para poner en marcha el backend

1. **Clonar el repositorio** y abrir el proyecto en tu IDE.

2. **Crear la base de datos**
   Ejecuta el script ubicado en `database/setup_bd_techpos_v1.sql` en tu MySQL.
   Esto creará la base de datos `bd_techposv2`, la tabla `usuario` y cargará 4 usuarios de prueba con la contraseña `123` (hasheada con Argon2).

3. **Configurar credenciales de MySQL**
   Edita `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/bd_techposv2?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=3333
   ```

4. **Ejecutar la aplicación**
   ```bash
   # Linux/macOS
   ./mvnw spring-boot:run

   # Windows
   mvnw.cmd spring-boot:run
   ```

5. **Acceder**
   - Panel web: `http://localhost:8080/login` (usuario: `admin`, contraseña: `123`)
   - API (requiere autenticación previa): `http://localhost:8080/api/usuarios`
   - Login desde React: `POST http://localhost:8080/api/auth/login`

---

## 🔌 Consumo desde un Frontend Moderno (React / Angular)

El backend está listo para recibir peticiones desde un frontend que corra en `http://localhost:5173` (puerto típico de Vite). La configuración CORS ya está activada y permite el envío de credenciales.

### Ejemplo de login con Axios (React)

```javascript
const response = await axios.post('http://localhost:8080/api/auth/login', {
  username: 'admin',
  password: '123'
}, { withCredentials: true });

console.log(response.data); // { id, nombre, usuario, rol, activo }
```

Las siguientes peticiones deben incluir `withCredentials: true` para que el navegador envíe la cookie `JSESSIONID`.

### Ejemplo de listar usuarios (requiere rol administrador)

```javascript
const usuarios = await axios.get('http://localhost:8080/api/usuarios', {
  withCredentials: true
});
```

---

## 📄 Notas adicionales

- **Cambio importante respecto a la versión anterior:** ahora existe `UsuarioRestController` (devuelve JSON) y `UsuarioController` (devuelve vistas Thymeleaf). No están mezclados.
- El método `guardar()` del `UsuarioController` original (vistas) se mantiene para el panel web, pero la API REST usa su propia lógica en `UsuarioController` (el de `/api/usuarios`).
- Se ha añadido `AuthRestController` con manejo manual del contexto de seguridad, algo que no existía en la versión anterior.
- La encriptación de contraseñas sigue siendo **Argon2id** (más seguro que BCrypt).

---

## ✒️ Autor

**Danid Esneider Vallejos Almeida**
Proyecto para el programa de Análisis y Desarrollo de Software – SENA.