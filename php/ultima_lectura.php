<?php
include "cn.php";
$r = mysqli_query($c, "SELECT dispositivo, temperatura, humedad, fecha FROM lecturas ORDER BY id DESC LIMIT 1");
$f = mysqli_fetch_assoc($r);
echo $f ? $f['temperatura'] . "|" . $f['humedad'] . "|" . $f['fecha'] . "|" . $f['dispositivo'] : "vacio";
?>