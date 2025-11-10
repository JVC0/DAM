package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;



public class Tardis {
    public AtomicBoolean destinoAlcanzado = new AtomicBoolean(false);
    public AtomicReference<String> eraGanadora = new AtomicReference<>(null);

    public class Viaje implements Runnable {
        String era;

        public Viaje(String era) {
            this.era = era;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(500, 2001));
                if (!destinoAlcanzado.get()) {
                    destinoAlcanzado.set(true);
                    eraGanadora.set(era);
                    System.out.println("La TARDIS llegó primero a " + era);
                }
            } catch (Exception e) {
                System.out.println("El hilo fue interrumpido.");
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Tardis tardis = new Tardis();
        String[] eras = {"Roma Antigua", "Futuro Lejano", "Era Victoriana", "Año 3000"};
        List<Thread> hilos =new ArrayList<>();;
        for (String e : eras) {
            Thread t = new Thread(tardis.new Viaje(e));
            hilos.add(t);
            t.start();
        }
        for (Thread t : hilos) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Hilo principal interrumpido");
                Thread.currentThread().interrupt();
            }
        }
    }
}
