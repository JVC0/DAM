package com.example;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


public class CiudadEnPeligro {
    public AtomicBoolean amenazaNeutralizada = new AtomicBoolean(false);
    String[] zonasA = { "Norte", "Centro", "Este" };
    String[] zonasB = { "Oeste", "Sur" };
    public AtomicReference<String> ganador = new AtomicReference<>(null);

    public class Superman implements Runnable {
        @Override
        public void run() {
            for (String zona : zonasA) {
                if (amenazaNeutralizada.get()) {
                    break;
                }
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(200, 501));
                    System.out.println("Superman salvó " + zona);
                    if (!amenazaNeutralizada.get()) {
                        amenazaNeutralizada.set(true);
                        ganador.set("Superman");
                        System.out.println("Amenaza neutralizada por Superman");
                    }
                } catch (Exception e) {
                    System.out.println("El hilo fue interrumpido.");
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public class Batman implements Runnable {
        @Override
        public void run() {
            for (String zona : zonasB) {
                if (amenazaNeutralizada.get()) {
                    break;
                }
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(300, 601));
                    System.out.println("Batman salvó " + zona);
                    if (!amenazaNeutralizada.get()) {
                        amenazaNeutralizada.set(true);
                        ganador.set("Batman");
                        System.out.println("Amenaza neutralizada por Batman");
                    }
                } catch (Exception e) {
                    System.out.println("El hilo fue interrumpido.");
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    public static void main(String[] args) throws InterruptedException{
        CiudadEnPeligro ciudad = new CiudadEnPeligro();
        Thread t1 = new Thread(ciudad.new Superman());
        Thread t2 = new Thread(ciudad.new Batman());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

}