package com.docencia.semaforo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class ColorSemaforoMejoradoTest {
    @Test
    public void colorSemaforoMejoradoTest()throws InterruptedException{
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Thread redTrafficLightThread = new Thread(new ColorSemaforoMejorado("ROJO"));
        Thread greenTrafficLightThread = new Thread(new ColorSemaforoMejorado("VERDE"));
        Thread amberTrafficLightThread = new Thread(new ColorSemaforoMejorado("AMBAR"));
        redTrafficLightThread.start();
        greenTrafficLightThread.start();
        amberTrafficLightThread.start();
        greenTrafficLightThread.join();
        redTrafficLightThread.join();
        amberTrafficLightThread.join();
        String output = outContent.toString();
        String[] sequence= output.split("\\n");
        Assertions.assertTrue(output.contains("ROJO") && output.contains("VERDE")&& output.contains("AMBAR"));
        Assertions.assertTrue(sequence[0].contains("ROJO") && sequence[1].contains("VERDE")&& sequence[2].contains("AMBAR"));
        Assertions.assertTrue(sequence[6].contains("ROJO") && sequence[7].contains("VERDE")&& sequence[8].contains("AMBAR"));
    }
}

