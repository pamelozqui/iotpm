package com.example.iot

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class SensorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        val tvTemp = findViewById<TextView>(R.id.tvTemp)
        val tvHum = findViewById<TextView>(R.id.tvHum)
        val tvEstado = findViewById<TextView>(R.id.tvEstado)

        findViewById<Button>(R.id.btnEnviar).setOnClickListener {
            val t = Random.nextInt(80, 200) / 10.0
            val h = Random.nextInt(800, 980) / 10.0
            tvTemp.text = "$t °C"
            tvHum.text = "$h %"
            tvEstado.text = "Enviando..."

            Thread {
                val r = Api.enviarLectura("sensor1", t, h)
                runOnUiThread {
                    tvEstado.text = when (r) {
                        "ok" -> "Lectura enviada"
                        "sinconexion" -> "No hay conexión con el servidor"
                        else -> "No se pudo enviar la lectura"
                    }
                }
            }.start()
        }

        findViewById<Button>(R.id.btnVolver).setOnClickListener { finish() }
    }
}