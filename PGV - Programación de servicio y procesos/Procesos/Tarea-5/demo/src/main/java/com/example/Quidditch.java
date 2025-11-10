package com.example;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class Quidditch {
    public AtomicBoolean snitchAtrapada = new AtomicBoolean(false);
    public int puntosEquipoA = 0;
    public int puntosEquipoB = 0;
    ReentrantLock mutex = new ReentrantLock();

    public class CazadorA implements Runnable {
        @Override
        public void run() {
            while (!snitchAtrapada.get()) {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(200, 501));
                    int g = ThreadLocalRandom.current().nextInt(0, 2) * 10;
                    if (g > 0) {
                        mutex.lock();
                        puntosEquipoA += g;
                        System.out.println("Equipo A anota 10. Total A=" + puntosEquipoA);
                        mutex.unlock();
                    }
                } catch (Exception e) {
                    System.out.println("El hilo fue interrumpido.");
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public class CazadorB implements Runnable {
        @Override
        public void run() {
            while (!snitchAtrapada.get()) {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(200, 501));
                    int g = ThreadLocalRandom.current().nextInt(0, 2) * 10;
                    if (g > 0) {
                        mutex.lock();
                        puntosEquipoB += g;
                        System.out.println("Equipo B anota 10. Total B=" + puntosEquipoB);
                        mutex.unlock();
                    }
                } catch (Exception e) {
                    System.out.println("El hilo fue interrumpido.");
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public class Buscador implements Runnable {

        @Override
        public void run() {
            while (!snitchAtrapada.get()) {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(300, 701));
                    if (ThreadLocalRandom.current().nextInt(1, 101) <= 15) {
                        snitchAtrapada.set(true);
                        System.out.println("¡Snitch dorada atrapada!");
                        System.out.println("Marcador final: A=" + puntosEquipoA + " B=" + puntosEquipoB);
                    }
                } catch (Exception e) {
                    System.out.println("El hilo fue interrumpido.");
                    Thread.currentThread().interrupt();
                }
            }
        }

    }
    public static void main(String[] args) throws InterruptedException {
        Quidditch juego = new Quidditch();
        Thread t1 = new Thread(juego.new CazadorA());
        Thread t2 = new Thread(juego.new CazadorB());
        Thread t3 = new Thread(juego.new Buscador());
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }

}

