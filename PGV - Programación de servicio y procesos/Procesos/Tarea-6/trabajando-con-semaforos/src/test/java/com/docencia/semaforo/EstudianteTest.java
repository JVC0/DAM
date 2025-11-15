package com.docencia.semaforo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class EstudianteTest {
    @Test
    public void estudianteTest()throws InterruptedException{
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));


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
        String output = outContent.toString();
        Assertions.assertTrue(output.contains("El estudiante Carlos ha comenzado a utilizar el equipo"));
        Assertions.assertTrue(output.contains("El estudiante Ana ha finalizado con el equipo "));
    }
}

