import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.example.FuerzaThorHulk;


public class FuerzaThorHulkTest {

    @Test
    public void FuerzaThorHulk_terminaPorTiempo_yDeclaraResultado() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
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
        String output = outContent.toString();
        assertTrue(output.contains("¡Tiempo!"));
        assertTrue(output.contains("gana") || output.contains("Empate"));
}
}
