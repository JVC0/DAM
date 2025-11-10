package com.example;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class BatallaMages {
    public AtomicInteger energiaGandalf =new AtomicInteger(120);
    public AtomicInteger energiaSaruman =new AtomicInteger(120);
    public AtomicBoolean combateTerminado = new AtomicBoolean(false);
    ReentrantLock mutex = new ReentrantLock();
    public void lanzarHechizo (String atacante, AtomicInteger energiaRival){
        int dano = ThreadLocalRandom.current().nextInt(8, 26);
        energiaRival.addAndGet(-dano);
        System.out.println(atacante + " lanza hechizo por " + dano + ". Energía rival: " + energiaRival.get());
        if (energiaRival.get() <=0 && !combateTerminado.get()) {
            combateTerminado.set(true);
            System.out.println(atacante + " gana la batalla mágica.");
        }
}
    public class Gandalf implements Runnable {
        @Override
        public void run() {
            while (!combateTerminado.get()) {
                mutex.lock();
                if (!combateTerminado.get()) {
                    lanzarHechizo("Gandalf", energiaSaruman);
                }
                mutex.unlock();
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(200, 601));
                } catch (Exception e) {
                    System.out.println("El hilo fue interrumpido.");
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    public class Saruman implements Runnable {
        @Override
        public void run() {
            while (!combateTerminado.get()) {
                mutex.lock();
                if (!combateTerminado.get()) {
                    lanzarHechizo("Gandalf", energiaGandalf);
                }
                mutex.unlock();
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(200, 601));
                } catch (Exception e) {
                    System.out.println("El hilo fue interrumpido.");
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    public static void main(String[] args) throws InterruptedException{
        BatallaMages batalla = new BatallaMages();
        Thread t1 = new Thread(batalla.new Gandalf());
        Thread t2 = new Thread(batalla.new Saruman());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
