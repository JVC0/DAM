<?php
$comidas =explode("\n",file_get_contents("comidas.txt"));
$comidas = array_filter(array_map('trim', $comidas));
for($i=0;$i<=(count($comidas)-1);$i++){
    echo "$i. $comidas[$i]\n";
}
?>