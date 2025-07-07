package com.example.tiendaonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TiendaOnlineApp {

    public static void main(String[] args) {
        // El metodo main arranca la aplicacion y la cierra inmediatamente despues
        // de que el CommandLineRunner termine. Esto nos permite ver la fase de
        // detruccion de los bean de forma clara.
        System.out.println("--- Iniciando la aplicacion de la tienda online... ---");
        SpringApplication.run(TiendaOnlineApp.class, args);
        System.out.println("--- Aplicacion de la tienda online finalizada. ---");
    }

}
