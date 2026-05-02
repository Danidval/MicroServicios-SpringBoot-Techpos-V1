package org.example.microserviciotechposv1.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración de seguridad centralizada para el microservicio TechPOS V1.
 *
 * Define las políticas de acceso, gestión de sesiones manuales para API REST,
 * configuración de CORS para la integración con React y cifrado de contraseñas.
 * Esta clase es el núcleo que permite la comunicación segura entre el backend
 * y el componente frontend del proyecto formativo.
 *
 * @author Danid Vallejos
 * @version 1.1
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Define el repositorio para el contexto de seguridad.
     * Es fundamental para que el AuthRestController guarde manualmente la sesión
     * y el JSESSIONID sea persistente entre peticiones[cite: 8].
     */
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    /**
     * Configuración principal de la cadena de filtros de seguridad.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Habilita CORS y deshabilita CSRF para permitir peticiones desde el origen de React[cite: 8]
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                // Registra el repositorio de contexto para el manejo manual de sesiones[cite: 8]
                .securityContext(context -> context
                        .securityContextRepository(securityContextRepository())
                )

                // Definición de reglas de autorización por ruta
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Permite login sin estar autenticado[cite: 8]
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        // Restringe el acceso a la API administrativa solo a usuarios con rol 'administrador'
                        .requestMatchers("/api/**").hasRole("administrador")
                        .anyRequest().authenticated()
                )

                // Manejo de excepciones para responder con estados HTTP adecuados en lugar de redirecciones
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
                        })
                )

                // Política de creación de sesiones: Solo si es necesario para mantener al usuario logueado[cite: 8]
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // Configuración de cierre de sesión personalizada para API REST
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK); // Retorna 200 OK tras logout
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )

                .userDetailsService(userDetailsService);

        return http.build();
    }

    /**
     * Configura el intercambio de recursos de origen cruzado (CORS).
     * Permite que React (en puerto 5173) se comunique con este backend (puerto 8080)[cite: 8, 9].
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Origen de Vite[cite: 9]
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Cabeceras permitidas para la compatibilidad con Axios y manejo de tokens/cookies[cite: 8]
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "X-Requested-With"));
        configuration.setAllowCredentials(true); // REQUERIDO para permitir el envío de cookies de sesión[cite: 8]

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Define el algoritmo de hashing para las contraseñas.
     * Argon2 es el estándar recomendado para máxima seguridad de credenciales.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    /**
     * Expone el AuthenticationManager para ser utilizado en el controlador de login manual[cite: 8].
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}