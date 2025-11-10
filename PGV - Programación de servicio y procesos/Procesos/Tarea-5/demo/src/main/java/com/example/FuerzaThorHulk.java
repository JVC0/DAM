package com.example;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class FuerzaThorHulk {
    int durationMS= 5000;
    public AtomicBoolean tiempoTerminado= new AtomicBoolean(false);
    public AtomicInteger totalThor = new AtomicInteger(0);
    public AtomicInteger totalHulk = new AtomicInteger(0);
    public class Temporizador implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(durationMS);
                tiempoTerminado.set(true);
                System.out.println("¡Tiempo!");
            } catch (Exception e) {
                System.out.println("El hilo fue interrumpido.");
                Thread.currentThread().interrupt();
            }
        }
    }
    public class Thor implements Runnable{
        @Override
        public void run() {
            while (!tiempoTerminado.get()) {
                int peso = ThreadLocalRandom.current().nextInt(200, 501);
                totalThor.addAndGet(peso);
                try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(50, 120));
            } catch (Exception e) {
                System.out.println("El hilo fue interrumpido.");
                Thread.currentThread().interrupt();
            }
            }
        }
    }
    public class Hulk implements Runnable{

        @Override
        public void run() {
            while (!tiempoTerminado.get()) {
                int peso = ThreadLocalRandom.current().nextInt(200, 501);
                totalHulk.addAndGet(peso);
                try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(50, 120));
            } catch (Exception e) {
                System.out.println("El hilo fue interrumpido.");
                Thread.currentThread().interrupt();
            }
            }
        }
    }
    public static void main(String[] args) throws InterruptedException{
        FuerzaThorHulk fuerza = new FuerzaThorHulk();
        Thread t1 = new Thread(fuerza.new Temporizador());
        Thread t2 = new Thread(fuerza.new Thor());
        Thread t3 = new Thread(fuerza.new Hulk());
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        if (fuerza.totalThor.get()>fuerza.totalHulk.get()) {
            System.out.println("Thor gana con " + fuerza.totalThor.get() + " vs " + fuerza.totalHulk.get());
        }else if (fuerza.totalHulk.get()>fuerza.totalThor.get()) {
            System.out.println("Hulk gana con " + fuerza.totalHulk.get() + " vs " + fuerza.totalThor.get());
        }else{
            System.out.println("Empate: " + fuerza.totalThor.get());
        }
    }
}