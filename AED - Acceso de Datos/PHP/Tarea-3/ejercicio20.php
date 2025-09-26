<?php
$randomanimales = explode("\n", file_get_contents("animales.txt"));
$randomanimales = array_filter(array_map('trim', $randomanimales));

$randomcomida = explode("\n", file_get_contents("comidas.txt"));
$randomcomida = array_filter(array_map('trim', $randomcomida));

$randomlugares = explode("\n", file_get_contents("lugares.txt"));
$randomlugares = array_filter(array_map('trim', $randomlugares));

$plantilla = explode(" ", file_get_contents("plantilla.txt"));
$plantilla = array_filter(array_map('trim', $plantilla));

$key = array_search('[animal]', $plantilla);
$plantilla[$key] = $randomanimales[array_rand($randomanimales,1)];
$key = array_search('[lugar]', $plantilla);
$plantilla[$key] = $randomlugares[array_rand($randomlugares,1)];
$key = array_search('[comida].', $plantilla);
$plantilla[$key] = $randomcomida[array_rand($randomcomida,1)];

echo implode(" ", $plantilla);


?>