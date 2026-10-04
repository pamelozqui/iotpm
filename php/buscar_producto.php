<?php
include "cn.php";
$u = trim($_POST['usuario'] ?? '');
$p = $_POST['password'] ?? '';
$s = mysqli_prepare($c, "SELECT hash FROM usuarios WHERE usuario = ?");
mysqli_stmt_bind_param($s, "s", $u);
mysqli_stmt_execute($s);
mysqli_stmt_bind_result($s, $h);
echo (mysqli_stmt_fetch($s) && password_verify($p, $h)) ? "ok" : "error";
?>