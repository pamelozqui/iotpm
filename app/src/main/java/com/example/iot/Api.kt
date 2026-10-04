package com.example.iot

import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object Api {

    private const val BASE = "http://192.168.137.158/"

    fun post(archivo: String, usuario: String, password: String): String {
        return try {
            val con = URL(BASE + archivo).openConnection() as HttpURLConnection
            con.requestMethod = "POST"
            con.connectTimeout = 8000
            con.readTimeout = 8000
            con.doOutput = true
            val datos = "usuario=" + URLEncoder.encode(usuario, "UTF-8") +
                    "&password=" + URLEncoder.encode(password, "UTF-8")
            con.outputStream.use { it.write(datos.toByteArray()) }
            val respuesta = con.inputStream.bufferedReader().use { it.readText() }.trim()
            con.disconnect()
            respuesta
        } catch (e: Exception) {
            "sinconexion"
        }
    }
}