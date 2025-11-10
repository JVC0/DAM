import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.example.MilleniumFalcon;


public class MilleniumFalconTest {
    @Test
    public void BatallaPokemon_debeHaberGanador() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        MilleniumFalcon ship = new MilleniumFalcon();
        ship.inicio = System.currentTimeMillis();
        Thread t1 = new Thread(ship.new HanSolo());
        Thread t2 = new Thread(ship.new Chewbacca());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        if (!ship.destruida.get()) {
            System.out
                    .println("¡Han y Chewie escapan! Vel=" + ship.velocidad.get() + ", Escudos=" + ship.escudos.get());
        }
        String output = outContent.toString();
        assertTrue(output.contains("se destruye") || output.contains("escapan"));
    }
}