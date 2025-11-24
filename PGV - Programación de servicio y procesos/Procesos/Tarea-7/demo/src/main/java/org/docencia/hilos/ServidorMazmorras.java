package org.docencia.hilos;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class ServidorMazmorras {

    static class PeticionMazmorra implements Runnable, Comparable<PeticionMazmorra> {
    
        private final String nombreJugador;
        private final String mazmorra;
        private final int prioridad;

        public int getPrioridad() {
            return prioridad;
        }
        public PeticionMazmorra(String nombreJugador, String mazmorra, int prioridad) {
            this.nombreJugador = nombreJugador;
            this.mazmorra = mazmorra;
            this.prioridad = prioridad;
        }
        
        @Override
        public void run() {
            String hilo = Thread.currentThread().getName();
            System.out.println("[" + hilo + "] Preparando mazmorra '" + mazmorra +
                    "' para el jugador " + nombreJugador + " (prioridad " + prioridad + ")...");
            try {
                Thread.sleep(1000 + (int)(Math.random() * 1000));
            } catch (InterruptedException e) {
                System.out.println("[" + hilo + "] Petición de " + nombreJugador + " interrumpida");
                Thread.currentThread().interrupt();
                return;
            }
            System.out.println("[" + hilo + "] Mazmorra '" + mazmorra +
                    "' lista para " + nombreJugador + " 🎮");
        }
        @Override
    public int compareTo(PeticionMazmorra otra) {
        return Integer.compare(this.prioridad,otra.prioridad );
    }
    }

    public static void main(String[] args) throws InterruptedException {

        ExecutorService gmBots = Executors.newFixedThreadPool(3); // Creamos un pool de 3
         PriorityBlockingQueue<PeticionMazmorra> colaDetareas = new PriorityBlockingQueue<>();
        // Simulamos 10 jugadores que quieren entrar a mazmorras
        String[] jugadores = {
                "Link", "Zelda", "Geralt", "Yennefer", "Gandalf",
                "Frodo", "Aragorn", "Leia", "Luke", "DarthVader"
        };
        String[] mazmorras = {
                "Catacumbas de Hyrule", "Torre Oscura", "Moria",
                "Estrella de la Muerte", "Nido de Dragón"
        };
        
        for (int i = 0; i < jugadores.length; i++) {
            String jugador = jugadores[i];
            String dungeon = mazmorras[i % mazmorras.length];
            colaDetareas.put(new PeticionMazmorra(jugador, dungeon,ThreadLocalRandom.current().nextInt(1, 4)));
        }
        while (!colaDetareas.isEmpty()) {
            PeticionMazmorra peticion = colaDetareas.take();
            gmBots.execute(peticion);
        }
        gmBots.shutdown();
        System.out.println("Servidor: todas las peticiones han sido enviadas a los GM bots.");
    }
}