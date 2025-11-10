
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.example.Quidditch;


public class QuidditchTest {
    @Test
    public void Quidditch_terminaCuandoSnitchAtrapada() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Quidditch quidditch = new Quidditch();
        Thread t1 = new Thread(quidditch.new CazadorA());
        Thread t2 = new Thread(quidditch.new CazadorB());
        Thread t3 = new Thread(quidditch.new Buscador());
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        String output = outContent.toString();
        assertTrue(output.contains("¡Snitch dorada atrapada!"));
        assertTrue(output.contains("Marcador final:"));
    }
}
