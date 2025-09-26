<?php
    $file = fopen("nombres.txt", "w");
$list =['Ana','Luis','Marta','Carlos','Julia'];
    foreach ($list as $name ) {
        fwrite($file, $name . "\n");
    }
    
    fclose($file);
    echo file_get_contents("nombres.txt");

?>