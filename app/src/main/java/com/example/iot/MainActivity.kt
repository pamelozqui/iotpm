package com.example.iot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var etUsuario: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DBHelper(this)
        etUsuario = findViewById(R.id.etUsuario)
        etPassword = findViewById(R.id.etPassword)

        findViewById<Button>(R.id.btnIngresar).setOnClickListener { ingresar() }
        findViewById<Button>(R.id.btnGuardar).setOnClickListener { guardarUsuario() }
        findViewById<Button>(R.id.btnBorrar).setOnClickListener { borrarUsuario() }
    }

    // Valida que ningún campo esté vacío
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

    private fun ingresar() {
        if (!camposValidos()) return
        val usuario = etUsuario.text.toString().trim()
        val password = etPassword.text.toString()

        if (db.validarLogin(usuario, password)) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
            etPassword.text.clear()
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
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

        if (db.crearUsuario(usuario, password)) {
            Toast.makeText(this, "Usuario guardado", Toast.LENGTH_SHORT).show()
            etUsuario.text.clear()
            etPassword.text.clear()
        } else {
            Toast.makeText(this, "Ese usuario ya existe", Toast.LENGTH_SHORT).show()
        }
    }

    private fun borrarUsuario() {
        if (!camposValidos()) return
        val usuario = etUsuario.text.toString().trim()
        val password = etPassword.text.toString()

        if (db.borrarUsuario(usuario, password)) {
            Toast.makeText(this, "Usuario borrado", Toast.LENGTH_SHORT).show()
            etUsuario.text.clear()
            etPassword.text.clear()
        } else {
            Toast.makeText(this, "No se pudo borrar: usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }
}