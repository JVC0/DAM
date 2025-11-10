package com.example;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.*;

public class CazaHorrocruxes {
    public AtomicBoolean encontrado = new AtomicBoolean(false);
    public AtomicReference<String> ganador = new AtomicReference<>(null);

    public class Buscador implements Runnable {
        private String nombre;
        private String ubicacion;

        public Buscador(String nombre, String ubicacion) {
            this.nombre = nombre;
            this.ubicacion = ubicacion;
        }

        @Override
        public void run() {
            int tiempo = ThreadLocalRandom.current().nextInt(500, 1501);
            try {
                Thread.sleep(tiempo);
            } catch (InterruptedException e) {
                System.out.println("El hilo fue interrumpido.");
                Thread.currentThread().interrupt();
            }
            if (!encontrado.get()) {
                encontrado.set(true);
                ganador.set(nombre);
                System.out.println(nombre + " encontró un Horrocrux en " + ubicacion + ". ¡Búsqueda terminada!");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CazaHorrocruxes juego = new CazaHorrocruxes();
        Thread t1 = new Thread(juego.new Buscador("Harry", "Bosque Prohibido"));
        Thread t2 = new Thread(juego.new Buscador("Hermione", "Biblioteca Antigua"));
        Thread t3 = new Thread(juego.new Buscador("Ron", "Mazmorras del castillo"));
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }
}
