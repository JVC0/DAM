
package com.example;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class FabricaDroids {
    LinkedBlockingQueue<String> ensamblados = new LinkedBlockingQueue<>();
    public int N = 10;
    public AtomicInteger activados = new AtomicInteger(0);

    public class Ensamblador implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < N; i++) {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(100, 301));
                    System.out.println("Ensamblado " + i);
                    String droid = "Droid-" + i;
                    ensamblados.put(droid);
                } catch (Exception e) {
                    System.out.println("El hilo fue interrumpido.");
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public class Activador implements Runnable {

        @Override
        public void run() {
            int cuenta = 0;
            while (cuenta < N) {
                try {
                    String droid = ensamblados.take();
                    System.out.println("Activado" + droid);
                    activados.incrementAndGet();
                    cuenta = cuenta + 1;
                    Thread.sleep(ThreadLocalRandom.current().nextInt(50, 151));
                } catch (Exception e) {
                    System.out.println("El hilo fue interrumpido.");
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        FabricaDroids fabrica = new FabricaDroids();
        Thread ensamblador = new Thread(fabrica.new Ensamblador());
        Thread activador = new Thread(fabrica.new Activador());

        ensamblador.start();
        activador.start();

        ensamblador.join();
        activador.join();
    }

}
