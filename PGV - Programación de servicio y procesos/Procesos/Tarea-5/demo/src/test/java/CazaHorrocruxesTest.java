import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.example.CazaHorrocruxes;

public class CazaHorrocruxesTest {
    @Test
    public void CazaHorrocruxesUnGanadorYUnSoloHallazgo() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        CazaHorrocruxes juego = new CazaHorrocruxes();
        Thread Harry = new Thread(juego.new Buscador("Harry", "Bosque Prohibido"));
        Thread Hermione = new Thread(juego.new Buscador("Hermione", "Biblioteca Antigua"));
        Thread Ron = new Thread(juego.new Buscador("Ron", "Mazmorras del castillo"));

        Harry.start();
        Hermione.start();
        Ron.start();

        Harry.join();
        Hermione.join();
        Ron.join();
        String output = outContent.toString();
        assertTrue(juego.encontrado.get());
        assertNotNull(juego.ganador.get());
        assertTrue(output.contains("encontró un Horrocrux"));
    }
}
