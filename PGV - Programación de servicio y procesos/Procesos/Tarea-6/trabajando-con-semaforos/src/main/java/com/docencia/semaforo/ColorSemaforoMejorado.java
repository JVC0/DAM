package com.docencia.semaforo;

import java.util.concurrent.Semaphore;

public class ColorSemaforoMejorado implements Runnable {
    private final String color;
    private static final Semaphore redTurn = new Semaphore(1);
    private static final Semaphore greenTurn = new Semaphore(0);
    private static final Semaphore amberTurn = new Semaphore(0);
    final int seconds = 20000;
    public ColorSemaforoMejorado(String color) {
        this.color = color;
    }

    @Override
    public void run() {
        final boolean isRed = "ROJO".equals(color.toUpperCase());
        final boolean isAmber = "AMBAR".equals(color.toUpperCase());
        final boolean isGreen = "VERDE".equals(color.toUpperCase());
        final Semaphore myTurn = isRed ? redTurn : (isGreen ? greenTurn : amberTurn);
        final Semaphore nextTurn = isRed ? greenTurn : (isGreen ? amberTurn : redTurn);
        long startime = System.currentTimeMillis();
        try {
            while (System.currentTimeMillis() - startime < seconds) {
                myTurn.acquire();
                System.out.println("Color actual: " + color);
                if (isRed || isGreen) {
                    Thread.sleep(3000);
                } else if (isAmber) {
                    Thread.sleep(1000);
                }
                nextTurn.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread redTrafficLightThread = new Thread(new ColorSemaforoMejorado("ROJO"));
        Thread greenTrafficLightThread = new Thread(new ColorSemaforoMejorado("VERDE"));
        Thread amberTrafficLightThread = new Thread(new ColorSemaforoMejorado("AMBAR"));
        redTrafficLightThread.start();
        greenTrafficLightThread.start();
        amberTrafficLightThread.start();
        greenTrafficLightThread.join();
        redTrafficLightThread.join();
        amberTrafficLightThread.join();
    }
}
