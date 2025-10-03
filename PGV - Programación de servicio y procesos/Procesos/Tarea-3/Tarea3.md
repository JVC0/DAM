# <img src=../../../../../images/computer.png width="40"> Code, Learn & Practice(Programación de Servicios y Procesos: "Comenzando con Procesos en Java")

## 1️ Lanzar un proceso y capturar salida

**Ejemplo:** ejecutar `ls -l` desde Java y mostrar la salida.

```java
ProcessBuilder pb = new ProcessBuilder("ls", "-l");
pb.redirectErrorStream(true);
Process p = pb.start();

try (var br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
    br.lines().forEach(System.out::println);
}
System.out.println("Exit code: " + p.waitFor());
```

```bash
# Comando equivalente en la terminal
ls -l
```
### Salida

![alt text](image.png)
---

## 2️ Usar un *shell* para comandos compuestos

**Ejemplo:** contar cuántos procesos de `java` están corriendo.

```java
ProcessBuilder pb = new ProcessBuilder("sh", "-c", "ps aux | grep java | wc -l");
Process p = pb.start();

try (var br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
    br.lines().forEach(System.out::println);
}
```

```bash
# Comando equivalente en la terminal
ps aux | grep java | wc -l
```
### Salida

![alt text](image-1.png)

---

## 3️ Redirigir salida a un fichero

**Ejemplo:** guardar el listado de procesos en un archivo `procesos.txt`.

```java
ProcessBuilder pb = new ProcessBuilder("ps", "aux");
pb.redirectOutput(new File("procesos.txt"));
Process p = pb.start();
p.waitFor();
```

```bash
# Comando equivalente en la terminal
ps aux > procesos.txt
```


![alt text](image-2.png)
---

## 4️ Leer *stderr* y evitar bloqueos

**Ejemplo:** ejecutar un comando que falle (`ls carpeta_que_no_existe`).

```java
ProcessBuilder pb = new ProcessBuilder("ls", "carpeta_que_no_existe");
pb.redirectErrorStream(false);
Process p = pb.start();

try (var err = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
    err.lines().forEach(line -> System.err.println("ERROR: " + line));
}
```

```bash
# Comando equivalente en la terminal
ls carpeta_que_no_existe
```
![alt text](image-3.png)

---

## 5️ Escribir en la entrada (`stdin`) del proceso

**Ejemplo:** enviar líneas a `sort` y recibirlas ordenadas.

```java
Process p = new ProcessBuilder("sort").start();

try (var w = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()))) {
    w.write("pera\nmanzana\nuva\n");
}

try (var r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
    r.lines().forEach(System.out::println);
}
```

```bash
# Comando equivalente en la terminal
echo -e "pera\nmanzana\nuva" | sort
```

![alt text](image-4.png)

---

## 6️ Controlar el entorno y directorio de trabajo

**Ejemplo:** ejecutar `pwd` desde `/tmp`.

```java
ProcessBuilder pb = new ProcessBuilder("pwd");
pb.directory(new File("/tmp"));
Process p = pb.start();

try (var br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
    br.lines().forEach(System.out::println);
}
```

```bash
# Comando equivalente en la terminal
(cd /tmp && pwd)
```

![alt text](image-5.png)

**Ejemplo con variable de entorno:**

```java
ProcessBuilder pb = new ProcessBuilder("printenv", "MI_VAR");
pb.environment().put("MI_VAR", "Hola desde Java");
Process p = pb.start();

try (var br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
    br.lines().forEach(System.out::println);
}
```

```bash
# Comando equivalente en la terminal
MI_VAR="Hola desde Java" printenv MI_VAR
```

![alt text](image-6.png)

---

## 7️ Controlar tiempo de ejecución y matar procesos

**Ejemplo:** lanzar `sleep 10` pero cancelarlo a los 3 segundos.

```java
Process p = new ProcessBuilder("sleep", "10").start();

if (!p.waitFor(3, java.util.concurrent.TimeUnit.SECONDS)) {
    System.out.println("¡Demasiado lento! Matando proceso...");
    p.destroyForcibly();
}
```

```bash
# Comando equivalente en la terminal
sleep 10
# (Ctrl+C para interrumpir manualmente)
```

![alt text](image-7.png)

---



---

## 🎓 Actividad final

Diseña un programa en Java que:

1. Liste los procesos (`ps aux`).
2. Filtre solo los que contengan `java`.
3. Guarde el resultado en `mis_procesos.txt`.
4. Muestre en pantalla cuántas líneas tiene el archivo (`wc -l`).
5. Si hay más de 3 procesos `java`, imprime:  
   `"¡Cuidado, muchos procesos de Java activos!"`.

```bash
# Ayuda: comandos equivalentes en la terminal
ps aux | grep java > mis_procesos.txt
wc -l mis_procesos.txt
```

```java

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
```
## Licencia 📄

Este proyecto está bajo la Licencia (Apache 2.0) - mira el archivo [LICENSE.md]([../../../LICENSE.md](https://github.com/jpexposito/code-learn-practice/blob/main/LICENSE)) para detalles.