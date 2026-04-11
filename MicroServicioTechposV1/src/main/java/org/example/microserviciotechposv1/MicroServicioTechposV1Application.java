package org.example.microserviciotechposv1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

/**
 * Clase principal (Main) del Microservicio TechPOS V1.
 * Punto de entrada para la ejecución de la aplicación Spring Boot.
 * Configura el contexto de la aplicación y permite la ejecución de tareas de inicialización.
 * * @author [Tu Nombre/Aprendiz]
 * @version 1.0
 */
@SpringBootApplication
public class MicroServicioTechposV1Application {

    /**
     * Método de inicio que arranca el servidor embebido y el contexto de Spring.
     * Contiene bloques de código para la generación y validación de hashes de seguridad.
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(MicroServicioTechposV1Application.class, args);

        /* * Uso del encoder Argon2 (estándar v5.8) para la generación manual de hashes.
         * Útil para crear usuarios iniciales en la base de datos con contraseñas seguras.
         */
      /*  Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        // Bloque de código para depuración y generación de credenciales iniciales
        String passwordClara = "123";
        String hashGenerado = encoder.encode(passwordClara);

        System.out.println("-------------------------------------------");
        System.out.println("HASH GENERADO PARA BASE DE DATOS:");
        System.out.println(hashGenerado);
        System.out.println("-------------------------------------------");

        // Validación de coincidencia del hash generado
        System.out.println("¿Validación exitosa?: " + encoder.matches(passwordClara, hashGenerado));
        */
    }

    /**
     * Bean opcional para ejecutar lógica al inicio de la aplicación.
     * Actualmente configurado para pruebas de encriptación BCrypt como alternativa.
     * @return Instancia de CommandLineRunner con la lógica de generación de hash.
     */
    /*
    @Bean
    public CommandLineRunner generarHash() {
        return args -> {
            PasswordEncoder encoder = new BCryptPasswordEncoder(12);
            String hash = encoder.encode("123");
            System.out.println("=========================================");
            System.out.println("Hash de respaldo (BCrypt):");
            System.out.println(hash);
            System.out.println("=========================================");
        };
    }
    */
}