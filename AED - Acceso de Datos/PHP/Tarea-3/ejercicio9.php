<?php
    $file = fopen("usuarios.txt", "w");
    $list =['Ana','Pedro','Lucia'];
    foreach ($list as $name ) {
        fwrite($file, $name . "\n");
    }
    
    fclose($file);
    echo file_get_contents("usuarios.txt");


?>