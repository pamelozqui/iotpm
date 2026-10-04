package com.example.iot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etUsuario: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUsuario = findViewById(R.id.etUsuario)
        etPassword = findViewById(R.id.etPassword)

        findViewById<Button>(R.id.btnIngresar).setOnClickListener { ingresar() }
        findViewById<Button>(R.id.btnGuardar).setOnClickListener { guardarUsuario() }
        findViewById<Button>(R.id.btnBorrar).setOnClickListener { borrarUsuario() }
    }

    private fun camposValidos(): Boolean {
        val usuario = etUsuario.text.toString().trim()
        val password = etPassword.text.toString()

        if (usuario.isEmpty()) {
            etUsuario.error = "Ingrese un usuario"
            etUsuario.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            etPassword.error = "Ingrese una contraseña"
            etPassword.requestFocus()
            return false
        }
        return true
    }

    private fun mensaje(texto: String) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
    }

    private fun ingresar() {
        if (!camposValidos()) return
        val usuario = etUsuario.text.toString().trim()
        val password = etPassword.text.toString()

        Thread {
            val r = Api.post("buscar_producto.php", usuario, password)
            runOnUiThread {
                when (r) {
                    "ok" -> {
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.putExtra("usuario", usuario)
                        startActivity(intent)
                        etPassword.text.clear()
                    }
                    "sinconexion" -> mensaje("No hay conexión con el servidor")
                    else -> mensaje("Usuario o contraseña incorrectos")
                }
            }
        }.start()
    }

    private fun guardarUsuario() {
        if (!camposValidos()) return
        val usuario = etUsuario.text.toString().trim()
        val password = etPassword.text.toString()

        if (password.length < 6) {
            etPassword.error = "Mínimo 6 caracteres"
            etPassword.requestFocus()
            return
        }

        Thread {
            val r = Api.post("ingreso.php", usuario, password)
            runOnUiThread {
                when (r) {
                    "ok" -> {
                        mensaje("Usuario guardado")
                        etUsuario.text.clear()
                        etPassword.text.clear()
                    }
                    "existe" -> mensaje("Ese usuario ya existe")
                    "sinconexion" -> mensaje("No hay conexión con el servidor")
                    else -> mensaje("No se pudo guardar el usuario")
                }
            }
        }.start()
    }

    private fun borrarUsuario() {
        if (!camposValidos()) return
        val usuario = etUsuario.text.toString().trim()
        val password = etPassword.text.toString()

        Thread {
            val r = Api.post("borrar.php", usuario, password)
            runOnUiThread {
                when (r) {
                    "ok" -> {
                        mensaje("Usuario borrado")
                        etUsuario.text.clear()
                        etPassword.text.clear()
                    }
                    "sinconexion" -> mensaje("No hay conexión con el servidor")
                    else -> mensaje("No se pudo borrar: usuario o contraseña incorrectos")
                }
            }
        }.start()
    }
}