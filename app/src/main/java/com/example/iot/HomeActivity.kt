package com.example.iot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val usuario = intent.getStringExtra("usuario") ?: ""
        findViewById<TextView>(R.id.tvBienvenida).text = "Bienvenido, $usuario"

        findViewById<Button>(R.id.btnSensor).setOnClickListener {
            startActivity(Intent(this, SensorActivity::class.java))
        }
        findViewById<Button>(R.id.btnMonitoreo).setOnClickListener {
            startActivity(Intent(this, MonitoreoActivity::class.java))
        }
        findViewById<Button>(R.id.btnSalir).setOnClickListener { finish() }
    }
}