import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.example.CiudadEnPeligro;


public class CiudadEnPeligroTest {
    @Test
    public void CiudadEnPeligroSoloNeutralizaElOtroSeDetiene() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        CiudadEnPeligro ciudad = new CiudadEnPeligro();
        Thread t1 = new Thread(ciudad.new Superman());
        Thread t2 = new Thread(ciudad.new Batman());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        String output = outContent.toString();
        assertTrue(ciudad.amenazaNeutralizada.get());
        assertTrue(Set.of("Superman", "Batman").contains(ciudad.ganador.get()));
        assertTrue(output.contains("Amenaza neutralizada"));
    }
}
