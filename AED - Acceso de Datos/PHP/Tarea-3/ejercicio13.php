<?php
$randomsong= explode("\n",file_get_contents("canciones.txt"));
$randomsong = array_filter(array_map('trim', $randomsong));
$randkey = array_rand($randomsong,1);
print_r($randomsong[$randkey]);
?>