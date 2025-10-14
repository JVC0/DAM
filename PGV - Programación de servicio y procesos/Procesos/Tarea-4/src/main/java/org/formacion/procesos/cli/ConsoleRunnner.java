package org.formacion.procesos.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

@Component
public class ConsoleRunnner {
    public void actividafinal() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("sh", "-c", "ps aux | grep java");
        pb.redirectOutput(new File("mis_procesos.txt"));
        Process p = pb.start();
        
try (var br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
    br.lines().forEach(System.out::println);
}
        p.waitFor();
        Process pcount = new ProcessBuilder("wc", "-l", "mis_procesos.txt").start();
        try (var br = new BufferedReader(new InputStreamReader(pcount.getInputStream()))) {
            Integer lines=Integer.parseInt(br.readLine().split(" ")[0]);
            if (lines > 3) {
                System.out.println("¡Cuidado, muchos procesos de Java activos!");
            } 
        }
        pcount.waitFor();

}
}
