<?php
$randompalabras = explode("\n", file_get_contents("palabras.txt"));
$randompalabras = array_filter(array_map('trim', $randompalabras));
$randkey = array_rand($randompalabras,1);

print_r(substr($randompalabras[$randkey],-2));
$guess = readline("\nAdivina las dos últimas letras de la palabra seleccionada: ");
while($guess != $randompalabras[$randkey]){
    $guess = readline("Incorrecto. Inténtalo de nuevo: ");
}
echo "¡Correcto! La palabra era: " . $randompalabras[$randkey];
?>