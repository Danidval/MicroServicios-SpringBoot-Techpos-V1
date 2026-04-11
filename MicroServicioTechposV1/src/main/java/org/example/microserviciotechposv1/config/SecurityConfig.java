package org.example.microserviciotechposv1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad del sistema (Spring Security).
 * Define las reglas de acceso, encriptación de contraseñas y gestión de sesiones
 * de acuerdo a los requerimientos técnicos del proyecto.
 * * @author Danid Vallejos
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    /**
     * Constructor para la inyección de dependencias del servicio de usuarios.
     * @param userDetailsService Servicio que gestiona la carga de usuarios desde la base de datos.
     */
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configura la cadena de filtros de seguridad (Security Filter Chain).
     * Define qué rutas son públicas y cuáles requieren roles específicos.
     * * @param http Objeto para configurar la seguridad web.
     * @return SecurityFilterChain configurado.
     * @throws Exception Si ocurre un error en la configuración.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitación de CSRF para facilitar el desarrollo inicial del módulo
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // 1. Recursos públicos: Permitir acceso a login y assets (CSS, JS, Imágenes)
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()

                        // 2. Control de acceso por rol: Gestión de usuarios restringida a administradores
                        .requestMatchers("/usuarios/**").hasRole("administrador")

                        // 3. Regla general: Cualquier otra petición requiere autenticación previa
                        .anyRequest().authenticated()
                )

                // Configuración del formulario de inicio de sesión personalizado
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )

                // Manejo de excepciones para mejorar la experiencia de usuario (UX)
                .exceptionHandling(exception -> exception
                        // Redirección personalizada en caso de que el usuario no tenga permisos suficientes
                        .accessDeniedPage("/dashboard?prohibido")
                )

                // Configuración del proceso de cierre de sesión
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true) // Invalida la sesión actual del servidor
                        .deleteCookies("JSESSIONID") // Elimina la cookie de sesión del navegador
                        .permitAll()
                )

                // Integración del servicio de detalles de usuario para la validación de credenciales
                .userDetailsService(userDetailsService);

        return http.build();
    }

    /**
     * Define el algoritmo de encriptación para las contraseñas de los usuarios.
     * Se utiliza Argon2, cumpliendo con los estándares modernos de ciberseguridad.
     * * @return Instancia de Argon2PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Implementación de Argon2 compatible con Spring Security 6.x / 5.8+
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
}