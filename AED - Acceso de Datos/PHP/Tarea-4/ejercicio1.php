<?php
$operacion = explode("\n", file_get_contents("ops.csv"));
$operacion = array_filter(array_map('trim', $operacion));
array_shift($operacion);
$file = fopen("resultado.txt", "w");
foreach ($operacion as $opera) {
    $miniope = explode(",", $opera);
    switch ($miniope[2]) {
        case 'suma':
            $result = $miniope[0] + $miniope[1];
            fwrite($file, implode(",", $miniope) . ",$result\n");
            break;
        case 'producto':
            $result = $miniope[0] * $miniope[1];
            fwrite($file, implode(",", $miniope) . ",$result\n");
            break;
        case 'division':
            if ($miniope[1] == 0) {
                $result = 0;
                fwrite($file, implode(",", $miniope) . ",$result\n");
                break;
            }
            $result = $miniope[0] / $miniope[1];
            fwrite($file, implode(",", $miniope) . ",$result\n");
            break;
        case 'resta':
            $result = $miniope[0] - $miniope[1];
            fwrite($file, implode(",", $miniope) . ",$result\n");
            break;
        default:
            echo "Operacion no valida\n";
            break;
    }
}
fclose($file);
echo file_get_contents("resultado.txt");
