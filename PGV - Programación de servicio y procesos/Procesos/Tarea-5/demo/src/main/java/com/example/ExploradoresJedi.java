package com.example;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;


public class ExploradoresJedi {
    public AtomicBoolean pitasEncontrada = new AtomicBoolean(false);
    volatile String ganador = null;
    public class Jedi implements Runnable{
        public String nombre;
        public String planeta;
        public Jedi(String nombre, String planeta){
            this.nombre = nombre;
            this.planeta =planeta;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(400, 1501));
                if(!pitasEncontrada.get()){
                    pitasEncontrada.set(true);
                    ganador=nombre;
                    System.out.print(nombre + " halló una pista en " + planeta + ". Fin de búsqueda.");
                }
            } catch (Exception e) {
                System.out.println("El hilo fue interrumpido.");
                Thread.currentThread().interrupt();
            }

        }

    }
    public static void main(String[] args) throws InterruptedException {
        ExploradoresJedi Explorador = new ExploradoresJedi();
        Thread t1 = new Thread(Explorador.new Jedi("Kenobi", "Tatooine"));
        Thread t2 = new Thread(Explorador.new Jedi("Skywalker", "Dagobah"));
        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}