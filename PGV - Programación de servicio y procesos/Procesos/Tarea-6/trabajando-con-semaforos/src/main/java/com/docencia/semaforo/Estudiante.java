package com.docencia.semaforo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Estudiante  implements Runnable {
    private final String nombre;
    private static final Semaphore semaphore = new Semaphore(4, true);

    public Estudiante (String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            System.out.println("El estudiante " +nombre+ " ha comenzado a utilizar el equipo");
            Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 6000) );
            System.out.println("El estudiante " +nombre+ " ha finalizado con el equipo");
            semaphore.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
    Thread student1 = new Thread(new Estudiante("Juan"));
    Thread student2 = new Thread(new Estudiante("Maria"));
    Thread student3 = new Thread(new Estudiante("Carlos"));
    Thread student4 = new Thread(new Estudiante("Ana"));
    Thread student5 = new Thread(new Estudiante("Pedro"));
    Thread student6 = new Thread(new Estudiante("Laura"));
    Thread student7 = new Thread(new Estudiante("David"));
    Thread student8 = new Thread(new Estudiante("Sofia"));
        student1.start();
        student2.start();
        student3.start();
        student4.start();
        student5.start();
        student6.start();
        student7.start();
        student8.start();
        student1.join();
        student2.join();
        student3.join();
        student4.join();
        student5.join();
        student6.join();
        student7.join();
        student8.join();

    }
}
