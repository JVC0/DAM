<?php
$organizedata= explode("\n",file_get_contents("ranking.txt"));
$data = [];
foreach ($organizedata as $line) {
    list($name,$value) = explode(": ", $line);
    $data[$name] = (int)$value;
}
arsort($data);
print_r($data);

?>