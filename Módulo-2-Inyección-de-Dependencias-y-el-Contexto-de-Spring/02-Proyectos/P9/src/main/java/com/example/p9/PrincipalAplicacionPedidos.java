package com.example.p9;

import com.example.p9.interfaz.IServicioDePedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.example.p9"})
public class PrincipalAplicacionPedidos {

	private static final Logger LOG = LoggerFactory.getLogger(PrincipalAplicacionPedidos.class);

	public static void main(String[] args) {
		LOG.info("Iniciando la aplicación de pedidos de gestion...");

		ConfigurableApplicationContext context = SpringApplication.run(PrincipalAplicacionPedidos.class, args);
		LOG.info("Contexto de Spring inicializado correctamente");

		IServicioDePedido servicioPedido = context.getBean(IServicioDePedido.class);
		LOG.info("Bean de IServicioDePedido obtenido del contexto");

		servicioPedido.procesarNuevoPedido("Monitor Curvo Ultrawide", 1);
		servicioPedido.procesarNuevoPedido("Teclado Gamer", 3);

		context.close();
		LOG.info("Aplicación de pedidos finalizada y contexto cerrado");
	}

}
