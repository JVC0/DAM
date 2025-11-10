import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.Tardis;

public class TardisTest {
    @Test
    public void TardisExisteUnaEraGanadora() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Tardis tardis = new Tardis();
        String[] eras = {"Roma Antigua", "Futuro Lejano", "Era Victoriana", "Año 3000"};
        List<Thread> hilos =new ArrayList<>();;
        for (String e : eras) {
            Thread t = new Thread(tardis.new Viaje(e));
            hilos.add(t);
            t.start();
        }
        for (Thread t : hilos) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Hilo principal interrumpido");
                Thread.currentThread().interrupt();
            }
        }
        String output = outContent.toString();
        assertTrue(tardis.destinoAlcanzado.get());
        assertNotNull(tardis.eraGanadora.get());
        assertTrue(output.contains("llegó primero"));
    }
}