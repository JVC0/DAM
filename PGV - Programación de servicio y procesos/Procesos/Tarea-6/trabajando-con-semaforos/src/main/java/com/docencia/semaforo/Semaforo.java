package com.docencia.semaforo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Semaforo implements Runnable {
    private static final Semaphore semaphore = new Semaphore(1, true);
    private AtomicBoolean corriendo = new AtomicBoolean(true);

    @Override
    public void run() {
        try {
            while (corriendo) {
                semaphore.acquire();
                System.out.println("ROJO");
                Thread.sleep(3000);

                System.out.println("AMBAR");
                Thread.sleep(1000);

                System.out.println("VERDE");
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
public void stop() {
        running.set(false);
    }
    public static void main(String[] args) throws InterruptedException {
        Thread trafficLightThread = new Thread(new Semaforo());
        trafficLightThread.start();
        Thread.sleep(20000);
         trafficLight.stop();
        trafficLightThread.interrupt();
        trafficLightThread.join();
    }
}
// Haciendo funcionar un semáforo casero

// Simula un semáforo de tráfico con tres estados: ROJO, ÁMBAR, VERDE.

// Crea un programa que:

// Muestra en consola el color actual.
// Espera un tiempo según el color (ROJO 3s, VERDE 3s, ÁMBAR 1s).
// Cambia al siguiente color y repite en bucle. -El programa principal (main)
// debe poder parar la simulación después de 20 segundos.
// Objetivo:

// Trabajar con Thread.sleep() y controlar la vida de un hilo.
// Clase ColorSemaforo:

// color: ROJO, AMBAR, VERDE.
// semaforo: para permitir cambiar de estado de color.