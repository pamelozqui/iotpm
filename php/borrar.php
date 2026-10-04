<?php
include "cn.php";
$u = trim($_POST['usuario'] ?? '');
$p = $_POST['password'] ?? '';
$s = mysqli_prepare($c, "SELECT hash FROM usuarios WHERE usuario = ?");
mysqli_stmt_bind_param($s, "s", $u);
mysqli_stmt_execute($s);
mysqli_stmt_bind_result($s, $h);
if (mysqli_stmt_fetch($s) && password_verify($p, $h)) {
  mysqli_stmt_close($s);
  $d = mysqli_prepare($c, "DELETE FROM usuarios WHERE usuario = ?");
  mysqli_stmt_bind_param($d, "s", $u);
  mysqli_stmt_execute($d);
  echo "ok";
} else { echo "error"; }
?>