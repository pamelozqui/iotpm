package com.example.iot

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MonitoreoActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var tvTemp: TextView
    private lateinit var tvHum: TextView
    private lateinit var tvInfo: TextView

    private val refresco = object : Runnable {
        override fun run() {
            cargar()
            handler.postDelayed(this, 5000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoreo)

        tvTemp = findViewById(R.id.tvTemp)
        tvHum = findViewById(R.id.tvHum)
        tvInfo = findViewById(R.id.tvInfo)

        findViewById<Button>(R.id.btnActualizar).setOnClickListener { cargar() }
        findViewById<Button>(R.id.btnVolver).setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        handler.post(refresco)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(refresco)
    }

    private fun cargar() {
        Thread {
            val r = Api.ultimaLectura()
            runOnUiThread {
                when (r) {
                    "sinconexion" -> tvInfo.text = "No hay conexión con el servidor"
                    "vacio" -> tvInfo.text = "Aún no hay lecturas"
                    else -> {
                        val p = r.split("|")
                        if (p.size >= 4) {
                            tvTemp.text = "${p[0]} °C"
                            tvHum.text = "${p[1]} %"
                            tvInfo.text = "Última lectura: ${p[2]} (${p[3]})"
                        }
                    }
                }
            }
        }.start()
    }
}