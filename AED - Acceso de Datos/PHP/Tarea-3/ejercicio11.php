<?php
    $file = fopen("diario.txt", "w");
    $list =['[2025-09-24 10:00] Hoy aprendí PHP con ficheros 😄','[2025-09-24 12:00] Almorcé pizza mientras programaba.'];
    foreach ($list as $name ) {
        fwrite($file, $name . "\n");
    }
    
    fclose($file);
    echo file_get_contents("diario.txt");


?>