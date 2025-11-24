---



### 2.3. Responde y comenta la salida ejecutando los cambios que se proponen.

- Solo se usan **3 hilos** (3 GM bots) para atender a todos los jugadores, que esta sucediendo.
    ##### Que los otros jugadores se quedan en espera hasta que un GM bot este libre.
- Los mismos hilos procesan varias peticiones → **reutilización de hilos**. ¿Qué significa esto?
    ##### Que no se tienen que crear hilos nuevos para cada peticion
- ¿Qué pasa si cambias el tamaño del pool a 1? ¿Y a 10?
    ##### Si cambio el tamano del pool a 1 el servidorMazmorras atendera una peticion a la vez,
    ##### Si lo cambio a 10, cada jugador tendra su propio GM bot y todas las peticiones se atenderan simultaneamente.

    


### 3.3. Responde y comenta la salida ejecutando los cambios que se proponen.

- Diferencia con:
  - `execute(Runnable)` → no hay valor de retorno. → Muestra las salidas que demuestran esto.
  - `submit(Callable<V>)` → devuelve un `Future<V>` del que puedes sacar el resultado. → Muestra las salidas que demuestran esto.
- Cómo se pueden lanzar muchos cálculos de daño en paralelo y luego recogerlos todos.
  Al llamar al Tarea Cacular el codigo calcula el dano y se le ponen la clase Future de tipo Future que devuelve una promesa y bloquea el hilo cuando espera el un resultado como cuando se puso el .get.
- Probar a cambiar la probabilidad de crítico y ver cómo sube/baja el daño total.

---



### 4.3. Responde y comenta la salida ejecutando los cambios que se proponen.

- `ScheduledExecutorService` permite:
  - `schedule(...)` → una vez en el futuro. ¿Qué significa esto?
    Que el codigo solo que ejecutara una vez
  - `scheduleAtFixedRate(...)` → repetidamente, cada X tiempo. ¿Qué significa esto?
    que se ejecutar que cada X tiempo dado el codigo y no espera a que termine el programa
- Cómo se comporta el sistema si la tarea tarda más que el período. Modifca, muestra el resultado y comenta.
  Se ejecutaria una vez depues pararia y segiria otra vez para parar despues
- Probar a cambiar el período (1s, 3s…) y la duración del `sleep` del `main`. Modifca, muestra el resultado y comenta.
  si edito el el sleep y el periodo el codigo puede durar mas tiempo o menos

---

## 5. Retos opcionales para subir de nivel 🧙‍♂️

- **Reto 1:**  
  Añade tiempo de cola / prioridad de jugadores en el servidor de mazmorras.
- **Reto 2:**  
  Haz que cada ataque pueda fallar (daño 0) y calcula la **media** de daño.
- **Reto 3:**  
  Añade tipos de spawn “raro” (enemigo épico) con menos probabilidad.
- **Reto 4:**  
  Usa constantes y enums (`enum`) para las zonas, enemigos, clases de personaje, etc.
- **Reto 5:**  
  Escribe tus propias trazas de log con formato tipo:
  `[TIMESTAMP] [SISTEMA] mensaje`.

## Licencia 📄

Este proyecto está bajo la Licencia (Apache 2.0) - mira el archivo [LICENSE.md](<[../../../LICENSE.md](https://github.com/jpexposito/code-learn-practice/blob/main/LICENSE)>) para detalles.
