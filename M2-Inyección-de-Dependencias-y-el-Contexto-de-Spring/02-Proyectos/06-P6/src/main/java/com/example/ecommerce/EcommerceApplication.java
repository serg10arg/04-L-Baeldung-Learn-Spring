package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        // SpringApplication.run inicia el contexto de Spring, que escaneará,
        // creará y configurará todos los beans definidos.
        SpringApplication.run(EcommerceApplication.class, args);
        System.out.println("\n--- La aplicación ha finalizado su inicialización. Revisa los logs para ver el ciclo" +
                "de vida de los beans.");
    }

}
