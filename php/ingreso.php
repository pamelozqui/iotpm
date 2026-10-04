<?php
include "cn.php";
$u = trim($_POST['usuario'] ?? '');
$p = $_POST['password'] ?? '';
if ($u === '' || strlen($p) < 6) { echo "error"; exit; }
$h = password_hash($p, PASSWORD_DEFAULT);
$s = mysqli_prepare($c, "INSERT INTO usuarios (usuario, hash) VALUES (?, ?)");
mysqli_stmt_bind_param($s, "ss", $u, $h);
if (mysqli_stmt_execute($s)) {
  echo "ok";
} else {
  echo "existe";
}
?>