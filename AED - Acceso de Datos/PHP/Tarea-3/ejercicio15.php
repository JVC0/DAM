<?php
$randomchistes = explode("\n", file_get_contents("excusas.txt"));
$randomchistes = array_filter(array_map('trim', $randomchistes));
if (!file_exists("key.txt") || file_get_contents("key.txt") === false) {
    $key = 0;
} else {
    $key = (int)file_get_contents("key.txt");
    $key = ($key + 1) % count($randomchistes); 
}

file_put_contents("key.txt", $key);

echo $randomchistes[$key];
?>