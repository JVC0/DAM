import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.example.BatallaPokemon;

public class BatallaPokemonTest {
    @Test
    public void BatallaPokemon_debeHaberGanador() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        BatallaPokemon batalla = new BatallaPokemon();
        Thread pikachu = new Thread(batalla.new HiloPikachu());
        Thread charmander = new Thread(batalla.new HiloCharmander());

        pikachu.start();
        charmander.start();

        pikachu.join();
        charmander.join();
        String output = outContent.toString();
        assertTrue(batalla.hpPikachu.get() <= 0 || batalla.hpCharmander.get() <= 0);
        assertTrue(batalla.juegoTerminado == true);
        assertTrue(output.contains("ha ganado la batalla! A="));
    }
}
