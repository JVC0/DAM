<?php

    $file = fopen("tweets.txt", "w");
    fwrite($file, '[' . date('m/d/Y h:i:s') . '] ' . "Aprendiendo PHP con ejercicios divertidos");
    
    fclose($file);

?>