package org.example.microserviciotechposv1;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Clase principal (Main) del Microservicio TechPOS V1.
 * Punto de entrada para la ejecución de la aplicación Spring Boot.
 * Configura el contexto de la aplicación y permite la ejecución de tareas de inicialización.
 * * @author Danid Vallejos
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

    }

}