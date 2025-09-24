
# Code, Learn & Practice  
**Procesos y Servicios (modo alumno, sin root) — Trabajo en `$HOME/dam` con *user units* de systemd**

> **Importante:** Esta guía está adaptada para **usuarios sin privilegios de root**.  
> Todo se hace **en tu carpeta** `~/dam` usando **systemd --user** (un administrador por usuario), sin tocar `/etc` ni `/usr/local`.  
> Pega **salidas reales** y responde a las preguntas cortas.

---

## Preparación

Crea tu área de trabajo y variables útiles:

```bash
mkdir -p ~/dam/{bin,logs,units}
export DAM=~/dam
echo 'export DAM=~/dam' >> ~/.bashrc
```

Comprobar versión de systemd y que el *user manager* está activo:

```bash
systemctl --user --version | head -n1
systemctl --user status --no-pager | head -n5
```
**Pega salida aquí:**

```text
 
systemd 255 (255.4-1ubuntu8.6)

● a108pc20
    State: running
    Units: 287 loaded (incl. loaded aliases)
     Jobs: 0 queued
   Failed: 0 units


```

**Reflexiona la salida:**

```text
cuando haces el comando systemctl --user --version | head -n1 da de salida la version del sitema operativo y el segundo comando enseña el status del pc , los trabajos  , unidades cargadas y falladas.
```

---

## Bloque 1 — Conceptos (breve + fuentes)

1) ¿Qué es **systemd** y en qué se diferencia de SysV init?  

**Respuesta:**  

systemd es un sistema de inicializacion y ofrece encender servicios ,parar servicios, manejar sockets, dispositivos etc, mientras que SysVinit que solo enciende y para servicios basado en el runlevel.

_Fuentes:_https://www.maxizamorano.com/entrada/19/proceso-de-arranque-en-linux-systemd-vs-sysv-init 

_Fuentes:_https://natnat1.medium.com/sysvinit-vs-systemd-88395a76e869#:~:text=types%20of%20init%20systems,areas%20and%20other%20unit%20types.

2) **Servicio** vs **proceso** (ejemplos).  

**Respuesta:**  

un proceso es una secuencia de tarea mientras que un servicio es un programa o aplicacion cargado por el sistema operativo

_Fuentes:_ https://www.rafaelsantos.es/web/agora/serviciosxp.pdf

3) ¿Qué son los **cgroups** y para qué sirven?  

**Respuesta:**  
cgroups es una herramienta y sirven para controlar la distribucion de recursos a los procesos.

_Fuentes:_https://elpuig.xeill.net/Members/vcarceler/articulos/introduccion-a-los-grupos-de-control-cgroups-de-linux#:~:text=Su%20finalidad%20es%20contabilizar%20c%C3%B3mo,monitorizar%20con%20la%20herramienta%20perf.

4) ¿Qué es un **unit file** y tipos (`service`, `timer`, `socket`, `target`)?  

**Respuesta:**  

unit file son archivos de configuracion del systemd.Los tipos de fienen el unit file service se refiere al servicio , socket encapsula un socket , timer se utiliza para programar eventos temporales y target para agrupar otras unidades.

_Fuentes:_https://nebul4ck.wordpress.com/2015/02/11/sobre-systemd-mejoras-en-systemd-units-y-targets-uso-de-systemctl-compatibilidad-con-sysv/


5) ¿Qué hace `journalctl` y cómo ver logs **de usuario**?  

**Respuesta:**  
journalctl lee los registors de journald y para leer los log de un usurio tienes que poner journalctl _UID=<ID_del_usuario>.


_Fuentes:_https://www.ninjaone.com/es/blog/gestion-de-logs-de-linux/

---

## Bloque 2 — Práctica guiada (todo en tu `$DAM`)

> Si un comando pide permisos que no tienes, usa la **versión `--user`** o consulta el **ayuda** con `--help` para alternativas.

### 2.1 — PIDs básicos

**11.** PID de tu shell y su PPID.

```bash
echo "PID=$$  PPID=$PPID"
```
**Salida:**

```text
PID=13236  PPID=13217

```

**Pregunta:** ¿Qué proceso es el padre (PPID) de tu shell ahora?  

**Respuesta:**
13217
---

**12.** PID del `systemd --user` (manager de usuario) y explicación.

```bash
pidof systemd  || pgrep -u "$USER" -x systemd
```

**Salida:**

```text
3360
```
**Pregunta:** ¿Qué hace el *user manager* de systemd para tu sesión?  

**Respuesta:**

gestiona las unidades de sytemd en el espacio del usuario

---

### 2.2 — Servicios **de usuario** con systemd

Vamos a crear un servicio sencillo y un timer **en tu carpeta** `~/.config/systemd/user` o en `$DAM/units` (usaremos la primera para que `systemctl --user` lo encuentre).

**13.** Prepara directorios y script de práctica.

```bash
mkdir -p ~/.config/systemd/user "$DAM"/{bin,logs}
cat << 'EOF' > "$DAM/bin/fecha_log.sh"
#!/usr/bin/env bash
mkdir -p "$HOME/dam/logs"
echo "$(date --iso-8601=seconds) :: hello from user timer" >> "$HOME/dam/logs/fecha.log"
EOF
chmod +x "$DAM/bin/fecha_log.sh"
```

**14.** Crea el servicio **de usuario** `fecha-log.service` (**Type=simple**, ejecuta tu script).

```bash
cat << 'EOF' > ~/.config/systemd/user/fecha-log.service
[Unit]
Description=Escribe fecha en $HOME/dam/logs/fecha.log

[Service]
Type=simple
ExecStart=%h/dam/bin/fecha_log.sh
EOF

systemctl --user daemon-reload
systemctl --user start fecha-log.service
systemctl --user status fecha-log.service --no-pager -l | sed -n '1,10p'
```
**Salida (pega un extracto):**

```text
○ fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log
     Loaded: loaded (/home/dam/.config/systemd/user/fecha-log.service; static)
     Active: inactive (dead)

sep 23 18:49:47 a108pc20 systemd[3360]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.

```
**Pregunta:** ¿Se creó/actualizó `~/dam/logs/fecha.log`? Muestra las últimas líneas:

```bash
tail -n 5 "$DAM/logs/fecha.log"
```

**Salida:**

```text
dam  ~  3  0  cat dam/logs/fecha.log 
2025-09-23T18:49:47+01:00 :: hello from user timer

```


---

**15.** Diferencia **enable** vs **start** (modo usuario). Habilita el **timer**.

```bash
cat << 'EOF' > ~/.config/systemd/user/fecha-log.timer
[Unit]
Description=Timer (usuario): ejecuta fecha-log.service cada minuto

[Timer]
OnCalendar=*:0/1
Unit=fecha-log.service
Persistent=true

[Install]
WantedBy=timers.target
EOF

systemctl --user daemon-reload
systemctl --user enable --now fecha-log.timer
systemctl --user list-timers --all | grep fecha-log || true
```

**Salida (recorta):**

```text
Created symlink /home/dam/.config/systemd/user/timers.target.wants/fecha-log.timer → /home/dam/.config/systemd/user/fecha-log.timer.
Wed 2025-09-24 16:27:00 WEST   9s -                                       - fecha-log.timer                fecha-log.service

```
**Pregunta:** ¿Qué diferencia hay entre `enable` y `start` cuando usas `systemctl --user`?  

**Respuesta:**

start es para cuando quieres correr un servicio en el momento mientras que enable es cuando quieres que un servicio se encienda al logearte en el pc y systemctl --user se utiliza para gestionar los servicios de que pertenece a un usuario no root
---

**16.** Logs recientes **del servicio de usuario** con `journalctl --user`.

```bash
journalctl --user -u fecha-log.service -n 10 --no-pager
```

**Salida:**

```text
sep 23 18:49:47 a108pc20 systemd[3360]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
-- Boot 0246a4b0f69d40c5a1803437184027b5 --
sep 24 16:27:15 a108pc20 systemd[3435]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
sep 24 16:28:15 a108pc20 systemd[3435]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
sep 24 16:29:15 a108pc20 systemd[3435]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
sep 24 16:30:15 a108pc20 systemd[3435]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
sep 24 16:31:15 a108pc20 systemd[3435]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
sep 24 16:32:15 a108pc20 systemd[3435]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
sep 24 16:33:15 a108pc20 systemd[3435]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
```
**Pregunta:** ¿Ves ejecuciones activadas por el timer? ¿Cuándo fue la última?  

**Respuesta:**
veo la ejecuiones de timer y la ultima fue a a las 16:33
---

### 2.3 — Observación de procesos sin root

**17.** Puertos en escucha (lo que puedas ver como usuario).

```bash
lsof -i -P -n | grep LISTEN || ss -lntp
```
**Salida:**

```text
State   Recv-Q   Send-Q     Local Address:Port      Peer Address:Port  Process  
LISTEN  0        64               0.0.0.0:2049           0.0.0.0:*              
LISTEN  0        4096       127.0.0.53%lo:53             0.0.0.0:*              
LISTEN  0        4096          127.0.0.54:53             0.0.0.0:*              
LISTEN  0        4096             0.0.0.0:111            0.0.0.0:*              
LISTEN  0        4096             0.0.0.0:8000           0.0.0.0:*              
LISTEN  0        4096             0.0.0.0:39609          0.0.0.0:*              
LISTEN  0        64               0.0.0.0:38351          0.0.0.0:*              
LISTEN  0        32         192.168.122.1:53             0.0.0.0:*              
LISTEN  0        4096           127.0.0.1:631            0.0.0.0:*              
LISTEN  0        4096             0.0.0.0:58111          0.0.0.0:*              
LISTEN  0        4096             0.0.0.0:47591          0.0.0.0:*              
LISTEN  0        4096             0.0.0.0:46145          0.0.0.0:*              
LISTEN  0        64                  [::]:36715             [::]:*              
LISTEN  0        4096                [::]:52879             [::]:*              
LISTEN  0        64                  [::]:2049              [::]:*              
LISTEN  0        4096                   *:22                   *:*              
LISTEN  0        511                    *:80                   *:*              
LISTEN  0        4096                [::]:111               [::]:*              
LISTEN  0        4096                [::]:8000              [::]:*              
LISTEN  0        4096                [::]:57015             [::]:*              
LISTEN  0        4096                [::]:43013             [::]:*              
LISTEN  0        4096               [::1]:631               [::]:*              
LISTEN  0        4096                [::]:59363             [::]:*              
LISTEN  0        4096                   *:9100                 *:* 
```


---

**18.** Ejecuta un proceso bajo **cgroup de usuario** con límite de memoria.

```bash
systemd-run --user --scope -p MemoryMax=50M sleep 200
ps -eo pid,ppid,cmd,stat | grep "[s]leep 200"
```

**Salida:**

```text
ps -eo pid,ppid,cmd,stat | grep "[s]leep 200"
Running as unit: run-rd3ffe1c18cbb44178f37fec8a9829af6.scope; invocation ID: 90a5498b3fbf406ca734ca7947a7ce75


```
**Pregunta:** ¿Qué ventaja tiene lanzar con `systemd-run --user` respecto a ejecutarlo “a pelo”?  

**Respuesta:**
mas control 
---

**19.** Observa CPU en tiempo real con `top` (si tienes `htop`, también vale).

```bash
top -b -n 1 | head -n 15
```
**Salida (resumen):**

```text
top - 16:43:17 up  2:09,  1 user,  load average: 0,58, 0,42, 0,72
Tareas: 415 total,   1 ejecutar,  414 hibernar,    0 detener,    0 zombie
%Cpu(s):  1,4 us,  1,4 sy,  0,0 ni, 96,4 id,  0,7 wa,  0,0 hi,  0,0 si,  0,0 st 
MiB Mem :  31453,3 total,  21528,7 libre,   5521,4 usado,   4951,1 búf/caché    
MiB Intercambio:   2048,0 total,   2048,0 libre,      0,0 usado.  25931,9 dispon

    PID USUARIO   PR  NI    VIRT    RES    SHR S  %CPU  %MEM     HORA+ ORDEN
      1 root      20   0   23240  13820   9212 S   0,0   0,0   0:01.63 systemd
      2 root      20   0       0      0      0 S   0,0   0,0   0:00.01 kthreadd
      3 root      20   0       0      0      0 S   0,0   0,0   0:00.00 pool_wo+
      4 root       0 -20       0      0      0 I   0,0   0,0   0:00.00 kworker+
      5 root       0 -20       0      0      0 I   0,0   0,0   0:00.00 kworker+
      6 root       0 -20       0      0      0 I   0,0   0,0   0:00.00 kworker+
      7 root       0 -20       0      0      0 I   0,0   0,0   0:00.00 kworker+
     10 root       0 -20       0      0      0 I   0,0   0,0   0:00.00 kworker+

```
**Pregunta:** ¿Cuál es tu proceso con mayor `%CPU` en ese momento?  

**Respuesta:**
firefox
---

**20.** Traza syscalls de **tu propio proceso** (p. ej., el `sleep` anterior).
> Nota: Sin root, no podrás adjuntarte a procesos de otros usuarios ni a algunos del sistema.

```bash
pid=$(pgrep -u "$USER" -x sleep | head -n1)
strace -p "$pid" -e trace=nanosleep -tt -c -f 2>&1 | sed -n '1,10p'
```

**Salida (fragmento):**

```text
pid=$(pgrep -u "$USER" -x sleep | head -n1)
strace -p "$pid" -e trace=nanosleep -tt -c -f 2>&1 | sed -n '1,10p'
```


---

### 2.4 — Estados y jerarquía (sin root)

**21.** Árbol de procesos con PIDs.

```bash
pstree -p | head -n 50
```

**Salida (recorta):**

```text
systemd(1)-+-ModemManager(1847)-+-{ModemManager}(1857)
           |                    |-{ModemManager}(1859)
           |                    `-{ModemManager}(1861)
           |-NetworkManager(1820)-+-{NetworkManager}(1852)
           |                      |-{NetworkManager}(1853)
           |                      `-{NetworkManager}(1854)
           |-accounts-daemon(1151)-+-{accounts-daemon}(1187)
           |                       |-{accounts-daemon}(1188)
           |                       `-{accounts-daemon}(1816)
           |-agetty(2241)
           |-apache2(2326)-+-apache2(2335)
           |               |-apache2(2336)
           |               |-apache2(2338)
           |               |-apache2(2339)
           |               `-apache2(2340)
           |-at-spi2-registr(3791)-+-{at-spi2-registr}(3796)
           |                       |-{at-spi2-registr}(3797)
           |                       `-{at-spi2-registr}(3801)
           |-avahi-daemon(1153)---avahi-daemon(1247)
           |-blkmapd(1078)
           |-chrome_crashpad(6193)-+-{chrome_crashpad}(6194)
           |                       `-{chrome_crashpad}(6195)
           |-colord(2015)-+-{colord}(2021)
           |              |-{colord}(2022)
           |              `-{colord}(2024)
           |-containerd(1992)-+-{containerd}(2017)
           |                  |-{containerd}(2018)
           |                  |-{containerd}(2019)
           |                  |-{containerd}(2020)
           |                  |-{containerd}(2025)
           |                  |-{containerd}(2036)
           |                  |-{containerd}(2037)
           |                  |-{containerd}(2051)
           |                  |-{containerd}(2052)
           |                  |-{containerd}(2053)
           |                  |-{containerd}(2054)
           |                  |-{containerd}(2072)
           |                  |-{containerd}(2073)
           |                  |-{containerd}(2553)
           |                  `-{containerd}(2554)
           |-containerd-shim(3186)-+-apache2(3209)-+-apache2(3324)
           |                       |               |-apache2(3325)
           |                       |               |-apache2(3326)
           |                       |               |-apache2(3327)
           |                       |               `-apache2(3328)
           |                       |-{containerd-shim}(3187)
           |                       |-{containerd-shim}(3188)
           |                       |-{containerd-shim}(3189)
           |                       |-{containerd-shim}(3190)
           |                       |-{containerd-shim}(3191)

```
**Pregunta:** ¿Bajo qué proceso aparece tu `systemd --user`?  

**Respuesta:**
systemd(3435)
---

**22.** Estados y relación PID/PPID.

```bash
ps -eo pid,ppid,stat,cmd | head -n 20
```
**Salida:**

```text
    PID    PPID STAT CMD
      1       0 Ss   /sbin/init splash
      2       0 S    [kthreadd]
      3       2 S    [pool_workqueue_release]
      4       2 I<   [kworker/R-rcu_g]
      5       2 I<   [kworker/R-rcu_p]
      6       2 I<   [kworker/R-slub_]
      7       2 I<   [kworker/R-netns]
     10       2 I<   [kworker/0:0H-events_highpri]
     12       2 I<   [kworker/R-mm_pe]
     13       2 I    [rcu_tasks_kthread]
     14       2 I    [rcu_tasks_rude_kthread]
     15       2 I    [rcu_tasks_trace_kthread]
     16       2 S    [ksoftirqd/0]
     17       2 I    [rcu_preempt]
     18       2 S    [migration/0]
     19       2 S    [idle_inject/0]
     20       2 S    [cpuhp/0]
     21       2 S    [cpuhp/1]
     22       2 S    [idle_inject/1]

```
**Pregunta:** Explica 3 flags de `STAT` que veas (ej.: `R`, `S`, `T`, `Z`, `+`).  

**Respuesta:**
T significa suspension,T parado,R corriendo
---

**23.** Suspende y reanuda **uno de tus procesos** (no crítico).

```bash
# Crea un proceso propio en segundo plano
sleep 120 &
pid=$!
# Suspende
kill -STOP "$pid"
# Estado
ps -o pid,stat,cmd -p "$pid"
# Reanuda
kill -CONT "$pid"
# Estado
ps -o pid,stat,cmd -p "$pid"
```
**Pega los dos estados (antes/después):**

```text
 dam  ~  sleep 120 &
[1] 67514
 dam  ~  1  kill -STOP 67514

[1]+  Detenido                sleep 120
 dam  ~  1  ps -o pid,stat,cmd -p 67514
    PID STAT CMD
  67514 T    sleep 120
 dam  ~  1  kill -CONT 67514
 dam  ~  1  ps -o pid,stat,cmd -p 67514
    PID STAT CMD
  67514 S    sleep 120

```
**Pregunta:** ¿Qué flag indicó la suspensión?  

**Respuesta:**
T indica suspension
---

**24. (Opcional)** Genera un **zombie** controlado (no requiere root).

```bash
cat << 'EOF' > "$DAM/bin/zombie.c"
#include <stdlib.h>
#include <unistd.h>
int main() {
  if (fork() == 0) { exit(0); } // hijo termina
  sleep(60); // padre no hace wait(), hijo queda zombie hasta que padre termine
  return 0;
}
EOF
gcc "$DAM/bin/zombie.c" -o "$DAM/bin/zombie" && "$DAM/bin/zombie" &
ps -el | grep ' Z '
```
**Salida (recorta):**

```text

```
**Pregunta:** ¿Por qué el estado `Z` y qué lo limpia finalmente?  

**Respuesta:**

---

### 2.5 — Limpieza (solo tu usuario)

Detén y deshabilita tu **timer/servicio de usuario** y borra artefactos si quieres.

```bash
systemctl --user disable --now fecha-log.timer
systemctl --user stop fecha-log.service
rm -f ~/.config/systemd/user/fecha-log.{service,timer}
systemctl --user daemon-reload
rm -rf "$DAM/bin" "$DAM/logs" "$DAM/units"
```

---



