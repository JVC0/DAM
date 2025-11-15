package com.docencia.semaforo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/* */
public class SemaforoTest {
    @Test
    public void semaforoTest()throws InterruptedException{
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        int iterations = 2;


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
        String output = outContent.toString();
        Assertions.assertTrue(output.contains("ROJO") && output.contains("VERDE")&& output.contains("AMBAR"));
    }
}

