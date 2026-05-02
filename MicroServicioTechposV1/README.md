# ?? MicroServicioTechposV1 - Backend API REST para TechPOS

Este es el **backend oficial** del sistema TechPOS, desarrollado como un microservicio con **Spring Boot 3.2.5**. Expone una **API REST** completa para ser consumida por el frontend en React (o cualquier otro cliente) y adem¨¢s ofrece un panel de acceso b¨¢sico con Thymeleaf (login y dashboard). La gesti¨®n de usuarios se realiza exclusivamente a trav¨¦s de la API `/api/usuarios`.

---

## ?? Tecnolog¨ªas principales

| Tecnolog¨ªa | Versi¨®n | Prop¨®sito |
|------------|---------|-----------|
| Java | 21 | Lenguaje base |
| Spring Boot | 3.2.5 | Framework principal |
| Spring Security | 6.x | Autenticaci¨®n y RBAC |
| Spring Data JPA | 3.2.5 | Persistencia |
| MySQL | 8.0+ | Base de datos |
| Argon2 (BouncyCastle) | 1.76 | Hashing de contrase?as (seguridad bancaria) |
| Thymeleaf | 3.1.x | Panel de login y dashboard (solo vistas auxiliares) |
| Maven | 3.9+ | Gesti¨®n de dependencias |

---

## ?? Estructura del proyecto (real, basada en el c¨®digo)

```
MicroServicioTechposV1/
©¦
©À©¤©¤ pom.xml
©À©¤©¤ mvnw, mvnw.cmd
©À©¤©¤ .gitignore, .gitattributes
©À©¤©¤ README.md
©¦
©À©¤©¤ database/
©¦   ©¸©¤©¤ setup_bd_techpos_v1.sql      ¡û Crea bd_techposv2, tabla usuario, carga 4 usuarios (clave: 123)
©¦
©À©¤©¤ src/main/java/org/example/microserviciotechposv1/
©¦   ©À©¤©¤ MicroServicioTechposV1Application.java
©¦   ©À©¤©¤ config/
©¦   ©¦   ©¸©¤©¤ SecurityConfig.java          ¡û CORS (localhost:5173), Argon2, sesi¨®n manual
©¦   ©À©¤©¤ controller/
©¦   ©¦   ©À©¤©¤ AuthRestController.java      ¡û POST /api/auth/login (JSON) + sesi¨®n manual
©¦   ©¦   ©À©¤©¤ LoginController.java         ¡û GET /login y /dashboard (Thymeleaf)
©¦   ©¦   ©À©¤©¤ LoginRequest.java            ¡û DTO para login JSON
©¦   ©¦   ©¸©¤©¤ UsuarioController.java       ¡û API REST /api/usuarios (CRUD completo)
©¦   ©À©¤©¤ model/
©¦   ©¦   ©À©¤©¤ Rol.java                     ¡û Enum (administrador, tecnico, recepcionista, inventario)
©¦   ©¦   ©¸©¤©¤ Usuario.java                 ¡û Entidad JPA (con validaciones)
©¦   ©À©¤©¤ repository/
©¦   ©¦   ©¸©¤©¤ UsuarioRepository.java
©¦   ©¸©¤©¤ service/
©¦       ©À©¤©¤ CustomUserDetailsService.java
©¦       ©¸©¤©¤ UsuarioService.java
©¦
©¸©¤©¤ src/main/resources/
    ©À©¤©¤ application.properties
    ©À©¤©¤ static/
    ©¸©¤©¤ templates/
        ©À©¤©¤ dashboard.html                ¡û Panel simple (solo informativo)
        ©À©¤©¤ login.html                    ¡û Formulario de login (Thymeleaf)
        ©¸©¤©¤ usuarios/                     ¡û Plantillas sin uso (no hay controlador web)
            ©À©¤©¤ formulario.html
            ©¸©¤©¤ lista.html
```

> **Nota:** Las plantillas `lista.html` y `formulario.html` existen pero **no est¨¢n conectadas** a ning¨²n controlador. El proyecto es puramente API REST para el CRUD de usuarios; el panel de administraci¨®n web no est¨¢ implementado.

---

## ?? Seguridad y autenticaci¨®n

- **Password encoder**: `Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()`
- **Sesiones**: `SessionCreationPolicy.IF_REQUIRED` + `HttpSessionSecurityContextRepository` (para login v¨ªa JSON)
- **Reglas de autorizaci¨®n**:
  - `/api/auth/**` ¡ú p¨²blico
  - `/api/**` ¡ú solo rol `administrador`
  - `/login`, `/dashboard`, `/css/**`, `/js/**`, `/images/**` ¡ú p¨²blicos
  - Cualquier otra ruta ¡ú requiere autenticaci¨®n
- **CORS**: Origen permitido `http://localhost:5173`, credenciales `true`

---

## ?? Endpoints de la API REST

| M¨¦todo | Endpoint | Descripci¨®n | Requiere rol |
|--------|----------|-------------|--------------|
| POST | `/api/auth/login` | Autenticaci¨®n (devuelve JSON con perfil) | p¨²blico |
| GET | `/api/usuarios` | Listar todos los usuarios | `administrador` |
| GET | `/api/usuarios/{id}` | Obtener usuario por ID | `administrador` |
| POST / PUT | `/api/usuarios` | Crear o actualizar usuario (JSON) | `administrador` |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario | `administrador` |

### Ejemplo de login (desde React con Axios)

```javascript
const response = await axios.post('http://localhost:8080/api/auth/login', {
  username: 'admin',
  password: '123'
}, { withCredentials: true });

// Respuesta:
// { id, nombre, usuario, rol, activo }
```

Todas las peticiones posteriores deben incluir `withCredentials: true` para enviar la cookie `JSESSIONID`.

---

## ??? Instalaci¨®n y ejecuci¨®n

### Requisitos previos

- JDK 21
- MySQL 8.0+
- Maven 3.9+ (o usar el wrapper `./mvnw`)

### Pasos

1. **Clonar el repositorio**
   ```
   git clone https://github.com/Danidval/MicroServicios-SpringBoot-Techpos-V1.git
   ```

2. **Crear la base de datos**
   Ejecuta el script `database/setup_bd_techpos_v1.sql` en tu MySQL.
   Esto crea la base `bd_techposv2`, la tabla `usuario` y carga 4 usuarios de prueba con contrase?a `123` (hasheada con Argon2).

3. **Configurar credenciales** en `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/bd_techposv2?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=tu_contrase?a
   ```

4. **Ejecutar la aplicaci¨®n**
   ```bash
   ./mvnw spring-boot:run   # Linux/macOS
   mvnw.cmd spring-boot:run # Windows
   ```

5. **Probar**
   - Acceso web (solo login/dashboard): `http://localhost:8080/login`
   - API (con Postman o frontend React): `POST http://localhost:8080/api/auth/login`

---

## ?? Usuarios de prueba

| Rol            | Usuario  | Contrase?a |
|----------------|----------|------------|
| Administrador  | `admin`  | `123`      |
| T¨¦cnico        | `carlos` | `123`      |
| Recepcionista  | `laura`  | `123`      |
| Inventario     | `inv`    | `123`      |

---

## ?? Notas importantes

- **El CRUD de usuarios solo est¨¢ disponible v¨ªa API REST**, no hay interfaz web para gestionar usuarios.
- Las plantillas Thymeleaf `formulario.html` y `lista.html` **no son utilizadas**; se mantienen por herencia del proyecto anterior pero no afectan al funcionamiento.
- La sesi¨®n se maneja con cookies `JSESSIONID`; aseg¨²rate de que tu frontend env¨ªe `withCredentials: true`.
- Para cualquier cambio en la estructura de la base de datos, modifica el script SQL y reinicia la aplicaci¨®n (el `ddl-auto=validate` no altera el esquema autom¨¢ticamente).

---

## ?? Autor

**Danid Esneider Vallejos Almeida**
Proyecto para el programa de An¨¢lisis y Desarrollo de Software ¨C SENA