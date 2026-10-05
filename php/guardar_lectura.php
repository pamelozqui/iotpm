<?php
include "cn.php";
$d = trim($_POST['dispositivo'] ?? '');
$t = $_POST['temperatura'] ?? '';
$h = $_POST['humedad'] ?? '';
if ($d === '' || !is_numeric($t) || !is_numeric($h)) { echo "error"; exit; }
$t = (float)$t;
$h = (float)$h;
$s = mysqli_prepare($c, "INSERT INTO lecturas (dispositivo, temperatura, humedad) VALUES (?, ?, ?)");
mysqli_stmt_bind_param($s, "sdd", $d, $t, $h);
echo mysqli_stmt_execute($s) ? "ok" : "error";
?>