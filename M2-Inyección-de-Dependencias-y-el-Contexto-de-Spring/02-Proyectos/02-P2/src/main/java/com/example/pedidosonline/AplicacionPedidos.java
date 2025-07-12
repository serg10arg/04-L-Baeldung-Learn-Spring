package com.example.pedidosonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AplicacionPedidos {

	public static void main(String[] args) {
		// El metodo main ahora solo se encarga de arrancar la aplicacion
		// Spring encontrar y ejecutara EjecutorAplicacion automaticamente.
		SpringApplication.run(AplicacionPedidos.class, args);
	}

}
