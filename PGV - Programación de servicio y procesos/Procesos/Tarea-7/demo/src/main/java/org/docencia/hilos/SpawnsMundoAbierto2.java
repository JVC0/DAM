package org.docencia.hilos;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpawnsMundoAbierto2 {
    
    // Enums para las constantes
    enum Zona {
        BOSQUE_MALDITO("Bosque Maldito"),
        RUINAS_ANTIGUAS("Ruinas Antiguas"),
        PANTANO_RADIACTIVO("Pantano Radiactivo"),
        CIUDAD_CIBERNETICA("Ciudad Cibernética"),
        TEMPLO_PROHIBIDO("Templo Prohibido");
        
        private final String nombre;
        
        Zona(String nombre) {
            this.nombre = nombre;
        }
        
        public String getNombre() {
            return nombre;
        }
        
        public static Zona getAleatoria() {
            return values()[(int)(Math.random() * values().length)];
        }
    }
    
    enum EnemigoComun {
        SLIME_MUTANTE("Slime Mutante"),
        ESQUELETO_GUERRERO("Esqueleto Guerrero"),
        MECHA_DRAGON("Mecha-Dragón"),
        BANDIDO_DESIERTO("Bandido del Desierto"),
        LICH_SUPREMO("Lich Supremo");
        
        private final String nombre;
        
        EnemigoComun(String nombre) {
            this.nombre = nombre;
        }
        
        public String getNombre() {
            return nombre;
        }
        
        public static EnemigoComun getAleatorio() {
            return values()[(int)(Math.random() * values().length)];
        }
    }
    
    enum EnemigoRaro {
        MECHA_DRAGON("Mecha-Dragon"),
        LICH_SUPREMO("Lich Supremo"),
        CAZADOR_SOMBRAS("Cazador de Sombras"),
        TITAN_ANCESTRAL("Titan Ancestral"),
        SERPIENTE_COSMICA("Serpiente Cosmica");
        
        private final String nombre;
        
        EnemigoRaro(String nombre) {
            this.nombre = nombre;
        }
        
        public String getNombre() {
            return nombre;
        }
        
        public static EnemigoRaro getAleatorio() {
            return values()[(int)(Math.random() * values().length)];
        }
    }
    
    enum EnemigoEpico {
        DIOS_PRIMIGENIO("Dios Primigenio"),
        ENTIDAD_VACIO("Entidad del Vacio"),
        GUARDIAN_ABISMO("Guardian del Abismo");
        
        private final String nombre;
        
        EnemigoEpico(String nombre) {
            this.nombre = nombre;
        }
        
        public String getNombre() {
            return nombre;
        }
        
        public static EnemigoEpico getAleatorio() {
            return values()[(int)(Math.random() * values().length)];
        }
    }
    
    // Constantes para probabilidades
    private static final double PROBABILIDAD_EPICO = 0.10;
    private static final double PROBABILIDAD_RARO = 0.40;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    static class SpawnTarea implements Runnable {
        
        @Override
        public void run() {
            String hilo = Thread.currentThread().getName();
            Zona zona = Zona.getAleatoria();
            double probabilidad = Math.random();
            
            if (probabilidad < PROBABILIDAD_EPICO) {
                EnemigoEpico enemigo = EnemigoEpico.getAleatorio();
                logSpawnEpico(hilo, enemigo.getNombre(), zona.getNombre());
                return;
            } else if (probabilidad < PROBABILIDAD_RARO) {
                EnemigoRaro enemigo = EnemigoRaro.getAleatorio();
                logSpawnRaro(hilo, enemigo.getNombre(), zona.getNombre());
                return;
            }
            
            EnemigoComun enemigo = EnemigoComun.getAleatorio();
            logSpawnComun(hilo, enemigo.getNombre(), zona.getNombre());
        }
        
        // Métodos de log con formato específico
        private void logSpawnEpico(String hilo, String enemigo, String zona) {
            System.out.println(String.format("[%s] [SPAWN_EPICO] ¡¡SPAWN ÉPICO DE %s en %s!! (Hilo: %s)",
                    getTimestamp(), enemigo, zona, hilo));
        }
        
        private void logSpawnRaro(String hilo, String enemigo, String zona) {
            System.out.println(String.format("[%s] [SPAWN_RARO] Spawn raro de %s en %s (Hilo: %s)",
                    getTimestamp(), enemigo, zona, hilo));
        }
        
        private void logSpawnComun(String hilo, String enemigo, String zona) {
            System.out.println(String.format("[%s] [SPAWN_COMUN] Spawn de %s en %s (Hilo: %s)",
                    getTimestamp(), enemigo, zona, hilo));
        }
        
        private String getTimestamp() {
            return LocalTime.now().format(TIME_FORMATTER);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        
        // Log de inicio del sistema
        System.out.println(String.format("[%s] [SISTEMA] Iniciando servidor de mundo abierto...",
                LocalTime.now().format(TIME_FORMATTER)));
        
        // Ejecutar la tarea cada 2 segundos, sin retraso inicial
        scheduler.scheduleAtFixedRate(new SpawnTarea(), 0, 2, TimeUnit.SECONDS);
        
        // Log de inicio de spawns
        System.out.println(String.format("[%s] [SISTEMA] Spawns programados cada 2 segundos",
                LocalTime.now().format(TIME_FORMATTER)));

        // Dejamos que el mundo "viva" durante 12 segundos
        Thread.sleep(12000);
        
        // Log de parada
        System.out.println(String.format("[%s] [SISTEMA] Deteniendo spawns...",
                LocalTime.now().format(TIME_FORMATTER)));
        
        // Apagado ordenado
        scheduler.shutdown();
        if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println(String.format("[%s] [SISTEMA_ERROR] Forzando parada de spawns.",
                    LocalTime.now().format(TIME_FORMATTER)));
            scheduler.shutdownNow();
        }
        
        System.out.println(String.format("[%s] [SISTEMA] Servidor de mundo abierto detenido.",
                LocalTime.now().format(TIME_FORMATTER)));
    }
}