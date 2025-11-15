package com.docencia.semaforo;

import java.util.concurrent.Semaphore;

public class Semaforo implements Runnable {
    private final String color;
    private static final Semaphore semaphore = new Semaphore(1, true);

    public Semaforo(String color) {
        this.color = color;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            System.out.println("Color actual: " + color);

            if ("ROJO".equals(color.toUpperCase())) {
                Thread.sleep(3000);
            } else if ("VERDE".equals(color.toUpperCase())) {
                Thread.sleep(3000);
            } else if ("AMBAR".equals(color.toUpperCase())) {
                Thread.sleep(1000);
            }

            semaphore.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int iterations = 3;


        for (int i = 0; i < iterations; i++) {
            Thread redTrafficLightThread = new Thread(new Semaforo("ROJO"));
            Thread greenTrafficLightThread = new Thread(new Semaforo("VERDE"));
            Thread amberTrafficLightThread = new Thread(new Semaforo("AMBAR"));

            redTrafficLightThread.start();
            greenTrafficLightThread.start();
            amberTrafficLightThread.start();
            greenTrafficLightThread.join();
            redTrafficLightThread.join();
            amberTrafficLightThread.join();
        }

    }
}
