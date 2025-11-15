package com.docencia.semaforo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class EstudianteMejorado  implements Runnable {
    private final String nombre;
    private static final Semaphore semaphore = new Semaphore(4, true);
    public EstudianteMejorado (String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            int equipoUsado = semaphore.availablePermits() + 1;
            System.out.println("El estudiante " +nombre+ " ha comenzado a utilizar el equipo "+equipoUsado);
            Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 6000) );
            System.out.println("El estudiante " +nombre+ " ha finalizado con el equipo ");
            semaphore.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
    Thread student1 = new Thread(new EstudianteMejorado("Juan"));
    Thread student2 = new Thread(new EstudianteMejorado("Maria"));
    Thread student3 = new Thread(new EstudianteMejorado("Carlos"));
    Thread student4 = new Thread(new EstudianteMejorado("Ana"));
    Thread student5 = new Thread(new EstudianteMejorado("Pedro"));
    Thread student6 = new Thread(new EstudianteMejorado("Laura"));
    Thread student7 = new Thread(new EstudianteMejorado("David"));
    Thread student8 = new Thread(new EstudianteMejorado("Sofia"));
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
