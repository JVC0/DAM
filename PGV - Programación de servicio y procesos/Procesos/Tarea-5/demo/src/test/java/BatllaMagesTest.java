import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.example.BatallaMages;


public class BatllaMagesTest {
    @Test
    public void BatallaMagos_debeHaberGanadorYTerminar() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        BatallaMages batalla = new BatallaMages();
        Thread t1 = new Thread(batalla.new Gandalf());
        Thread t2 = new Thread(batalla.new Saruman());
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        String output = outContent.toString();
        assertTrue(output.contains("gana la batalla mágica."));
        assertTrue(batalla.combateTerminado.get());
        assertTrue(batalla.energiaGandalf.get() <= 0 || batalla.energiaSaruman.get() <= 0);
    }
}