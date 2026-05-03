# MicroServicioTechposV1 - Backend API REST para TechPOS
 
Este es el **backend oficial** del sistema TechPOS, desarrollado como un microservicio con **Spring Boot 3.2.5**. Expone una **API REST** completa para ser consumida por el frontend en React (o cualquier otro cliente) y además ofrece un panel de acceso básico con Thymeleaf (login y dashboard). La gestión de usuarios se realiza exclusivamente a través de la API `/api/usuarios`.
 
---
 
## Tecnologías principales
 
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 21 | Lenguaje base |
| Spring Boot | 3.2.5 | Framework principal |
| Spring Security | 6.x (incluido con Spring Boot 3.2.5) | Autenticación y RBAC |
| Spring Data JPA | 3.2.5 | Persistencia |
| MySQL | 8.0+ | Base de datos |
| Argon2 (BouncyCastle) | 1.76 | Hashing de contraseñas |
| Thymeleaf | 3.1.x | Panel de login y dashboard (vistas auxiliares) |
| Maven | 3.9+ | Gestión de dependencias |
 
---
 
## Estructura del proyecto
 
```
MicroServicioTechposV1/
│
├── pom.xml
├── mvnw, mvnw.cmd
├── .gitignore, .gitattributes
├── README.md
│
├── database/
│   └── setup_bd_techpos_v1.sql       ← Crea bd_techposv2, tabla usuario, carga 4 usuarios (clave: 123)
│
└── src/main/java/org/example/microserviciotechposv1/
    ├── MicroServicioTechposV1Application.java
    ├── config/
    │   └── SecurityConfig.java           ← CORS (localhost:5173), Argon2, sesión manual
    ├── controller/
    │   ├── AuthRestController.java       ← POST /api/auth/login (JSON) + sesión manual
    │   ├── LoginController.java          ← GET /login y /dashboard (Thymeleaf)
    │   ├── LoginRequest.java             ← DTO para login JSON
    │   └── UsuarioController.java        ← API REST /api/usuarios (CRUD completo)
    ├── model/
    │   ├── Rol.java                      ← Enum (administrador, tecnico, recepcionista, inventario)
    │   └── Usuario.java                  ← Entidad JPA (con validaciones)
    ├── repository/
    │   └── UsuarioRepository.java
    └── service/
        ├── CustomUserDetailsService.java
        └── UsuarioService.java
```
 
> **Nota:** Las plantillas `lista.html` y `formulario.html` dentro de `templates/usuarios/` existen pero **no están conectadas a ningún controlador web**. El CRUD de usuarios se gestiona exclusivamente a través de la API REST. Estas plantillas se conservan por herencia del proyecto anterior y no afectan el funcionamiento.
 
---
 
## Seguridad y autenticación
 
- **Password encoder:** `Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()`
- **Gestión de sesión:** `SessionCreationPolicy.IF_REQUIRED` + `HttpSessionSecurityContextRepository` (necesario para login vía JSON con persistencia manual del contexto de seguridad)
- **Reglas de autorización:**
  - `/api/auth/**` → público
  - `/api/**` → solo rol `administrador`
  - `/login`, `/dashboard`, `/css/**`, `/js/**`, `/images/**` → públicos
  - Cualquier otra ruta → requiere autenticación
- **CORS:** Origen permitido `http://localhost:5173`, `allowCredentials: true`
---
 
## Endpoints de la API REST
 
| Método | Endpoint | Descripción | Requiere rol |
|--------|----------|-------------|--------------|
| POST | `/api/auth/login` | Autenticación (devuelve JSON con perfil del usuario) | público |
| GET | `/api/usuarios` | Listar todos los usuarios | `administrador` |
| GET | `/api/usuarios/{id}` | Obtener usuario por ID | `administrador` |
| POST / PUT | `/api/usuarios` | Crear o actualizar usuario (cuerpo JSON) | `administrador` |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario | `administrador` |
 
### Ejemplo de login desde React con Axios
 
```javascript
const response = await axios.post('http://localhost:8080/api/auth/login', {
  username: 'admin',
  password: '123'
}, { withCredentials: true });
 
// Respuesta esperada:
// { id, nombre, usuario, rol, activo }
```
 
> Todas las peticiones posteriores al login deben incluir `withCredentials: true` para enviar la cookie `JSESSIONID` y mantener la sesión activa.
 
---
 
## Instalación y ejecución
 
### Requisitos previos
 
- JDK 21
- MySQL 8.0+
- Maven 3.9+ (o usar el wrapper incluido `./mvnw`)
### Pasos
 
1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Danidval/MicroServicios-SpringBoot-Techpos-V1.git
   ```
 
2. **Crear la base de datos**
   Ejecuta el script `database/setup_bd_techpos_v1.sql` en tu cliente MySQL. Este script crea la base de datos `bd_techposv2`, la tabla `usuario` y carga 4 usuarios de prueba con contraseña `123` (hasheada con Argon2).
3. **Configurar credenciales de MySQL**
   Abre `src/main/resources/application.properties` y ajusta el usuario y contraseña según tu entorno local:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/bd_techposv2?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=TU_CONTRASEÑA
   ```
 
4. **Ejecutar la aplicación**
   ```bash
   # Linux / macOS
   ./mvnw spring-boot:run
 
   # Windows
   mvnw.cmd spring-boot:run
   ```
 
5. **Verificar funcionamiento**
   - Panel web (login/dashboard): `http://localhost:8080/login`
   - API REST (desde Postman o frontend React): `POST http://localhost:8080/api/auth/login`
---
 
## Usuarios de prueba
 
| Rol | Usuario | Contraseña |
|-----|---------|------------|
| Administrador | `admin` | `123` |
| Técnico | `carlos` | `123` |
| Recepcionista | `laura` | `123` |
| Inventario | `inv` | `123` |
 
---
 
## Notas importantes
 
- El CRUD de usuarios **solo está disponible vía API REST**; no existe interfaz web para gestionar usuarios desde el navegador.
- La sesión se maneja mediante cookies `JSESSIONID`. Es obligatorio que el frontend envíe `withCredentials: true` en cada petición protegida.
- El `ddl-auto=validate` en `application.properties` significa que Hibernate **valida** el esquema existente pero **no lo modifica**. Si realizas cambios en la estructura de la base de datos, actualiza el script SQL y vuelve a ejecutarlo antes de reiniciar la aplicación.
- El naming strategy está configurado para respetar el CamelCase de los campos Java (`idUsuario`, `fechaCreacion`, etc.) sin convertirlos a snake_case.
---
 
## Autor
 
**Danid Esneider Vallejos Almeida**  
Programa: Análisis y Desarrollo de Software — SENA  
Repositorio frontend: [https://github.com/Danidval/techpos-frontend](https://github.com/Danidval/techpos-frontend)
