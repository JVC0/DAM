import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.example.FabricaDroids;

public class FabricaDroidsTest {
    @Test
    public void FabricaDroidsNoSeActivaAntesDeEnsamblaryCuentaCorrecta() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        FabricaDroids fabrica = new FabricaDroids();
        Thread ensamblador = new Thread(fabrica.new Ensamblador());
        Thread activador = new Thread(fabrica.new Activador());

        ensamblador.start();
        activador.start();

        ensamblador.join();
        activador.join();

        String salida = outContent.toString();
        for (int i = 0; i < fabrica.N; i++) {
            String ensambladoStr = "Ensamblado " + i;
            String activadoStr = "ActivadoDroid-" + i;

            int idxE = salida.indexOf(ensambladoStr);
            int idxA = salida.indexOf(activadoStr);

            assertTrue(idxE != -1, "Droid-" + i + " no fue ensamblado");
            assertTrue(idxA != -1, "Droid-" + i + " no fue activado");
            assertTrue(idxE < idxA, "Droid-" + i + " fue activado antes de ser ensamblado");
        }
        assertEquals(fabrica.N, fabrica.activados.get(), "Número incorrecto de droids activados");
    }

}
