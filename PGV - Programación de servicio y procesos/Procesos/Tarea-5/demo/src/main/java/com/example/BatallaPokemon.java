package com.example;

import java.util.concurrent.locks.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class BatallaPokemon {
    public volatile boolean juegoTerminado = false;
    public AtomicInteger hpPikachu = new AtomicInteger(100);
    public AtomicInteger hpCharmander = new AtomicInteger(100);
    String turno = "Pikachu";
    ReentrantLock mutex = new ReentrantLock();
    Condition turnoCambio = mutex.newCondition();

    public void atacar(String atacante, AtomicInteger hpObjetivo) {
        int daño = ThreadLocalRandom.current().nextInt(5, 21);
        hpObjetivo.addAndGet(-daño);
        System.out.println(atacante + " ataca con " + daño + " de daño. HP rival: " + hpObjetivo);
        if (hpObjetivo.get() <= 0 && !juegoTerminado) {
            juegoTerminado = true;
            System.out.println(atacante + " ha ganado la batalla!");
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(200, 601));
            } catch (Exception e) {
                System.out.println("El hilo fue interrumpido.");
                Thread.currentThread().interrupt();
            }
        }
    }

    public class HiloPikachu implements Runnable {
        @Override
        public void run() {
            while (!juegoTerminado) {
                mutex.lock();
                try {
                    while (!turno.equals("Pikachu") && !juegoTerminado) {
                        turnoCambio.await();
                    }
                    if (!juegoTerminado) {
                        atacar("Pikachu", hpCharmander);
                        turno = "Charmander";
                        turnoCambio.signal();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    mutex.unlock();
                }
            }
        }
    }

    public class HiloCharmander implements Runnable {
        @Override
        public void run() {
            while (!juegoTerminado) {
                mutex.lock();
                try {
                    while (!turno.equals("Charmander") && !juegoTerminado) {
                        turnoCambio.await();
                    }
                    if (!juegoTerminado) {
                        atacar("Charmander", hpPikachu);
                        turno = "Pikachu";
                        turnoCambio.signal();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    mutex.unlock();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BatallaPokemon batalla = new BatallaPokemon();
        Thread pikachu = new Thread(batalla.new HiloPikachu());
        Thread charmander = new Thread(batalla.new HiloCharmander());

        pikachu.start();
        charmander.start();

        pikachu.join();
        charmander.join();
    }

}
