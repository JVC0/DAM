package com.example;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MilleniumFalcon {
    public AtomicBoolean fin = new AtomicBoolean(false);
    public AtomicBoolean destruida = new AtomicBoolean(false);
    int tiempoMisionMS = 4000;
    public long inicio;
    public AtomicInteger velocidad = new AtomicInteger(0);
    public AtomicInteger escudos = new AtomicInteger(100);

    public class HanSolo implements Runnable {
        @Override
        public void run() {
            while (!fin.get()) {
                velocidad.addAndGet(ThreadLocalRandom.current().nextInt(5, 16));
                if (ThreadLocalRandom.current().nextInt(1, 101) <= 5) {
                    destruida.set(true);
                    fin.set(true);
                    System.out.println("Fallo de hiperimpulsor. ¡La nave se destruye!");
                }
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    System.out.println("El hilo fue interrumpido.");
                    Thread.currentThread().interrupt();
                }
                if (System.currentTimeMillis() - inicio >= tiempoMisionMS) {
                    fin.set(true);
                }
            }
        }
    }

    public class Chewbacca implements Runnable {
        @Override
        public void run() {
            while (!fin.get()) {
                escudos.addAndGet(ThreadLocalRandom.current().nextInt(-10, 6));
                if (escudos.get() <= 0) {
                    destruida.set(true);
                    fin.set(true);
                    System.out.println("¡Escudos agotados! La nave se destruye!");
                }
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    System.out.println("El hilo fue interrumpido.");
                    Thread.currentThread().interrupt();
                }
                if (System.currentTimeMillis() - inicio >= tiempoMisionMS) {
                    fin.set(true);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MilleniumFalcon ship = new MilleniumFalcon();
        ship.inicio = System.currentTimeMillis();
        Thread t1 = new Thread(ship.new HanSolo());
        Thread t2 = new Thread(ship.new Chewbacca());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        if (!ship.destruida.get()) {
            System.out
                    .println("¡Han y Chewie escapan! Vel=" + ship.velocidad.get() + ", Escudos=" + ship.escudos.get());
        }
    }
}