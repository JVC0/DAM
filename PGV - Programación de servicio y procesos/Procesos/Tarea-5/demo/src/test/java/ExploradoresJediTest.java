import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.example.ExploradoresJedi;

public class ExploradoresJediTest {
    @Test
    public void ExploradoresJediUnSoloGanador() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
            ExploradoresJedi explorador = new ExploradoresJedi();
            Thread t1 = new Thread(explorador.new Jedi("Kenobi", "Tatooine"));
            Thread t2 = new Thread(explorador.new Jedi("Skywalker", "Dagobah"));
            t1.start();
            t2.start();

            t1.join();
            t2.join();
        String salida = outContent.toString();

        assertTrue( explorador.pitasEncontrada.get());
        assertTrue( salida.contains("halló una pista"));
    }
}


