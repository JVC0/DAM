<?php
$animales =explode("\n",file_get_contents("animales.txt"));
$animales = array_filter(array_map('trim', $animales));
$adjetivos =explode("\n",file_get_contents("adjetivos.txt"));
$adjetivos = array_filter(array_map('trim', $adjetivos));
for($i=0;$i<=(count($animales)-1);$i++){
    echo "$animales[$i]  $adjetivos[$i] \n";
}
?>