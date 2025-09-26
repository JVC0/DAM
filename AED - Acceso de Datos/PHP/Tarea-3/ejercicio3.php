<?php
file_put_contents("texto.txt", "PHP es muy divertido y potente.");
echo str_word_count(file_get_contents("texto.txt"));
?>