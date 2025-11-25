package org.docencia.hilos;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpawnsMundoAbierto {

    static class SpawnTarea implements Runnable {

        private final String[] zonas = {
                "Bosque Maldito",
                "Ruinas Antiguas",
                "Pantano Radiactivo",
                "Ciudad Cibernética",
                "Templo Prohibido"
        };

        private final String[] enemigosComunes = {
                "Slime Mutante",
                "Esqueleto Guerrero",
                "Mecha-Dragón",
                "Bandido del Desierto",
                "Lich Supremo"
        };
 private final String[] enemigosRaros = {
                "Mecha-Dragon",
                "Lich Supremo",
                "Cazador de Sombras",
                "Titan Ancestral",
                "Serpiente Cosmica"
        };

        private final String[] enemigosEpicos = {
                "Dios Primigenio",
                "Entidad del Vacio",
                "Guardian del Abismo"
        };
        @Override
        public void run() {
            String hilo = Thread.currentThread().getName();
            String zona = zonas[(int)(Math.random() * zonas.length)];
            int probabilidad = (int)(Math.random() * 100);
            if (probabilidad < 0.10) {
                String enemigo = enemigosEpicos[(int)(Math.random() * enemigosEpicos.length)];
                System.out.println("[" + LocalTime.now() + "][" + hilo + "] ¡¡SPAWN ÉPICO DE " +
                        enemigo + " en " + zona + "!!");
                return;
            } else if (probabilidad < 0.40) {
                String enemigo = enemigosRaros[(int)(Math.random() * enemigosRaros.length)];
                System.out.println("[" + LocalTime.now() + "][" + hilo + "] Spawn raro de " +
                        enemigo + " en " + zona);
            }
            String enemigo = enemigosComunes[(int)(Math.random() * enemigosComunes.length)];

            System.out.println("[" + LocalTime.now() + "][" + hilo + "] Spawn de " +
                    enemigo + " en " + zona);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // Ejecutar la tarea cada 2 segundos, sin retraso inicial
        scheduler.scheduleAtFixedRate(new SpawnTarea(), 0, 2, TimeUnit.SECONDS);

        // Dejamos que el mundo “viva” durante 12 segundos
        Thread.sleep(12000);

        // Apagado ordenado
        System.out.println("Deteniendo spawns...");
        scheduler.shutdown();
        if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("Forzando parada de spawns.");
            scheduler.shutdownNow();
        }
        System.out.println("Servidor de mundo abierto detenido.");
    }
}