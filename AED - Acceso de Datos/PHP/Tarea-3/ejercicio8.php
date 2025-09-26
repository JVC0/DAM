<?php

    $file = fopen("tabla5.txt", "w");
    for ($i = 1; $i <= 10; $i++) {
        fwrite($file, "5 x $i = ". 5*$i ."\n");
    }
    
    fclose($file);

?>