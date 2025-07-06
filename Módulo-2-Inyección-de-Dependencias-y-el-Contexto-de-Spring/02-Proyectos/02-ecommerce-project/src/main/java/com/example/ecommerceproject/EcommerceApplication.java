package com.example.ecommerceproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		// El método main ahora solo se encarga de arrancar la aplicación.
		// Spring se encargara de encontrar y ejecutar el AppRunner.
		SpringApplication.run(EcommerceApplication.class, args);
	}

}
