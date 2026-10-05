package com.example.iot

import android.os.Build
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Locale

object Api {

    private val esEmulador = Build.FINGERPRINT.contains("generic") ||
            Build.FINGERPRINT.contains("emulator") ||
            Build.MODEL.contains("sdk", ignoreCase = true)


    private val BASE = if (esEmulador) "http://192.168.137.50/" else "http://localhost:8080/"

    private fun enviar(archivo: String, datos: String): String {
        return try {
            val con = URL(BASE + archivo).openConnection() as HttpURLConnection
            con.requestMethod = "POST"
            con.connectTimeout = 8000
            con.readTimeout = 8000
            con.doOutput = true
            con.outputStream.use { it.write(datos.toByteArray()) }
            val respuesta = con.inputStream.bufferedReader().use { it.readText() }.trim()
            con.disconnect()
            respuesta
        } catch (e: Exception) {
            "sinconexion"
        }
    }

    fun post(archivo: String, usuario: String, password: String): String {
        val datos = "usuario=" + URLEncoder.encode(usuario, "UTF-8") +
                "&password=" + URLEncoder.encode(password, "UTF-8")
        return enviar(archivo, datos)
    }

    fun enviarLectura(dispositivo: String, temperatura: Double, humedad: Double): String {
        val datos = "dispositivo=" + URLEncoder.encode(dispositivo, "UTF-8") +
                "&temperatura=" + String.format(Locale.US, "%.1f", temperatura) +
                "&humedad=" + String.format(Locale.US, "%.1f", humedad)
        return enviar("guardar_lectura.php", datos)
    }

    fun ultimaLectura(): String = enviar("ultima_lectura.php", "")
}