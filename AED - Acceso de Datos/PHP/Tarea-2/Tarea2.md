# Code, Learn & Practice("Introducción a Php, uso de funciones")

## Número capicúa (palíndromo numérico)

Implementa una función __esCapicua(int $n): bool__ que determine si un número entero positivo es capicúa.

- Un número es capicúa si se lee igual de izquierda a derecha que de derecha a izquierda.

> Ejemplo: `12321` → `true`

```php
<?php
declare( strict_types=1);
function esCapicua(int $n):bool {
    $capicua = (int)strrev((string)$n);
    if ($n == $capicua){
        return true;
    }else{
        return false;
    }

}
esCapicua(212)
?>
```

## Escalera de asteriscos

Implementa una función __montañaAsteriscos(int $n, $m): void__ que imprima una escalera de asteriscos de altura `N`y el tamaño M.

- La primera fila contiene 1 asterisco, la segunda 2, y así hasta `N`, `M` veces.

> Ejemplo con entrada `4,2`:

```text
*.     *
**.   **
***  ***
********
```
```php
<?php

declare( strict_types=1);
function montanaAsteriscos(int $n, int $m) {
    for ($i = 1; $i <= $n; $i++) {
        for($e = 1; $e <= $m; $e++) {
            echo str_repeat("*", $i);
            if($e %2 != 0){
                echo str_repeat("  ", $n-$i);
                }
        }
    echo "\n";
    
}
}
montanaAsteriscos(7,7)
?>
```
## Suma de dígitos

Implementa una función __sumaDigitos(int $n): int__ que calcule la suma de los dígitos de un número entero positivo.

- Descompón el número en dígitos y súmalos.

> Ejemplo: `2025` → `9` (2+0+2+5)
```php

<?php
declare( strict_types=1);
function sumaDigitos(int $n):int {
    $array = str_split((string)$n);
    $result=0;
    foreach($array as $num){
        $result+= (int)$num;
    }
    return $result;
}
echo sumaDigitos(239);
?>
```

## Número secreto (múltiplos de 3 o 5)

Implementa una función __multiplosTresOCinco(int $n): array__ que devuelva todos los múltiplos de 3 o 5 menores que `N`.

- Además, calcula la suma de dichos múltiplos.

> Ejemplo con entrada `10`:

```code
3, 5, 6, 9
Suma = 23
```

```php
<?php
declare( strict_types=1);
function multiplosTresOCinco(int $n): array{
    $number = array();
    for ($i = 1; $i < $n; $i++) {
        if($i %3 ==0){
            $number[]=$i;
        }elseif($i %5 ==0){
            $number[]=$i;
        }

    }
    return $number;
}
print_r(multiplosTresOCinco(10));
print_r(array_sum(multiplosTresOCinco(10)));
?>
```

## Secuencia de Collatz

Implementa una función __secuenciaCollatz(int $n): array__ que genere la secuencia de Collatz a partir de un entero positivo.

- Si el número es par → dividir entre 2.  
- Si es impar → multiplicar por 3 y sumar 1.  
- Repetir hasta llegar a 1.

> Ejemplo con entrada `6`:

```code
6 → 3 → 10 → 5 → 16 → 8 → 4 → 2 → 1
```
```php
<?php
declare(strict_types=1);
function secuenciaCollatz(int $n): array {
    $secuencia = array($n);
    $numero = $n;
    
    while ($numero != 1) {
        if ($numero % 2 == 0) {
            $numero = $numero / 2;
        } else {
            $numeroActual = $numero * 3 + 1;
        }
        $secuencia[] = $numero;
    }
    
    return $secuencia;
}


print_r(secuenciaCollatz(6));
?>
```
---

