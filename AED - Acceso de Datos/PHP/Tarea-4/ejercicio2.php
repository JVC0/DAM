<?php
$text = explode(" ", file_get_contents("texto.txt"));
$text = str_replace([",", "."], "", array_filter(array_map('trim', $text)));
$text = array_map('strtolower', $text);
$file = fopen("estadisticas.csv", "w");
fwrite($file, "palabra,frecuencia\n");
foreach (array_count_values($text) as $word => $count) {
    fwrite($file, "$word,$count\n");
}
fclose($file);
