<?php
$randomexcusas= explode("\n",file_get_contents("excusas.txt"));
$randomexcusas = array_filter(array_map('trim', $randomexcusas));
$randkey = array_rand($randomexcusas,1);
print_r($randomexcusas[$randkey]);
?>